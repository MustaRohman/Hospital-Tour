package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.DBQueryAsyncTask;

public class MainActivity extends AppCompatActivity {

    public static final String TOUR_CODE =  "Tour Code";
    private CoordinatorLayout coordinatorLayout;
    private  ArrayList<String> tourCodes;
    private DBConnectionSystem dbConnection = new DBConnectionSystem();
    private DataCaching dataCaching;
    public static ArrayList<TourLocation> locationslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        locationslist = new ArrayList<>();
        dataCaching = new DataCaching(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Hive Tours");
        setSupportActionBar(toolbar);

//        S3Object object = null;

        try {
            new DBAsyncTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickStartBtn(View view) {
        Intent intent = new Intent(this, TourActivity.class);
        EditText codeEditText = (EditText) findViewById(R.id.code_edit);

        if (!codeEditText.getText().toString().equals("")) {
            //shared preference for getting code from EditText
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("codeEdit", codeEditText.getText().toString()).commit();


            String query = "Select * from tour where tourid = '" + codeEditText.getText().toString() + "';";

            DBQueryAsyncTask dbQueryAsyncTask = new DBQueryAsyncTask();
            HashMap<String, String> tourIds = null;
            try {
                tourIds = dbQueryAsyncTask.execute(query).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

//       dataCaching.saveDataToInternalStorage("locationsList",locationslist);

            if (tourIds != null) {
                if (tourIds.containsKey(codeEditText.getText().toString())) {
                    intent.putExtra(TOUR_CODE, codeEditText.getText());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MainActivity.this);
                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                } else {
                    Snackbar.make(coordinatorLayout, "Invalid tour code", Snackbar.LENGTH_SHORT).show();
                }
            }
        } else {
            Snackbar.make(coordinatorLayout, "Please enter tour code", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void onClickHelpBtn(View view) {

    }

    public void onSignInBtn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public class DBAsyncTask extends AsyncTask<Void, Bitmap, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            AWSCredentials creden= new BasicAWSCredentials("AKIAJQAUHJ7XGYHTS6AQ","2cX+t23YGpin7L4FbBAcr7zhMJAyePxL9b0bLGxK");
            AmazonS3Client s3Client = new AmazonS3Client(creden);
            s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));
            Bitmap bitmap = turnS3ObjectIntoBitmap(s3Client, "1457788264.jpg");

            return null;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            ImageView imageView = (ImageView) findViewById(R.id.image_view);
            imageView.setImageBitmap(values[0]);
        }

        public Bitmap turnS3ObjectIntoBitmap(AmazonS3Client s3Client, String key){
            S3Object obj = s3Client.getObject(new GetObjectRequest("hive.testing.storage", key));
            return BitmapFactory.decodeStream(obj.getObjectContent());
        }

        public void createImageRes(Bitmap bitmap){

        }
    }
}
