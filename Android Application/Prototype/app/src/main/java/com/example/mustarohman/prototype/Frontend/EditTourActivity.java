package com.example.mustarohman.prototype.Frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.R;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.DBQueryAsyncTask;

public class EditTourActivity extends AppCompatActivity {

    private String tourCodeString;
    DBQueryAsyncTask getTourCodes ;
    HashMap<String,String> tourCode;
    Button btSave;
    EditText tourNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Tour");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tourCodeString = getIntent().getStringExtra("tourcode");


        try {
            getTourCodes = new DBQueryAsyncTask();
          tourCode = getTourCodes.execute("Select * from tour where tourid = '"+ tourCodeString + "';").get();
        } catch (ExecutionException e) {
            Log.w("e", "TourRetive error in activity EditTourActivity");
        } catch (InterruptedException e) {
            Log.w(""+e, "TourRetive error in activity EditTourActivity");
        }

        setTextOfViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_node, menu);
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

    public void onViewStopsBtnClick(View view) {
        Intent intent = new Intent(this, TourActivity.class);
        startActivity(intent);

    }

    public void onClickAddNode(View view) {
        Intent intent = new Intent(this, CurrentActivity.class);
        startActivity(intent);
    }

    public void onClickAddStopMenuItem(MenuItem item) {
        Intent intent = new Intent(this, CurrentActivity.class);
        startActivity(intent);
    }

    public void onClickViewStopsMenuItem(MenuItem item) {
        Intent intent = new Intent(this, TourActivity.class);
        startActivity(intent);
    }

    public void onClickChangeTourMenuItem(MenuItem item) {
        //TODO
        //Sliding list of tour codes. Should reload the current activity
    }

    public void generateTourCode(){


    }

    public void onClickSaveTour(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        DBConnectionSystem dbConnectionSystem = new DBConnectionSystem();

                        dbConnectionSystem.UpdateDatabase("update tour set tour_name= '"+tourNameEdit.getText().toString()+"' where tourid = '"+tourCodeString+"';");

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Confirm The Changes?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void setTextOfViews(){
        TextView tourCodeText = (TextView) findViewById(R.id.tour_code);
        tourCodeText.setText(tourCodeString);

        //Retrieve name of tour from the database
        generateTourCode();

        TextView loggedInText = (TextView) findViewById(R.id.logged_in_text);
        String loggedInString = loggedInText.getText().toString();
        String username = getIntent().getStringExtra("username");
        loggedInString += " " + LogInActivity.USER_NAME;
        loggedInText.setText(loggedInString);

         tourNameEdit = (EditText) findViewById(R.id.tourname_edit);
        tourNameEdit.setText(tourCode.get(tourCodeString));
    }
}
