package com.example.mustarohman.prototype.Frontend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.mustarohman.prototype.Backend.DataBase.DBQueryAsyncTask;

public class MainActivity extends AppCompatActivity {

    public static final String PACKAGE = "com.example.mustarohman.prototype.";
    public static final String TOUR_CODE =  "Tour Code";
    private boolean isRunning;
    private boolean isInternetRunning;

    private CoordinatorLayout coordinatorLayout;
    public static ArrayList<TourLocation> locationslist;
    private DataCaching dataCaching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        locationslist = new ArrayList<>();
        dataCaching = new DataCaching(this);
        isRunning = true;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Hive Tours");
        setSupportActionBar(toolbar);

    }

    private boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());

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

    /**
     * This method starts the location listener and proceeds with the client version of the app.
     * @param view is the button to start the tour
     */
    public void onClickStartBtn(View view) {
        EditText codeEditText = (EditText) findViewById(R.id.code_edit);
        String inputTourCode = codeEditText.getText().toString();

        if (!inputTourCode.equals("")) {
            //Checks if tour code is stored on device
            String storedTourCode = PreferenceManager.getDefaultSharedPreferences(this).getString("inputTour", " ");
            Log.d("onClickStartBtn", "Checking for stored tour code...");
            if (storedTourCode != null && inputTourCode.equals(storedTourCode)){
                Intent intent = new Intent(MainActivity.this, TourActivity.class);
                intent.putExtra(TOUR_CODE, inputTourCode);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                Log.d("onClickStartBtn", "Tour code exists in data");
            } else {
                if (isConnected()) {
                    Log.d("onClickStartBtn", "Network is connected");
                    new DBAsyncTask().execute(inputTourCode);
                } else Snackbar.make(coordinatorLayout, "No network connection. Please try again later", Snackbar.LENGTH_SHORT).show();
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


    private class DBAsyncTask extends AsyncTask<String, String, Void>{

        private ProgressDialog progressDialog;
        private ArrayList<TourLocation> tourLocations;
        private AmazonS3Client s3Client;
        private ArrayList<Bitmap> bitmapMedia = new ArrayList<>();
        private boolean tourCheckSuccess;
        String tourCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Initialising Tour");
            progressDialog.setMessage("Downloading");
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancel(true);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    progressDialog.show();
                                }
                            });
                    builder.create().show();
                }
            });
            progressDialog.show();

            Log.d("onPreExecute", "ProgressDialog displayed");
        }

        @Override
        protected Void doInBackground(String... params) {

            AWSCredentials creden= new BasicAWSCredentials("AKIAJQAUHJ7XGYHTS6AQ","2cX+t23YGpin7L4FbBAcr7zhMJAyePxL9b0bLGxK");
            s3Client = new AmazonS3Client(creden);
            s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));

            //Remove all white space
            tourCode = params[0];
            tourCode = tourCode.replace("//s+", "");

            publishProgress("Checking tour code...", "0");
            if (checkTourCode(tourCode)){
                publishProgress("Downloading tour data...", "50");
                retrieveAndSaveTourData(tourCode);
                tourCheckSuccess =  true;
            } else {
                tourCheckSuccess = false;
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
            if (values.length > 1) {
                int currentProgress = progressDialog.getProgress();
                currentProgress += Integer.parseInt(values[1]);
                Log.d("onProgressUpdate", String.valueOf(currentProgress));
                progressDialog.setProgress(currentProgress);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, TourActivity.class);
            intent.putExtra(TOUR_CODE, tourCode);

            if (tourCheckSuccess) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            } else {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, "Unable to retrieve tour data. Please check tour code", Snackbar.LENGTH_SHORT).show();
            }

        }

        private byte[] turnS3ObjIntoByteArray(S3Object obj){
//            return obj.getObjectContent();
            if (obj == null) {
                Log.d("turnS3ObjIntoByteArray", "S3Object is NULL!");
            } else {
                Log.d("turnS3ObjIntoByteArray", "S3Object is Not NULL!");
            }

            Log.d("turnS3ObjIntoByteArray", obj.getKey());
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(obj.getObjectContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bytes;
        }

        public File storeS3ObjInVidFile(String name, byte[] bytes){
            String ext = name.split("\\.")[1];

            String root = Environment.getExternalStorageDirectory().toString();
            File path = new File(root + "/videos");
            path.mkdirs();

            File vidFilePath = new File(path, name);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            OutputStream out = null;
            try {
                out = new FileOutputStream(vidFilePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte data[] = new byte[4096];
            int count;
            try {
                while ((count = inputStream.read(data)) != -1) {
                    out.write(data, 0, count);
                }
            } catch (IOException e) {
                Snackbar.make(coordinatorLayout, "Failed to download store media on SD card", Snackbar.LENGTH_SHORT);
                e.printStackTrace();
            }

            return vidFilePath;
        }


        /**
         * Checks if inputted tour code is in database
         * @param inputTourCode
         * @return
         */
        private boolean checkTourCode(String inputTourCode) {
            String query = "Select * from tour where tourid = '" + inputTourCode + "';";
            HashMap<String, String> tourIds = null;
            Log.d("checkTourCode", "Retrieving tourIds from database...");

            tourIds = DBQueryAsyncTask.retrieveTours(query);

            if (tourIds == null) return false;
            Log.d("checkTourCode", "End of asynctask");
            return tourIds.containsKey(inputTourCode);
        }

        private void retrieveMediaData(){
            int counter = 0;
            for (TourLocation tourLocation: tourLocations){
                ArrayList<Media> mediaArrayList = tourLocation.getMediaList();
                for (Media media: mediaArrayList){
                    S3Object obj = s3Client.getObject(new GetObjectRequest("hive.testing.storage", media.getInBucketName()));
                    byte[] bytes = turnS3ObjIntoByteArray(obj);
                    if (media.getDatatype() == Media.DataType.IMAGE) {
                        media.setBitmapBytes(bytes);
                    } else {
                        File vidFilePath = storeS3ObjInVidFile(media.getInBucketName(), bytes);
                        media.setVidFile(vidFilePath);
                    }
                }
                int progress = (counter/50) * 100;
                counter++;
                publishProgress("Downloading Media...", String.valueOf(progress));
            }
        }
        /**
         * retrieves and saves the data related to the tour code in the cache.
         * @param inputTourCode code linked to the data that has to be saved
         */
        private ArrayList<TourLocation> retrieveAndSaveTourData(String inputTourCode){

            tourLocations = null;
            tourLocations = DBConnectionSystem.retrieveTourLocations(inputTourCode);

            //Add relevant media data to each tourLocation
            //Query would be called to retrieve media data

            publishProgress("Retrieving media meta data...");
            DBConnectionSystem.locationMediaQuery(tourLocations);
            Log.d("retrieveAndSaveTourData", "Media retrieved");
            publishProgress("Downloading media...");
            retrieveMediaData();
            Log.d("retrieveAndSaveTourData", "Media data downloaded");

            if (tourLocations != null){
                Log.d("retrieveAndSaveTourData", "Saving relevant tour locations to storage...");
                dataCaching.saveDataToInternalStorage(PACKAGE + ".tourLocations", tourLocations);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("inputTour", inputTourCode).commit();
            }
            return  tourLocations;
        }

    }

}
