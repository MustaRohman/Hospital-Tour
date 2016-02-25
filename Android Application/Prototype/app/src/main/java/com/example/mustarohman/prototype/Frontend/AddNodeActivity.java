package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mustarohman.prototype.R;

public class AddNodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Edit Tour");

        TextView loggedInText = (TextView) findViewById(R.id.logged_in_text);
        String loggedInString = loggedInText.getText().toString();
        String username = getIntent().getStringExtra("username");
        loggedInString += " " + username;
        loggedInText.setText(loggedInString);

        setSupportActionBar(toolbar);

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
}
