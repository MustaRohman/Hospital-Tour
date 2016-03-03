package com.example.mustarohman.prototype.Frontend;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.R;

import java.util.ArrayList;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<String> imageFilePaths;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private FrameLayout mainFrame;

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
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        //Which will changed
        imageFilePaths = new ArrayList<>();

        addImageViews(imageFilePaths);
        addVideoViews();

    }

    private void addImageViews(ArrayList<String> filePaths){
        //TODO
        //Loop thru image file paths in arrayList and create views using layoutinflater and add them to the main layout
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < 3; i++){

            int imageResource = R.drawable.cardiac5;
            if (i == 1) imageResource = R.drawable.hospital;
            setImageButtonAndAnimation(inflater, imageResource);

        }

    }

    /**
     * Sets up image button and image view for enlarging animation
     * @param inflater
     * @param res
     */
    private void setImageButtonAndAnimation(LayoutInflater inflater, int res){
        View imageView =  inflater.inflate(R.layout.view_media_image_visible, null);
        ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);
        imageButton.setImageResource(res);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
        param.setMargins(20,20,20,20);

        ImageView enlargedImage = new ImageView(this);
        enlargedImage.setImageResource(res);
        FrameLayout.LayoutParams frameParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        enlargedImage.setLayoutParams(frameParam);
        enlargedImage.setVisibility(View.INVISIBLE);
        enlargedImage.setContentDescription("Cardiac");

        linearLayout.addView(imageView);
        mainFrame.addView(enlargedImage);
        imageView.setLayoutParams(param);
        Log.d("TourPointMediaActivity", "view added");

    }

    private void addVideoViews(){
        //TODO
        //create video/youtube views
    }

    public void onClickImageItem(View view) {
    }
}
