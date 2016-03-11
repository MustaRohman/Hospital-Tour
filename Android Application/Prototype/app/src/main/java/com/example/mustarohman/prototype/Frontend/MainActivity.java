package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
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
    public static ArrayList<TourLocation> locationslist;
    private  ArrayList<String> tourCodes;
    private DBConnectionSystem dbConnection = new DBConnectionSystem();
    private DataCaching dataCaching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

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
            if (storedTourCode != null & inputTourCode.equals(storedTourCode)){
                Log.d("onClickStartBtn", "Tour code exists in data");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            } else if (checkTourCode(inputTourCode)) {
                //shared preference for getting code from EditText
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("inputTour", inputTourCode).commit();

                retrieveAndSaveTourData(inputTourCode);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            } else {
                Snackbar.make(coordinatorLayout, "Invalid tour code", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(coordinatorLayout, "Please enter tour code", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if inputted tour code is in database
     * @param inputTourCode
     * @return
     */
    private boolean checkTourCode(String inputTourCode){
        String query = "Select * from tour where tourid = '" + inputTourCode + "';";
        DBQueryAsyncTask dbQueryAsyncTask = new DBQueryAsyncTask();
        HashMap<String, String> tourIds = null;
        try {
            tourIds = dbQueryAsyncTask.execute(query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (tourIds != null) {
            if (tourIds.containsKey(inputTourCode)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void retrieveAndSaveTourData(String inputTourCode){
        ArrayList<TourLocation> tourLocations = null;
        try {
            tourLocations = dbConnection.getLocations("SELECT * from tour_res, location where tourid ='" + inputTourCode + "'and tour_res.locationid = location.locationid;");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tourLocations != null){
            Log.d("checkTourCode", "Saving relevant tour locations to storage...");
            dataCaching.saveDataToInternalStorage(PACKAGE + inputTourCode +  ".tourLocations", tourLocations);
        }
    }

    public void onClickHelpBtn(View view) {

    }

    public void onSignInBtn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
