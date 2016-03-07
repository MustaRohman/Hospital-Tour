package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TourActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // in Milliseconds
    protected LocationManager locationManager;
    private LinearLayout tourPointsLinear;
    private DataCaching dataCaching;
    private ArrayList<View> tourViewsList;
    private  ArrayList<TourLocation> tourLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is the datacaching class.
        dataCaching = new DataCaching(this.getApplicationContext());
        setContentView(R.layout.activity_tour);
        tourPointsLinear = (LinearLayout) findViewById(R.id.linear_tourpoints);
        tourViewsList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (LogInActivity.LOGGED_IN){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else{
            toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        }
        toolbar.setTitle("Royal Brompton Hospital");


        try {
            //getting text from codeEdit in main class
            String codeFromMain = PreferenceManager.getDefaultSharedPreferences(this).getString("codeEdit", " ");


            //getting tuples from tourRes table where the id = code and storing it in an arrayList
            DBConnectionSystem dbConnection = new DBConnectionSystem();
            tourLocations = dbConnection.getLocations("SELECT * from tour_res, location where tourid ='" + codeFromMain + "'and tour_res.locationid = location.locationid;");
            System.out.println();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addAllTourPointViews();

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

    public void addAllTourPointViews(){
        LayoutInflater inflater = getLayoutInflater();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameView = (TextView) v.findViewById(R.id.text_pointname);
                Intent intent = new Intent(TourActivity.this, TourPointMediaActivity.class);
                intent.putExtra("tour-location-name", nameView.getText().toString());
                startActivity(intent);
            }
        };

        for (TourLocation tourLoc: tourLocations){
            addSingleTourPoint(tourLoc, inflater, listener);
        }
    }

    public void addSingleTourPoint(TourLocation tourLoc, LayoutInflater inflater, View.OnClickListener listener){
        View tourPointView = inflater.inflate(R.layout.view_tourpoint, null);
        tourPointView.setOnClickListener(listener);
        TextView name = (TextView) tourPointView.findViewById(R.id.text_pointname);
        name.setText(tourLoc.getName());
        TextView location = (TextView) tourPointView.findViewById(R.id.text_pointloc);
        location.setText("Some place");
        Log.d("TourLocation", tourLoc.getName());

        tourPointsLinear.addView(tourPointView);
        tourViewsList.add(tourPointView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (LogInActivity.LOGGED_IN){
            menuInflater.inflate(R.menu.menu_tour_add, menu);
            Log.d("onCreateOptionsMenu", "Loaded menu_tour_add");
        } else {
            menuInflater.inflate(R.menu.menu_main, menu);
            Log.d("onCreateOptionsMenu", "Loaded menu_main");

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddTourPoint(MenuItem item) {
        DialogFragment dialog = new AddTourPointDialog();
        dialog.show(getSupportFragmentManager(), "add node");

    }

    public void onClickAddNewLocation(View view) {
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
            Toast.makeText(TourActivity.this, "Provider status changed",
                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(TourActivity.this,
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
