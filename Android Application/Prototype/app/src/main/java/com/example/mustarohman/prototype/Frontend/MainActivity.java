package com.example.mustarohman.prototype.Frontend;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.concurrent.ExecutionException;

import database.DBQueryAsyncTask;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String PACKAGE = "com.example.mustarohman.prototype.";
    public static final String TOUR_CODE =  "Tour Code";

    private CoordinatorLayout coordinatorLayout;
    public static ArrayList<TourLocation> locationslist;
    private  ArrayList<String> tourCodes;
    private DBConnectionSystem dbConnection = new DBConnectionSystem();
    private DataCaching dataCaching;

    private String[] permissionsNeeded =
            {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.LOCATION_HARDWARE)!= PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissionsNeeded,0);
            }
        }
        else {
           Snackbar.make(coordinatorLayout,
                        "please check if you have allowed your phone to check your loacation and to modify your storage",
                        Snackbar.LENGTH_LONG).show();
        }

        locationslist = new ArrayList<>();
        dataCaching = new DataCaching(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Hive Tours");
        setSupportActionBar(toolbar);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.w("permissionrequestcode", requestCode + "");
//        Log.w("permissions", permissions.toString());
//        Log.w("result", grantResults.length + "");
//
//
//        for (int i = 0; i< grantResults.length; i++) {
//            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
//                builder.setMessage("This permission is requiered to run the app. Are you sure you wish to deny access?")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                MainActivity.this.finish();
//                                System.exit(0);
//                            }
//                        })
//                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                // Create the AlertDialog object and return it
//                builder.create();
//                builder.show();
//            }
//        }
//    }



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
        Intent intent = new Intent(this, TourActivity.class);
        intent.putExtra(TOUR_CODE, inputTourCode);

        if (!inputTourCode.equals("")) {
            //Checks if tour code is stored on device
            String storedTourCode = PreferenceManager.getDefaultSharedPreferences(this).getString("inputTour", " ");
            Log.d("onClickStartBtn", "Checking for stored tour code...");
            if (storedTourCode != null && inputTourCode.equals(storedTourCode)){
            } else {
                try {
                    new DBAsyncTask().execute(inputTourCode).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            Log.d("onClickStartBtn", "Tour code exists in data");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this);
            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

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


    private class DBAsyncTask extends AsyncTask<String, String, Boolean>{

        private ProgressDialog progressDialog;
        private ArrayList<TourLocation> tourLocations;
        private AmazonS3Client s3Client;
        private ArrayList<Bitmap> bitmapMedia = new ArrayList<>();

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
        protected Boolean doInBackground(String... params) {

            AWSCredentials creden= new BasicAWSCredentials("AKIAJQAUHJ7XGYHTS6AQ","2cX+t23YGpin7L4FbBAcr7zhMJAyePxL9b0bLGxK");
            s3Client = new AmazonS3Client(creden);
            s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));

            //Remove all white space
            String tourCode = params[0];
            tourCode = tourCode.replace("//s+", "");

            publishProgress("Checking tour code...", "0");
            if (checkTourCode(tourCode)){
                publishProgress("Downloading tour data...", "50");
                retrieveAndSaveTourData(tourCode);
                return true;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            progressDialog.setMessage(values[0]);
//            progressDialog.setProgress(Integer.parseInt(values[1]));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
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
                        //                    SerialBitmap serialBitmap = new SerialBitmap(bytes, media.getInBucketName());
                        Log.d("retrieveMediaData", "SerialBitmap created");
                        media.setBitmapBytes(bytes);
                    } else {
                        File vidFilePath = storeS3ObjInVidFile(media.getInBucketName(), bytes);
                        media.setVidFile(vidFilePath);
                    }
                }
                counter++;
                publishProgress("Downloading Media...", String.valueOf(counter));
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
            publishProgress("Downloading media...", "1");
            retrieveMediaData();
            Log.d("retrieveAndSaveTourData", "Media data downloaded");

            if (tourLocations != null){
                Log.d("checkTourCode", "Saving relevant tour locations to storage...");
                dataCaching.saveDataToInternalStorage(PACKAGE + inputTourCode + ".tourLocations", tourLocations);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("inputTour", inputTourCode).commit();
            }
            return  tourLocations;
        }
    }
}
