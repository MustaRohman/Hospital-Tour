package com.example.mustarohman.prototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;

public class TourPointInfoActivity extends AppCompatActivity {


    private GridLayout gridLayout;
    private ArrayList<String> imageFilePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO
        //This activity should appear when user enters the respective geofence

        gridLayout = (GridLayout) findViewById(R.id.grid_views);
        //Row count depends on how many images there are from the database
        //Col count has been hard coded to 3 in xml
        gridLayout.setRowCount(3);

        //Which will changed
        imageFilePaths = new ArrayList<>();

        createImageViews(imageFilePaths);
        createVideoViews();

    }

    public void createImageViews(ArrayList<String> filePaths){
        //TODO
        //Loop thru image file paths in arrayList and create views using layoutinflater and add them to the main layout

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < 6; i++){
            View imageView = inflater.inflate(R.layout.view_image_tourinfo, null);
            gridLayout.addView(imageView);
        }

    }

    public void createVideoViews(){
        //TODO
        //create video/youtube views
    }

    public void onClickImageItem(View view) {
    }
}


