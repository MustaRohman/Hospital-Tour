package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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

public class TourActivity extends AppCompatActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1500; // in Milliseconds
    public static final String TOUR_LOCATION= "tour-location-name";
    public static final String TOUR_CODE = "tour-code";

    private String inputTourCode;
    protected LocationManager locationManager;
    private LinearLayout tourPointsLinear;
    private CoordinatorLayout coordinatorLayout;
    private DataCaching dataCaching;
    private ArrayList<View> tourViewsList;
    private  ArrayList<TourLocation> tourLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        dataCaching = new DataCaching(this.getApplicationContext());
        tourPointsLinear = (LinearLayout) findViewById(R.id.linear_tourpoints);
        tourViewsList = new ArrayList<>();

        setUpToolbar();
        loadTourLocations();
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

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (LogInActivity.LOGGED_IN){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else{
            toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        }
        toolbar.setTitle("Royal Brompton Hospital");
    }

    private void loadTourLocations(){
        inputTourCode = PreferenceManager.getDefaultSharedPreferences(this).getString("inputTour", " ");
        Log.d("loadTourLocations", "Retrieved tour code: " + inputTourCode);
        tourLocations = null;
        Log.d("loadTourLocations", "Attempting to load from storage...");
        tourLocations = dataCaching.readFromInternalStorage(MainActivity.PACKAGE + ".tourLocations");
    }

    public void addAllTourPointViews(){
        LayoutInflater inflater = getLayoutInflater();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameView = (TextView) v.findViewById(R.id.text_pointname);
                Intent intent = new Intent(TourActivity.this, TourPointMediaActivity.class);
                intent.putExtra(TOUR_CODE, inputTourCode);
                intent.putExtra(TOUR_LOCATION, nameView.getText().toString());

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        TourActivity.this);
                ActivityCompat.startActivity(TourActivity.this, intent, options.toBundle());
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

            checkInGeofence(location.getLatitude(), location.getLongitude(), 0.00008);
            Log.d("current loc","current latitude: "+location.getLatitude() + "longitude: "+ location.getLongitude()+"");
            Log.d("la+",location.getLatitude()+0.00008+"");
            Log.d("la-", location.getLatitude() - 0.00008 + "");
            Log.d("lo+",location.getLongitude()+0.00008+"");
            Log.d("lo-",location.getLongitude()-0.00008+"");
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


    /**
     * Method that checks if current location is inside a specific location
     *
     * @param la current latitude
     * @param lo current longitude
     * @param sensitivity sensitivity that sets the are around the set location
     * @param geoLa latitude of a location
     * @param geoLo longitude of a lovation
     * @return true if the current location is in  a square with geoLa:geoLo as it center and sensitivity as +/- distance
     */
    public boolean isInSquare(double la, double lo, double sensitivity ,double geoLa ,double geoLo){
        Boolean isInSquare = false;

        //check if we are in square
        if((la <= geoLa+sensitivity && la >= geoLa-sensitivity) && (lo <= geoLo+sensitivity&& lo >= geoLo-sensitivity))
        {
            isInSquare = true;
        }


        return isInSquare;
    }

    /**
     * This method calls InSquare for all the locations in a specific tour.
     *
     * @param la current latitude
     * @param lo current longitude
     * @param sensitivity sensitivity of the search for the in square method call
     */
    public void checkInGeofence(double la, double lo, double sensitivity) {

        ArrayList<TourLocation> nodesList = tourLocations;
        Log.w("list",""+nodesList.size());
        for (int i = 0; i <nodesList.size() ; i++) {

            double currentLa = la;
            double currentLo = lo;
            double geoLaNoe = nodesList.get(i).getLatitude(); //get Latitude;
            double geoloNode = nodesList.get(i).getLongitude();; //get Longitude

            if(isInSquare(la,lo,sensitivity,geoLaNoe,geoloNode)) {
                Log.w("in square","in square");
                Toast.makeText(TourActivity.this, "you have entered your locations: "+nodesList.get(i).getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, TourPointMediaActivity.class);
                startActivity(intent);

            }
        }
    }

    /**
     * Checks if two location areas are overlapping
     *
     * @param la1 latitude of location 1
     * @param lo1 longitude of location 1
     * @param sensitivity area sensitivity
     * @param la2 latitude of location 2
     * @param lo2 longitude of location 2
     * @return true if the locations are overlapping
     */
    public boolean areOverlapping (double la1 , double lo1 , double sensitivity , double la2 , double lo2)
    {
        boolean bottomLeftCornerIn = isInSquare(la1-sensitivity, lo1-sensitivity, sensitivity, la2, lo2);
        boolean bottomRightCornerIn = isInSquare(la1+sensitivity, lo1-sensitivity, sensitivity, la2, lo2);
        boolean topRightCornerIn = isInSquare(la1+sensitivity, lo1+sensitivity, sensitivity, la2, lo2);
        boolean topLeftCornerIn = isInSquare(la1-sensitivity, lo1+sensitivity, sensitivity, la2, lo2);

        if (bottomLeftCornerIn||bottomRightCornerIn||topLeftCornerIn||topRightCornerIn)
        {
            return true;
        }
        return false;
    }
}
