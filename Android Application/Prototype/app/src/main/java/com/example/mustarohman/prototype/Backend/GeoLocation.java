package com.example.mustarohman.prototype.Backend;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by yezenalnafei on 03/03/2016.
 */
public class GeoLocation extends AppCompatActivity{

    private int MINIMUM_TIME_BETWEEN_UPDATES = 1500;
    private int MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private LocationManager locationManager;

    public GeoLocation(){


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATES,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                    new MyLocationListener()
            );
        }
        catch(SecurityException e) {
            Log.w("e", "error1");
        }
    }

    public void showCurrentLocation() {

        try {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            if (location != null) {

                String message = String.format(
                        "Current Location: \n Longitude: %1$s \n Latitude: %2$s",
                        location.getLongitude(), location.getLatitude()
                );


            }
            else
            {
                Log.w("e","previous location is null");
            }

        }
        catch(SecurityException e) {
            Log.w("e", "error2");
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.w("update","location updated");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
