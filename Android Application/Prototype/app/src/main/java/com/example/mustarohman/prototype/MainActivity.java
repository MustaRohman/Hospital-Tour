package com.example.mustarohman.prototype;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TOUR_CODE =  "Tour Code";
    private  ArrayList<String> tourCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (tourCodes.contains(codeEditText.getText().toString())){
            intent.putExtra(TOUR_CODE, codeEditText.getText());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect tour code", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickHelpBtn(View view) {

    }

    public void onSignInBtn(View view) {
    }
}
