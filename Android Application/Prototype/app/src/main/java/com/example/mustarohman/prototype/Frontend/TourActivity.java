package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TourActivity extends AppCompatActivity {

    private LinearLayout tourPointsLinear;
    private DataCaching dataCaching;


    //Map of tour points with their locations. Could be retrieved via JSON
    private HashMap<String,String> tourPoints;
    private ArrayList<View> tourViewsList;
    private  ArrayList<TourLocation> tourLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataCaching = new DataCaching(this.getApplicationContext());


        super.onCreate(savedInstanceState);
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

        tourPoints = new HashMap<>();
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Radiology","Sydney Wing");
        tourPoints.put("Cardiac", "North Wing");
        tourPoints.put("Something", "Main Building");
        tourPoints.put("ER","North Wing");
        tourPoints.put("Operating Theatre","North Wing");
        tourPoints.put("Reception","Main Building");
        tourPoints.put("Staff Room", "Sydney Wing");



        try {
            //getting text from codeEdit in main class
            String codeFromMain = PreferenceManager.getDefaultSharedPreferences(this).getString("codeEdit", " ");


            //getting tuples from tourRes table where the id = code and storing it in an arrayList
            DBConnectionSystem dbConnection = new DBConnectionSystem();
            tourLocations = dbConnection.getTourlocations("SELECT * from tour_res, location where tourid ='" + codeFromMain + "'and tour_res.locationid = location.locationid;");
            System.out.println();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addAllTourPointViews();



    }

    public void addAllTourPointViews(){
        LayoutInflater inflater = getLayoutInflater();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourActivity.this, "Tour point click event", Toast.LENGTH_SHORT)
                .show();
                Intent intent = new Intent(TourActivity.this, TourPointMediaActivity.class);
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
}
