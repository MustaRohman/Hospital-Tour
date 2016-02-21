package com.example.mustarohman.prototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class TourPointActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private ArrayList<String> imageFilePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO
        //Loop thru images and create views using layoutinflater and add them to the main layout
        //This activity should appear when user enters the respective geofence

        gridLayout = (GridLayout) findViewById(R.id.grid_views);
        //Row count depends on how many images there are from the database
        //Col count has been hard coded to 3 in xml
        gridLayout.setRowCount(7);
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
            ImageView img = (ImageView) imageView.findViewById(R.id.image_view);
            img.setImageResource(R.drawable.cardiac5);

            gridLayout.addView(imageView,i);
            Log.d("TourPointActivity", "view added");
        }

    }

    public void createVideoViews(){
        //TODO
        //create video/youtube views
    }

    public void onClickImageItem(View view) {
    }
}
