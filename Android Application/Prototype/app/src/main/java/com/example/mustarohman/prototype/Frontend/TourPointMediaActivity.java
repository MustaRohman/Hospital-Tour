package com.example.mustarohman.prototype.Frontend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.R;

import java.util.ArrayList;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<String> imageFilePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point_media);
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
            ImageButton imageView =  (ImageButton) inflater.inflate(R.layout.view_media_image_visible, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            if (i == 1) imageView.setImageResource(R.drawable.hospital);
            param.setMargins(20,20,20,20);
            imageView.setLayoutParams(param);
            linearLayout.addView(imageView);
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
