package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ImageFullScreenActivity extends AppCompatActivity {

    private ImageView imageView;
    private LinkedHashMap<Media.DataType, Object> gallerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        gallerHashMap = (LinkedHashMap< Media.DataType, Object>) bundle.getSerializable("media");
//        gallerHashMap.
        imageView = (ImageView) findViewById(R.id.image_view);
//        imageView.setImageResource(getIntent().getIntExtra("image-res", 0));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });




    }

    public void onClickExit(View view) {
        finish();
    }
}
