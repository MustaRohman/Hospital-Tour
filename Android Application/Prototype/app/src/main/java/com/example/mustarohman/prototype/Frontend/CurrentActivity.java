package com.example.mustarohman.prototype.Frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.R;

import java.util.concurrent.ExecutionException;

public class CurrentActivity extends AppCompatActivity {

    protected int toastTimer =1500;
        private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
       private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // in Milliseconds
    protected LocationManager locationManager;
    private EditText locationNameEt;


    protected Button retrieveLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationNameEt = (EditText) findViewById(R.id.AddLocationName);


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

                if(locationNameEt!=null){

                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());
                    addButtonConformation(latitude,longitude);
                }else{
                    Toast.makeText(CurrentActivity.this,"Please Enter Location Name",
                            Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Log.w("e", "previous location is null");
            }

        }
        catch(SecurityException e) {
            Log.w("e", "error2");
        }
    }


    public void addButtonConformation(final String latitude,final String longitude ){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String locationName = locationNameEt.getText().toString();
                        DBConnectionSystem dbConnectionSystem = new DBConnectionSystem();
                        String query = "Insert into location (lname,latitude,logitude) values('"+locationName+"',"+latitude+","+longitude+");";
                        try {
                            //Check for successful query added toast for validation.
                          boolean checkQuery =  dbConnectionSystem.UpdateDatabase(query);

                            if (checkQuery){
                                Toast.makeText(CurrentActivity.this,
                                        "Successful",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(CurrentActivity.this,
                                        "Unsuccessful",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confrim adding?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = "location updated";

            // Toast.makeText(CurrentActivity.this, message, Toast.LENGTH_SHORT).show();

//            checkInGeofence(location.getLatitude(), location.getLongitude(), 0.00005);
//            Log.d("current loc","current latitude: "+location.getLatitude() + "longitude: "+ location.getLongitude()+"");
//            Log.d("la+",location.getLatitude()+0.00005+"");
//            Log.d("la-", location.getLatitude() - 0.00005 + "");
//            Log.d("lo+",location.getLongitude()+0.00005+"");
//            Log.d("lo-",location.getLongitude()-0.00005+"");
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(CurrentActivity.this, "Provider status changed",
//                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(CurrentActivity.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String s) {

        }

    }
//
//    //check if location is in square
//    public boolean isInSquare(double la, double lo, double sensitivity ,double geoLa ,double geoLo){
//        Boolean isInSquare = false;
//
//        //check if we are in square
//        if((la <= geoLa+sensitivity && la >= geoLa-sensitivity) && (lo <= geoLo+sensitivity&& lo >= geoLo-sensitivity))
//        {
//            isInSquare = true;
//        }
//
//        return isInSquare;
//    }
//
//
//    public void checkInGeofence(double la, double lo, double sensitivity) {
//
//        ArrayList<TourLocation> nodesList = MainActivity.locationslist;
//        for (int i = 0; i <nodesList.size() ; i++) {
//
//            MainActivity.locationslist.get(i);
//
//            double currentLa = la;
//            double currentLo = lo;
//            double geoLaNoe = nodesList.get(i).getLatitude(); //get Latitude;
//            double geoloNode = nodesList.get(i).getLongitude();; //get Longitude
//
//            if(isInSquare(la,lo,sensitivity,geoLaNoe,geoloNode)) {
//                //Toast.makeText(CurrentActivity.this, "you have Near your locations: "+nodesList.get(i).getName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, LogInActivity.class);
//                startActivity(intent);
//
//            }
//        }
//    }

}
