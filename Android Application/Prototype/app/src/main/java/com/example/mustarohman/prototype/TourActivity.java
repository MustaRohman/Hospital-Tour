package com.example.mustarohman.prototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TourActivity extends AppCompatActivity {

    private LinearLayout tourPointsLin;

    //Map of tour points with their locations. Could be retrieved via JSON
    private HashMap<String,String> tourPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        tourPointsLin = (LinearLayout) findViewById(R.id.linear_tourpoints);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Royal Brompton Hospital");
        setSupportActionBar(toolbar);

        tourPoints = new HashMap<>();
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Radiology","Sydney Wing");
        tourPoints.put("Cardiac","North Wing");
        tourPoints.put("Something","Main Building");

        addTourPointViews();

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
                //Create new activity for the tourPoint
            }
        };
        Iterator it = tourPoints.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> point = (Map.Entry) it.next();
            View tourPointView = inflater.inflate(R.layout.view_tourpoint, null);
            tourPointView.setOnClickListener(listener);
            TextView name = (TextView) tourPointView.findViewById(R.id.text_pointname);
            name.setText(point.getKey());
            TextView location = (TextView) tourPointView.findViewById(R.id.text_pointloc);
            location.setText(point.getValue());

            tourPointsLin.addView(tourPointView);
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
