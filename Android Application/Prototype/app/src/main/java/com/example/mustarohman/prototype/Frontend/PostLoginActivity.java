package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.mustarohman.prototype.R;

public class PostLoginActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Tours");
        setSupportActionBar(toolbar);

        username = getIntent().getStringExtra("username");

        TextView loggedIn = (TextView) findViewById(R.id.user_textview);
        String text = loggedIn.getText().toString();
        String loggedMsg =  text += " " + username;
        loggedIn.setText(loggedMsg);

    }

    public void addToursToList(){
        //TODO
        //Loop through Tour objects related to user and add them to the list
        //Each list item will have on onClickListener that will open up a TourActivity with the relevant tourpoints
    }

    public void onClickCreateTourBtn(View view) {
        Intent intent = new Intent(this, AddNodeActivity.class);

        //Random code generator
        //putExtra(code)
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
