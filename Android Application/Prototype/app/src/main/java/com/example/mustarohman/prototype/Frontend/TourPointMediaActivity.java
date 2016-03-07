package com.example.mustarohman.prototype.Frontend;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.R;

import java.util.ArrayList;

public class TourPointMediaActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<String> imageFilePaths;
    private String tourLocationName;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private FrameLayout mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_point_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tourLocationName = getIntent().getStringExtra("tour-location-name");
        toolbar.setTitle(tourLocationName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
            setImageButton(inflater, imageResource);
        }
    }

    /**
     * Sets up image button and image view for enlarging animation
     * @param inflater
     * @param res
     */
    private void setImageButton(LayoutInflater inflater, final int res){
        final View imageView =  inflater.inflate(R.layout.view_media_image_visible, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);
        imageButton.setImageResource(res);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250);
        param.setMargins(30, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(TourPointMediaActivity.this, ImageFullScreenActivity.class);
        intent.putExtra("image-res", res);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    v.setTransitionName(String.valueOf(R.string.transition_name));
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            TourPointMediaActivity.this,
//                            new Pair<View, String>(v, getString(R.string.transition_name)));
//                    ActivityCompat.startActivity(TourPointMediaActivity.this, intent, options.toBundle());
//                    Log.d("setImageButton", "Build version sdk is >= lollipop");
//                } else {
//                  zoomImage(imageView, res);
//                  startActivity(intent);
//                }

                startActivity(intent);
            }
        });

        linearLayout.addView(imageView);
        Log.d("TourPointMediaActivity", "view added");

    }

    private void zoomImage(final View imageButton, int res) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(res);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        imageButton.getGlobalVisibleRect(startBounds);
        findViewById(R.id.main_frame)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        imageButton.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageButton.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        imageButton.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    private void addVideoViews(){
        //TODO
        //create video/youtube views
    }

    public void onClickImageItem(View view) {
    }
}
