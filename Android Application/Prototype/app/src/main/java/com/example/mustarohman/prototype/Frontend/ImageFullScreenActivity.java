package com.example.mustarohman.prototype.Frontend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.File;
import java.util.ArrayList;

public class ImageFullScreenActivity extends AppCompatActivity {




    private ViewFlipper viewFlipper;
    private TourLocation currentTourLocation;
    private ArrayList<Media> mediaArrayList;
    private MediaController mediaControls;
    private int position = 0;
    private int index;
    private Animation slideLeft, slideRight;
    private float lastX;
    //views for image inflater
    private TextView textname;
    private ImageView image;
    private View screen;
    //views for video inflater
    private TextView VideoTextname;
    private RelativeLayout relativeLayout;
    private View Videoscreen;

    private LinearLayout layout;


    /**
     * @param savedInstanceState
     * used to save an instance of the screen upon rotating
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int position = viewFlipper.getDisplayedChild();
        savedInstanceState.putInt("TAB_NUMBER", position);
        super.onSaveInstanceState(savedInstanceState);


    }

    /**
     * @param savedInstanceState
     * used to restore the instance of the screen when rotating it
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int position1 = savedInstanceState.getInt("TAB_NUMBER", position);
        viewFlipper.setDisplayedChild(position1);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        //initialising variables
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);

        layout = (LinearLayout) findViewById(R.id.image_Layout);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(TourPointMediaActivity.BUNDLE_NAME);

        index = intent.getIntExtra(TourPointMediaActivity.MEDIA_INDEX_TAG, 0);
        Log.d("ImageFullScreenActivity", "image-index is " + index);
        mediaArrayList = (ArrayList<Media>) bundle.getSerializable("media");


        //here we create dimensions for bitmap images/image views
        int height;
        int width;

        //getting the display of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //code used to test the orientation and return values based upon it
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        //code for portrait mode
            width = size.x;
            height = size.y / 2;


        } else {


            width = size.x / 2;
            height = size.y - 200;
        }

        //setting animations for view flipper
        slideLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(slideLeft);
        viewFlipper.setOutAnimation(slideRight);



        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });


        //this method is used to create media from the array list and add it to the view flipper
        for (Media media : mediaArrayList) {

        //description of the media
        String description = media.getDescription();

        if (media.getDatatype() == Media.DataType.IMAGE) {

        //creating the image
        Bitmap bitmap = Bitmap.createScaledBitmap(media.returnBitmap(), width, height, false);

        //we use a layout inflater to add the image and description, then add the instance of the layout to the
        //view flipper

        LayoutInflater inflater = LayoutInflater.from(ImageFullScreenActivity.this);
        screen = inflater.inflate(R.layout.image_full_screen_content, null);

        textname = (TextView) screen.findViewById(R.id.name);
        image = (ImageView) screen.findViewById(R.id.img);


        //handling the exception in case the description online is empty/has no value
        if (description != null) {
        textname.setText(description);
        } else {
        Toast.makeText(this, "no description available", Toast.LENGTH_LONG).show();
        }


        image.setImageBitmap(bitmap);
        viewFlipper.addView(screen);


        //same process as above but for the videos

        } else {

        LayoutInflater inflater = LayoutInflater.from(ImageFullScreenActivity.this);
        Videoscreen = inflater.inflate(R.layout.content_video, null);

        VideoTextname = (TextView) Videoscreen.findViewById(R.id.video_text);
        relativeLayout = (RelativeLayout) Videoscreen.findViewById(R.id.video_frame);

        if (description != null) {
        VideoTextname.setText(description);


        } else {
        Toast.makeText(this, "no description available(check online)", Toast.LENGTH_LONG).show();

        }

        //getting the video file
        VideoView videoView = createVideoView(media.getVidFile());
        relativeLayout.addView(videoView);

        viewFlipper.addView(Videoscreen);
        }}

        //hiding system bar
        hideSystemUI();
        showSystemUI();

    }


    /**
     * @param touchevent
     * @return
     *  Using the following method, we will handle all screen swaps.
     */
    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

        case MotionEvent.ACTION_DOWN:
        lastX = touchevent.getX();
        break;
        case MotionEvent.ACTION_UP:
        float currentX = touchevent.getX();

        // Handling left to right screen swap.
        if (lastX < currentX) {

        // If there aren't any other children, just break.
        if (viewFlipper.getDisplayedChild() == 0)
        break;

        // Next screen comes in from left.
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
        // Current screen goes out from right.
        viewFlipper.setOutAnimation(this, R.anim.slight_out_from_right);

        // Display next screen.
        viewFlipper.showNext();
        }

        // Handling right to left screen swap.
        if (lastX > currentX) {

        // If there is a child (to the left), kust break.
         if (viewFlipper.getDisplayedChild() == 1)
                        break;

         // Next screen comes in from right.
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
        // Current screen goes out from left.
        viewFlipper.setOutAnimation(this, R.anim.slide_out_from_left);

        // Display previous screen.
         viewFlipper.showPrevious();
                }
         break;
        }
        return false;
    }


        public void setUpViewFlipper() {
        viewFlipper.setOnTouchListener(new View.OnTouchListener()

        {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
        onBackPressed();
        return false;
         }});
    }


    /**
     * @param file
     * @return videoView
     * in this method we are creating an instance of a video from media
     *
     */
    private VideoView createVideoView(File file) {
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


    /**
     * This code hides the system bars.
     */

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        layout.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
         | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
         | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
         | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
         | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        layout.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }











}