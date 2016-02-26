package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import database.DBQueryAsyncTask;

public class MainActivity extends AppCompatActivity {

    public static final String TOUR_CODE =  "Tour Code";
    private  ArrayList<String> tourCodes;
    private DBConnectionSystem dbConnection = new DBConnectionSystem();
    private DataCaching dataCaching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataCaching = new DataCaching(this.getApplicationContext());


        tourCodes = new ArrayList<>();
        tourCodes.add("BROMP100");
        tourCodes.add("BROMP200");
        tourCodes.add("BROMP300");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Hive Tours");
        setSupportActionBar(toolbar);
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

        /**
         * Retrieve data related to the to tour code
         *
         */

        String query = "Select * from tour where tourid = '"+codeEditText.getText().toString() +"';";

        DBQueryAsyncTask dbQueryAsyncTask = new DBQueryAsyncTask();
        ArrayList<String> tourIds = null;
        ArrayList<TourLocation> locationslist = null;
        try {
           tourIds = dbQueryAsyncTask.execute(query).get();
            locationslist = dbConnection.getTourlocations("select * from tour r , tour_res tr, location l, location_res lr where r.tourid = '" + codeEditText.getText().toString() + "' and r.tourid = tr.tourid and tr.locationid = l.locationid and l.locationid = lr.locationid;");
            dataCaching.saveDataToInternalStorage("locationsList",locationslist );
            System.out.println(locationslist.get(0).getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (tourIds.contains(codeEditText.getText().toString())){
            intent.putExtra(TOUR_CODE, codeEditText.getText());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect tour code", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickHelpBtn(View view) {

    }

    public void onSignInBtn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
