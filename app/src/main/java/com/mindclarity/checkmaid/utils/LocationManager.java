package com.mindclarity.checkmaid.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mindclarity.checkmaid.ui.activities.PropertiesActivity;

import java.io.IOException;
import java.util.List;

public class LocationManager {

    public static LatLng getLocationFromAddress(Context context,String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude() );

        }
        catch (IOException ex)
        {
            Toast.makeText(context,"Could not get lat/long : "+ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return latLng;
    }

    public static boolean validateGeoLocation (double validRadius,double currentLat, double currentLong, double actualLat, double actualLong) {

        float[] distance = new float[1];
        double raidusInMeter=validRadius/3.281;
        Location.distanceBetween(actualLat, actualLong, currentLat, currentLong, distance);
        if (distance[0] <raidusInMeter) {
            return true;
        }
        else
        {
            return false;
        }
    }
}