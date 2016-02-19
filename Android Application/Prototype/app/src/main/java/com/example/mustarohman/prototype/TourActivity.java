package com.example.mustarohman.prototype;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TourActivity extends AppCompatActivity {

    private LinearLayout tourPointsLinear;

    //Map of tour points with their locations. Could be retrieved via JSON
    private HashMap<String,String> tourPoints;
    private ArrayList<View> tourViewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        tourPointsLinear = (LinearLayout) findViewById(R.id.linear_tourpoints);
        tourViewsList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Royal Brompton Hospital");
        setSupportActionBar(toolbar);

        tourPoints = new HashMap<>();
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Radiology","Sydney Wing");
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Something","Main Building");
        tourPoints.put("ER","North Wing");
        tourPoints.put("Operating Theatre","North Wing");
        tourPoints.put("Reception","Main Building");
        tourPoints.put("Staff Room","Sydney Wing");
        tourPoints.put("Toilet","Sydney Wing");

        addTourPointViews();

        //
        View point = tourViewsList.get(0);
        ImageView img = (ImageView) point.findViewById(R.id.thumbnail);
        img.setImageResource(R.drawable.cardiac5);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void addTourPointViews(){
        LayoutInflater inflater = getLayoutInflater();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourActivity.this, "Tour point click event", Toast.LENGTH_SHORT)
                .show();
//                Intent intent = new Intent(TourActivity.this, TourPointActivity.class);
//                startActivity(intent);
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

}
