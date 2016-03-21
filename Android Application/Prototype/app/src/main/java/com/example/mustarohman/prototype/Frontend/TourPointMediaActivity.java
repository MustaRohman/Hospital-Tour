package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.File;
import java.util.ArrayList;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private String inputTourCode;
    private String tourLocationName;
    private TourLocation currentTourLocation;
    public final static String BUNDLE_NAME = "TourPointMediaActivity.bundle";
    public final static String MEDIA_INDEX_TAG = "media-index";
    public final static String MEDIA_TOTAL_TAG = "media-total";

    private final Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        inputTourCode = intent.getStringExtra(TourActivity.TOUR_CODE);
        tourLocationName = intent.getStringExtra(TourActivity.TOUR_LOCATION);
        toolbar.setTitle(tourLocationName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.grid_views);

        //Which will changed

        loadBitmapImages();
    }

    public void loadBitmapImages(){

        DataCaching dataCaching = new DataCaching(this);
        ArrayList<TourLocation> tourLocations = null;
        Log.d("loadTourLocations", "Attempting to load from storage...");
        tourLocations = dataCaching.readFromInternalStorage(MainActivity.PACKAGE + inputTourCode + ".tourLocations");
        currentTourLocation = null;
        for (TourLocation tourLocation: tourLocations){
            if (tourLocation.getName().equals(tourLocationName)){
                currentTourLocation = tourLocation;
            }
        }
        ArrayList<Media> mediaArrayList = currentTourLocation.getMediaList();
        bundle.putSerializable("media", mediaArrayList);
        LayoutInflater inflater = getLayoutInflater();
        Bitmap thumb;
        int indexCounter = 0;
        for (Media media: mediaArrayList){
            if (media.getDatatype() == Media.DataType.IMAGE){
                thumb = media.returnBitmap();
                setImageThumbButton(inflater, thumb, indexCounter);
            } else{
                File file = media.getVidFile();
                setVidThumbButton(inflater, file.getPath(), indexCounter ) ;
            }
            indexCounter++;
        }

        Log.d("loadBitmapImages", String.valueOf(indexCounter));
    }


    private void setVidThumbButton(LayoutInflater inflater, String filepath, int mediaIndex){
        final View imageView =  inflater.inflate(R.layout.view_media_image, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
        param.gravity = Gravity.CENTER;
        param.setMargins(20, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(TourPointMediaActivity.this, ImageFullScreenActivity.class);
        intent.putExtra(BUNDLE_NAME, bundle);
        intent.putExtra(MEDIA_INDEX_TAG, mediaIndex);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        linearLayout.addView(imageView);
        Log.d("TourPointMediaActivity", "view added");
    }


    private void setImageThumbButton(LayoutInflater inflater, Bitmap bitmap, int  mediaIndex){
        final View imageView =  inflater.inflate(R.layout.view_media_image, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);

        imageButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 370, false));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
        param.gravity = Gravity.CENTER;
        param.setMargins(20, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(TourPointMediaActivity.this, ImageFullScreenActivity.class);
        intent.putExtra(BUNDLE_NAME, bundle);
        intent.putExtra(MEDIA_INDEX_TAG, mediaIndex);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

        linearLayout.addView(imageView);
        Log.d("TourPointMediaActivity", "view added");
    }
}
