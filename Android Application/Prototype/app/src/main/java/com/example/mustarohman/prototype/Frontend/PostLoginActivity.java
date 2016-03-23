package com.example.mustarohman.prototype.Frontend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.R;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class PostLoginActivity extends AppCompatActivity {

    private String username;
    private DBConnectionSystem dbConnectionSystem;
    private HashMap<String,String> tourCodes;
    private LinearLayout tourCodesLinear;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Tours");
        setSupportActionBar(toolbar);

        username = getIntent().getStringExtra("username");
        tourCodes = new HashMap<>();
        dbConnectionSystem = new DBConnectionSystem();

        tourCodesLinear = (LinearLayout) findViewById(R.id.modify_tourlist);

        TextView loggedIn = (TextView) findViewById(R.id.user_textview);
        String text = loggedIn.getText().toString();
        String loggedMsg =  text += " " + username;
        loggedIn.setText(loggedMsg);

        try {
            tourCodes = dbConnectionSystem.getTourCodes(username);
            System.out.println(tourCodes.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addToursToList();

    }

    /**
     * This method adds the tours to the post login Activity list
     */
    public void addToursToList(){
        //TODO
        //Loop through Tour objects related to user and add them to the list
        //Each list item will have on onClickListener that will open up a TourActivity with the relevant tourpoints

        //getting shared preference from codeEdit
        final String codeFromMain = PreferenceManager.getDefaultSharedPreferences(this).getString("codeEdit", " ");
        sharedPreferences = getSharedPreferences("codeEdit", Context.MODE_PRIVATE);

      final  Context context = this;


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tourbutton = (Button) v;
                Intent intent = new Intent(PostLoginActivity.this, EditTourActivity.class);
                intent.putExtra("tourcode", tourbutton.getText().toString());

                //editing the sharedPreference
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("codeEdit", tourbutton.getText().toString()).commit();
                startActivity(intent);
            }
        };

        for (HashMap.Entry<String, String> tourCode : tourCodes.entrySet()){
            Button btn = new Button(this);
            btn.setText(tourCode.getKey()+" "+tourCode.getValue());
            btn.setGravity(Gravity.LEFT);
            btn.setPadding(40, 10, 10, 10);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setOnClickListener(listener);
            tourCodesLinear.addView(btn);
        }
    }

    /**
     * This method gets the last known location of the user and adds it as a room in the database. It will first ask the user for a name
     * @param view button that adds current location as a room in the database
     */
    public void onClickAddRoom(View view) {
        Intent intent = new Intent(this, CurrentActivity.class);
        startActivity(intent);
    }





}
