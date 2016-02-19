package com.example.mustarohman.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AddNodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_lightbulb_outline_white_24dp);
        toolbar.setTitle("Edit Tour");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    }

    public void onClickViewStopsMenuItem(MenuItem item) {
    }

    public void onClickChangeTourMenuItem(MenuItem item) {
    }
}
