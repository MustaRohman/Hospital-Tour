package com.example.mustarohman.prototype.Frontend;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ArrayList<String> imageFilePaths;
    private String inputTourCode;
    private String tourLocationName;


    private HashMap<Media.DataType,Object> galleryHashMap;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private FrameLayout mainFrame;

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

        //TODO
        //Loop thru images and create views using layoutinflater and add them to the main layout
        //This activity should appear when user enters the respective geofence

        linearLayout = (LinearLayout) findViewById(R.id.grid_views);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        //Which will changed
        imageFilePaths = new ArrayList<>();

        loadBitmapImages();
//        addImageViews(galleryHashMap);
        addVideoViews();

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
        ArrayList<Media> mediaArrayList = currentTourLocation.getMediaList();
        LayoutInflater inflater = getLayoutInflater();
        Bitmap thumb;
        for (Media media: mediaArrayList){
            if (media.getDatatype() == Media.DataType.IMAGE){
                thumb = media.returnBitmap();
                galleryHashMap.put(Media.DataType.IMAGE, thumb);
                setImageThumbButton(inflater, thumb, galleryHashMap.size() - 1);
            } else{
                File file = media.getVidFile();
                galleryHashMap.put(Media.DataType.VIDEO, file);
//                thumb = ThumbnailUtils.createVideoThumbnail(file.toString(),
//                        MediaStore.Images.Thumbnails.MINI_KIND);
                setVidThumbButton(inflater, media.getVidFile().getPath());
//                String name = media.getInBucketName();
//                String ext = name.split("\\.")[1];
//                byte[] bytes = media.getBitmapBytes();
//
//                String root = Environment.getExternalStorageDirectory().toString();
//                File path = new File(root + "/videos");
//                path.mkdirs();
//                File file = new File(path, name);
//                InputStream inputStream = new ByteArrayInputStream(bytes);
//                OutputStream out = null;
//                try {
//                    out = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                byte data[] = new byte[4096];
//                int count;
//                try {
//                    while ((count = inputStream.read(data)) != -1) {
//                        out.write(data, 0, count);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

        }

        Log.d("loadBitmapImages", String.valueOf(galleryHashMap.size()));

    }

    private void addImageViews(ArrayList<Bitmap> bitmapArrayList){
        //TODO
        //Loop thru image file paths in arrayList and create views using layoutinflater and add them to the main layout
        LayoutInflater inflater = getLayoutInflater();

        for (Bitmap bitmap: bitmapArrayList){
//            setImageThumbButton(inflater, bitmap);
        }
    }

    private void setVidThumbButton(LayoutInflater inflater, String filename){
        final View imageView =  inflater.inflate(R.layout.view_media_image, null);
        final ImageButton imageButton = (ImageButton) imageView.findViewById(R.id.image_button);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
        param.gravity = Gravity.CENTER;
        param.setMargins(20, 30, 30, 30);
        imageView.setLayoutParams(param);

        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        String split[] = filename.split("\\.");
        Uri data = Uri.parse(filename);
        if (split[split.length-1].toLowerCase().equals("avi")) {
            intent.setDataAndType(data, "video/avi");
            Log.d("setVidThumbButton", "vidtype set to avi");
        } else{
            intent.setDataAndType(data, "video/mp4");
            Log.d("setVidThumbButton", "vidtype is not avi");
        }
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
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

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
