package com.example.smarttravel.help;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String STORAGE_NAME = "Coordinates";

    private static SharedPreferences coordinates = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    public static void init( Context cntxt ){
        context = cntxt;
    }

    private static void init(){
        coordinates = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = coordinates.edit();
    }

    public static void addProperty( String name, String value ){
        if(coordinates == null ){
            init();
        }
        editor.putString( name, value );
        editor.commit();
    }

    public static String getProperty( String name ){
        if(coordinates == null ){
            init();
        }
        return coordinates.getString( name, null );
    }
}
