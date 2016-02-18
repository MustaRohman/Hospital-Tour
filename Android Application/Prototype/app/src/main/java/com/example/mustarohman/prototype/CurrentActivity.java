package com.example.mustarohman.prototype;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CurrentActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds

    protected LocationManager locationManager;


    protected Button retrieveLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



//        try {
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    MINIMUM_TIME_BETWEEN_UPDATES,
//                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
//                    new MyLocationListener()
//            );
//        }
//        catch(SecurityException e) {
//            Log.w("e", "error1");
//        }

        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

    }

    protected void showCurrentLocation() {

        try {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if (location != null) {

                String message = String.format(
                        "Current Location \n Longitude: %1$s \n Latitude: %2$s   from showCurrentLocation",
                        location.getLongitude(), location.getLatitude()
                );
                Toast.makeText(CurrentActivity.this, message,
                        Toast.LENGTH_LONG).show();
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

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s     from location changed",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(CurrentActivity.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(CurrentActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(CurrentActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {

        }

    }

}
