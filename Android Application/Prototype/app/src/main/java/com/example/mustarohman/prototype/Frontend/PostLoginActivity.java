package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PostLoginActivity extends AppCompatActivity {

    private String username;
    private DBConnectionSystem dbConnectionSystem;
    private ArrayList<String> tourCodes;
    private LinearLayout tourCodesLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Tours");
        setSupportActionBar(toolbar);

        username = getIntent().getStringExtra("username");
        tourCodes = new ArrayList<>();
        dbConnectionSystem = new DBConnectionSystem();

        tourCodesLinear = (LinearLayout) findViewById(R.id.modify_tourlist);

        TextView loggedIn = (TextView) findViewById(R.id.user_textview);
        String text = loggedIn.getText().toString();
        String loggedMsg =  text += " " + username;
        loggedIn.setText(loggedMsg);

        try {
            tourCodes = dbConnectionSystem.getTourCodes();
            System.out.println(tourCodes.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addToursToList();

    }

    public void addToursToList(){
        //TODO
        //Loop through Tour objects related to user and add them to the list
        //Each list item will have on onClickListener that will open up a TourActivity with the relevant tourpoints


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tourbutton = (Button) v;
                Intent intent = new Intent(PostLoginActivity.this, AddNodeActivity.class);
                intent.putExtra("tourcode",tourbutton.getText().toString());
                startActivity(intent);
            }
        };

        for (String tourCode: tourCodes){
            Button btn = new Button(this);
            btn.setText(tourCode);
            btn.setGravity(Gravity.LEFT);
            btn.setPadding(40, 10, 10, 10);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setOnClickListener(listener);
            tourCodesLinear.addView(btn);
        }
    }

    public void onClickCreateTourBtn(View view) {
        Intent intent = new Intent(this, AddNodeActivity.class);

        //Random code generator
        //putExtra(code)
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onClickAddRoom(View view) {
        Intent intent = new Intent(this, CurrentActivity.class);
        startActivity(intent);
    }
}
