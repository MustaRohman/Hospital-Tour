package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        tourLocations =  dataCaching.readFromInternalStorage("locationslist");
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

        LogInActivity.LOGGED_IN = true;

//        if (LogInActivity.LOGGED_IN){
//            Button btn = new Button(this);
//            btn.setOnClickListener(new Button.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(TourActivity.this, "Add button clicked", Toast.LENGTH_LONG).show();
//                }
//            });
//            btn.setBackgroundResource(R.drawable.ic_note_add_white_24dp);
//            toolbar.addView(btn);
//        }

        tourPoints = new HashMap<>();
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Radiology","Sydney Wing");
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Something","Main Building");
        tourPoints.put("ER","North Wing");
        tourPoints.put("Operating Theatre","North Wing");
        tourPoints.put("Reception","Main Building");
        tourPoints.put("Staff Room","Sydney Wing");

        addTourPointViews();

    }

    public void addTourPointViews(){
        LayoutInflater inflater = getLayoutInflater();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourActivity.this, "Tour point click event", Toast.LENGTH_SHORT)
                .show();
                Intent intent = new Intent(TourActivity.this, TourPointActivity.class);
                startActivity(intent);
            }
        };

        //Iterate through map and creates View object and adds to activity screen
        Iterator it = tourPoints.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> point = (Map.Entry) it.next();
            View tourPointView = inflater.inflate(R.layout.view_tourpoint, null);
            tourPointView.setOnClickListener(listener);
            TextView name = (TextView) tourPointView.findViewById(R.id.text_pointname);
            name.setText(point.getKey());
            TextView location = (TextView) tourPointView.findViewById(R.id.text_pointloc);
            location.setText(point.getValue());

            tourPointsLinear.addView(tourPointView);
            tourViewsList.add(tourPointView);
        }
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
    }
}
