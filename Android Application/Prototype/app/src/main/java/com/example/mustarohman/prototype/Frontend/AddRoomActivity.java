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

public class AddRoomActivity extends AppCompatActivity {

    protected int toastTimer =1500;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // in Milliseconds
    protected LocationManager locationManager;
    private EditText locationNameEt;


    protected Button retrieveLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_current);

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

    /**
     * This method retrieves the last knwn location of the network provider
     */
    protected void showCurrentLocation() {

        try {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {

                String message = String.format(
                        "Current Location: \n Longitude: %1$s \n Latitude: %2$s",
                        location.getLongitude(), location.getLatitude()
                );
                Toast.makeText(AddRoomActivity.this, message,
                        Toast.LENGTH_SHORT).show();

                if(locationNameEt!=null){
                    addButtonConfirmation(location.getLatitude(),location.getLongitude());
                }else{
                    Toast.makeText(AddRoomActivity.this,"Please Enter Location Name",
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

    /**
     * This method inserts the current location to the database and prints a toast with the name given by the user in the confirmation popup.
     *
     * @param latitude : latitude (double) of the location to add
     * @param longitude : longitude (double) of the location to add
     */
    public void addButtonConfirmation(final double latitude,final double longitude ){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            /**
             * onClick to open dialog box when adding rooms to the database
             */
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String locationName = locationNameEt.getText().toString();
                        locationName = locationName.replace(" ", "");
                        DBConnectionSystem dbConnectionSystem = new DBConnectionSystem();
                        try {
                            //Check for successful query added toast for validation.
                          boolean checkQuery =  dbConnectionSystem.UpdateDatabase(locationName, latitude, longitude);

                            if (checkQuery){
                                Toast.makeText(AddRoomActivity.this,
                                        "Successful",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddRoomActivity.this,
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
                        dialog.cancel();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confrim adding?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    /**
     * This method terminates the current Activity
     * @param view button that calls method
     */
    public void onViewStopsBtnClick (View view)
    {
        finish();
    }


    /**
     * Location Listener that updates the last known location
     */
    private class MyLocationListener implements LocationListener {


        public void onLocationChanged(Location location) {

            //String message = "location updated";
            // Toast.makeText(AddRoomActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(AddRoomActivity.this, "Provider status changed",
//                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(AddRoomActivity.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String s) {

        }
    }
}
