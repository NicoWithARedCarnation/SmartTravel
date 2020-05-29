package com.example.smarttravel.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smarttravel.R;
import com.example.smarttravel.help.Preferences;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements DrivingSession.DrivingRouteListener  {

    public String frm_lt = "latitude_from";
    public  double from_t = Double.parseDouble(Preferences.getProperty(frm_lt));
    public String frm_ln = "longitude_from";
    public  double from_n = Double.parseDouble(Preferences.getProperty(frm_ln));
    public String to_lt = "latitude_to";
    public  double to_t = Double.parseDouble(Preferences.getProperty(to_lt));
    public String to_ln = "longitude_to";
    public  double to_n = Double.parseDouble(Preferences.getProperty(to_ln));

    private final String MAPKIT_API_KEY = "790f3210-d37b-40fc-9a25-321a27a310e5";
    private final Point ROUTE_START_LOCATION = new Point(from_t, from_n);
    private final Point ROUTE_END_LOCATION = new Point(to_t, to_n);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2);

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private void submitRequest() {
        DrivingOptions options = new DrivingOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        drivingSession = drivingRouter.requestRoutes(requestPoints, options, this);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(getActivity());
        DirectionsFactory.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapview);
        mapView.getMap().move(new CameraPosition(
                SCREEN_CENTER, 5, 0, 0));

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        submitRequest();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> routes) {
        for (DrivingRoute route : routes) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
