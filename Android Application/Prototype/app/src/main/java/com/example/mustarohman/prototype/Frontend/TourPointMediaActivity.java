 package com.example.mustarohman.prototype.Frontend;

 import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.HashMap;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private String inputTourCode;
    private String tourLocationName;

    private HashMap<Media.DataType,Object> galleryHashMap;

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

        galleryHashMap = new HashMap<>();

        DataCaching dataCaching = new DataCaching(this);
        ArrayList<TourLocation> tourLocations = null;
        Log.d("loadTourLocations", "Attempting to load from storage...");
        tourLocations = dataCaching.readFromInternalStorage(MainActivity.PACKAGE + inputTourCode + ".tourLocations");
        TourLocation currentTourLocation = null;
        for (TourLocation tourLocation: tourLocations){
            if (tourLocation.getName().equals(tourLocationName)){
                currentTourLocation = tourLocation;
            }
        }
        ArrayList<Media> mediaArrayList ;
            mediaArrayList = currentTourLocation.getMediaList();

        LayoutInflater inflater = getLayoutInflater();
        Bitmap thumb;
        for (Media media: mediaArrayList){
            if (media.getDatatype() == Media.DataType.IMAGE){
                thumb = media.returnBitmap();
                galleryHashMap.put(Media.DataType.IMAGE, thumb);
                setImageThumbButton(inflater, thumb, galleryHashMap.size() - 1);
            } else{
                File file = media.getVidFile();
                setVidThumbButton(inflater, file.getPath());
                galleryHashMap.put(Media.DataType.VIDEO, file);
            }
        }

        Log.d("loadBitmapImages", String.valueOf(galleryHashMap.size()));
    }



    private void setVidThumbButton(LayoutInflater inflater, String filepath){
        final View imageView =  inflater.inflate(R.layout.view_media_image, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
        param.gravity = Gravity.CENTER;
        param.setMargins(20, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(TourPointMediaActivity.this, VidActivity.class);
        intent.putExtra("filepath", filepath);
//        String split[] = filename.split("\\.");
//        Uri data = Uri.parse(filename);
//        if (split[split.length-1].toLowerCase().equals("avi")) {
//            intent.setDataAndType(data, "video/avi");
//            Log.d("setVidThumbButton", "vidtype set to avi");
//        } else{
//            intent.setDataAndType(data, "video/mp4");
//            Log.d("setVidThumbButton", "vidtype is not avi");
//        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

        linearLayout.addView(imageView);
        Log.d("TourPointMediaActivity", "view added");
    }


    private void setImageThumbButton(LayoutInflater inflater, Bitmap bitmap, int  bitmapIndex){
        final View imageView =  inflater.inflate(R.layout.view_media_image, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);

        imageButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 370, false));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
        param.gravity = Gravity.CENTER;
        param.setMargins(20, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(TourPointMediaActivity.this, ImageFullScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("media", galleryHashMap);
        intent.putExtra("image-index", bitmapIndex);
        intent.putExtras(bundle);
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
