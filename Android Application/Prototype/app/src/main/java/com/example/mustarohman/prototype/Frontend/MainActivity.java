package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "eu-west-1:00e58264-33f5-422d-b7b3-7af17d77fa99", // Identity Pool ID
                Regions.EU_WEST_1
                // Region
        );

        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

// Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

        TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

        File file = new File(this.getFilesDir(), "image");

        TransferObserver observer = transferUtility.download(
                "storage.s3.website.com",     /* The bucket to download from */
                "1457788264.jpg",    /* The key for the object to download */
                file        /* The file to download the object to */
        );
        
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d("state", String.valueOf(state));
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.d("progress", String.valueOf(percentage));
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("Error", "error");
            }
        });
        Log.d("onCreate", "File downloaded");
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
}
