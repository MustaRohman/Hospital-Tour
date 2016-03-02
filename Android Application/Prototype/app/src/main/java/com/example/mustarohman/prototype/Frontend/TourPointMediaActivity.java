package com.example.mustarohman.prototype.Frontend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.R;

import java.util.ArrayList;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<String> imageFilePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tour Point Info");
        setSupportActionBar(toolbar);
        //TODO
        //Loop thru images and create views using layoutinflater and add them to the main layout
        //This activity should appear when user enters the respective geofence

        linearLayout = (LinearLayout) findViewById(R.id.grid_views);

        //Which will changed
        imageFilePaths = new ArrayList<>();

        createImageViews(imageFilePaths);
        createVideoViews();

    }

    public void createImageViews(ArrayList<String> filePaths){
        //TODO
        //Loop thru image file paths in arrayList and create views using layoutinflater and add them to the main layout

        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < 3; i++){
            View imageView = inflater.inflate(R.layout.view_image_test, null);
            ImageView img = (ImageView) imageView.findViewById(R.id.image_view);
            img.setImageResource(R.drawable.cardiac5);

            linearLayout.addView(imageView, i);
            Log.d("TourPointMediaActivity", "view added");
        }

    }

    public void createVideoViews(){
        //TODO
        //create video/youtube views
    }

    public void onClickImageItem(View view) {
    }
}
