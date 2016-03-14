package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.DBQueryAsyncTask;

public class MainActivity extends AppCompatActivity {

    public static final String PACKAGE = "com.example.mustarohman.prototype.";
    public static final String TOUR_CODE =  "Tour Code";

    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;

    public static ArrayList<TourLocation> locationslist;
    private  ArrayList<String> tourCodes;
    private DBConnectionSystem dbConnection = new DBConnectionSystem();
    private DataCaching dataCaching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        locationslist = new ArrayList<>();
        dataCaching = new DataCaching(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Hive Tours");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickStartBtn(View view) {
        EditText codeEditText = (EditText) findViewById(R.id.code_edit);
        String inputTourCode = codeEditText.getText().toString();
        Intent intent = new Intent(this, TourActivity.class);
        intent.putExtra(TOUR_CODE, inputTourCode);

        if (!inputTourCode.equals("")) {
            //Checks if tour code is stored on device
            String storedTourCode = PreferenceManager.getDefaultSharedPreferences(this).getString("inputTour", " ");
            Log.d("onClickStartBtn", "Checking for stored tour code...");
            if (storedTourCode != null && inputTourCode.equals(storedTourCode)){
                Log.d("onClickStartBtn", "Tour code exists in data");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            } else {
                try {
                    new DBAsyncTask().execute(inputTourCode).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
////                Log.d("onClickStartBtn", "Tour code not stored in data, retrieving from database...");
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        MainActivity.this);
//                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

            }

        } else {
            Snackbar.make(coordinatorLayout, "Please enter tour code", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onClickHelpBtn(View view) {

    }

    public void onSignInBtn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private class DBAsyncTask extends AsyncTask<String, String, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {

            String tourCode = params[0];
            publishProgress("Checking tour code...");
            if (checkTourCode(tourCode)){
                publishProgress("Downloading tour data...");
                retrieveAndSaveTourData(tourCode);
                return true;
            }

            return false;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            Snackbar.make(coordinatorLayout, values[0], Snackbar.LENGTH_SHORT).show();
        }

        /**
         * Checks if inputted tour code is in database
         * @param inputTourCode
         * @return
         */
        private boolean checkTourCode(String inputTourCode) {
            String query = "Select * from tour where tourid = '" + inputTourCode + "';";
            HashMap<String, String> tourIds = null;
            Log.d("checkTourCode", "Retrieving tourIds from database...");

            tourIds = DBQueryAsyncTask.retrieveTours(query);

            Log.d("checkTourCode", "End of asynctask");
            return tourIds.containsKey(inputTourCode);
        }

        public void retrieveAndSaveTourData(String inputTourCode){
            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("inputTour", inputTourCode).commit();
            ArrayList<TourLocation> tourLocations = null;
            tourLocations = DBConnectionSystem.retrieveTourLocations("SELECT * from tour_res, location where tourid ='" + inputTourCode + "'and tour_res.locationid = location.locationid;");

            if (tourLocations != null){
                Log.d("checkTourCode", "Saving relevant tour locations to storage...");
                dataCaching.saveDataToInternalStorage(PACKAGE + inputTourCode +  ".tourLocations", tourLocations);
            }
        }
    }

}
