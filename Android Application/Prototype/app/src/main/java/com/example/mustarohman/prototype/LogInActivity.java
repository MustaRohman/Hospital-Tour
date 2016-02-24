package com.example.mustarohman.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    private EditText userEditText, passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Log In");
        setSupportActionBar(toolbar);

        userEditText = (EditText) findViewById(R.id.user_edit);
        passEditText = (EditText) findViewById(R.id.pass_edit);

    }

    public void onLogInBtn(View view) {
        //TODO
        //Check database if username and password combination is correct
        String userString = userEditText.getText().toString();
        Intent intent = new Intent(this, AddNodeActivity.class);
        intent.putExtra("username", userString);
        startActivity(intent);

    }

    public void verifyUser(String username, String password){

    }
}
