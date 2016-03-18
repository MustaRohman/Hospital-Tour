package com.example.mustarohman.prototype.Frontend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.mustarohman.prototype.R;

public class ImageFullScreenActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

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
