package com.example.mustarohman.prototype.Frontend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ImageFullScreenActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private TourLocation currentTourLocation;
    private ArrayList<Media> mediaArrayList;
    private MediaController mediaControls;
    private int position = 0;
    private int index;
    private Animation slideLeft, slideRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TourPointMediaActivity.BUNDLE_NAME);

        index = intent.getIntExtra(TourPointMediaActivity.MEDIA_INDEX_TAG, 0);
        Log.d("ImageFullScreenActivity", "image-index is " + index);
        mediaArrayList = (ArrayList<Media>) bundle.getSerializable("media");

        slideLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(slideLeft);
        viewFlipper.setOutAnimation(slideRight);
        setUpViewFlipper(index);

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });

        Button prevBtn = (Button) findViewById(R.id.previous_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });
        Button nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });



    }

    public void setUpViewFlipper(int index){
//        int i = 0;
//        while (i != index){
//
//        }


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        for (Media media: mediaArrayList){
            if (media.getDatatype() == Media.DataType.IMAGE){
                Bitmap bitmap = media.returnBitmap();
//                ImageView imageView = createImageView(Bitmap.createScaledBitmap(bitmap,size.x, 500, false));
                ImageView imageView = createImageView(bitmap);
                viewFlipper.addView(imageView);

            } else {
                //set the media controller buttons
                VideoView videoView = createVideoView(media.getVidFile());
                viewFlipper.addView(videoView);
            }
        }

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });
    }

    private ImageView createImageView(Bitmap bitmap){
        ImageView imageView = new ImageView(this);
        //setting image resource
        imageView.setImageBitmap(bitmap);
        //setting image position
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }

    private VideoView createVideoView(File file){
        if (mediaControls == null) {
            mediaControls = new MediaController(ImageFullScreenActivity.this);
        }

        //initialize the VideoView
        final VideoView videoView = new VideoView(this);

        // create a progress bar while the video file is loading
        final ProgressDialog progressDialog = new ProgressDialog(ImageFullScreenActivity.this);
        // set a title for the progress bar
        progressDialog.setTitle("Playing Video...");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
//        progressDialog.show();

        try {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            String filePath = file.getPath();
            Log.d("VidActivity", filePath);
            videoView.setVideoPath(filePath);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                mediaControls.setVisibility(View.VISIBLE);
                mediaPlayer.setVolume(0f, 0f);
                //if we have a position on savedInstanceState, the video playback should start from here
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    videoView.pause();
                }
            }
        });

        return videoView;
    }

    public void onClickExit(View view) {
        finish();
    }

}
