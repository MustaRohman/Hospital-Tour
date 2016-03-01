package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;

public class CurrentActivity extends AppCompatActivity {

    protected int toastTimer =1500;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // in Milliseconds

    protected LocationManager locationManager;


    protected Button retrieveLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);

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

        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

    }

    protected void showCurrentLocation() {

        try {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            if (location != null) {

                String message = String.format(
                        "Current Location: \n Longitude: %1$s \n Latitude: %2$s",
                        location.getLongitude(), location.getLatitude()
                );
                Toast.makeText(CurrentActivity.this, message,
                        Toast.LENGTH_SHORT).show();


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
            String message = "location updated";

            // Toast.makeText(CurrentActivity.this, message, Toast.LENGTH_SHORT).show();

            checkInGeofence(location.getLatitude(), location.getLongitude(), 0.00005);
            Log.d("current loc","current latitude: "+location.getLatitude() + "longitude: "+ location.getLongitude()+"");
            Log.d("la+",location.getLatitude()+0.00005+"");
            Log.d("la-", location.getLatitude() - 0.00005 + "");
            Log.d("lo+",location.getLongitude()+0.00005+"");
            Log.d("lo-",location.getLongitude()-0.00005+"");
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(CurrentActivity.this, "Provider status changed",
                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(CurrentActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String s) {

        }

    }

    //check if location is in square
    public boolean isInSquare(double la, double lo, double sensitivity ,double geoLa ,double geoLo){
        Boolean isInSquare = false;

        //check if we are in square
        if((la <= geoLa+sensitivity && la >= geoLa-sensitivity) && (lo <= geoLo+sensitivity&& lo >= geoLo-sensitivity))
        {
            isInSquare = true;
        }

        return isInSquare;
    }


    public void checkInGeofence(double la, double lo, double sensitivity) {

        ArrayList<TourLocation> nodesList = MainActivity.locationslist;
        for (int i = 0; i <nodesList.size() ; i++) {

            MainActivity.locationslist.get(i);

            double currentLa = la;
            double currentLo = lo;
            double geoLaNoe = nodesList.get(i).getLatitude(); //get Latitude;
            double geoloNode = nodesList.get(i).getLongitude();; //get Longitude

            if(isInSquare(la,lo,sensitivity,geoLaNoe,geoloNode)) {
                //Toast.makeText(CurrentActivity.this, "you have Near your locations: "+nodesList.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);

            }
        }
    }

}
