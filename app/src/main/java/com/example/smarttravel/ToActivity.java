package com.example.smarttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.smarttravel.help.Preferences;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

public class ToActivity extends AppCompatActivity  implements Session.SearchListener, CameraListener {

    private MapView mapView;
    private EditText searchEdit;
    private Button next_btn;
    private SearchManager searchManager;
    private Session searchSession;

    public String lt, ln;
    SharedPreferences sharedPreferences;
    public String to_lt = "latitude_to";
    public String to_ln = "longitude_to";
    private final String MAPKIT_API_KEY = "790f3210-d37b-40fc-9a25-321a27a310e5";

    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to);

        next_btn = findViewById(R.id.to_home_bnt);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lt == null && ln == null){
                    Toast.makeText(ToActivity.this, "Обязательно заполните поле!", Toast.LENGTH_SHORT).show();;
                } else {
                    Preferences.init(getApplicationContext());
                    Preferences.addProperty(to_lt, lt);
                    Preferences.addProperty(to_ln, ln);
                    startActivity(new Intent(ToActivity.this, Home_Activity.class));
                }

            }
        });
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        mapView = findViewById(R.id.map2);
        mapView.getMap().addCameraListener(this);

        searchEdit = findViewById(R.id.to1);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(!searchEdit.getText().toString().equals("")){
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        submitQuery(searchEdit.getText().toString());
                    }
                }
                return false;
            }
        });

        mapView.getMap().move(
                new CameraPosition(new Point(55.7522200, 37.6155600),
                        14.0f, 0.0f, 0.0f));
        if (!searchEdit.getText().toString().equals("")){
            submitQuery(searchEdit.getText().toString());
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }



    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();

    }

    @Override
    public void onCameraPositionChanged(
            Map map,
            CameraPosition cameraPosition,
            CameraUpdateSource cameraUpdateSource,
            boolean finished) {
        if (finished) {
            if(!searchEdit.getText().toString().equals("")){
                submitQuery(searchEdit.getText().toString());
            }
        }
    }

    @Override
    public void onSearchResponse(@NonNull Response response) {
        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
        mapObjects.clear();
        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if(!searchEdit.getText().toString().equals("")){
                if (resultLocation != null) {
                    mapObjects.addPlacemark(
                            resultLocation,
                            ImageProvider.fromResource(this, R.drawable.search_result));
                    lt = String.valueOf(resultLocation.getLatitude());
                    ln = String.valueOf(resultLocation.getLongitude());
                }
            }
        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        String errorMessage = getString(R.string.unknown_error_message);
        if (error instanceof RemoteError) {
            errorMessage = getString(R.string.remote_error_message);
        } else if (error instanceof NetworkError) {
            errorMessage = getString(R.string.network_error_message);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}