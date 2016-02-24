package com.example.mustarohman.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import database.loginQuery;

public class LogInActivity extends AppCompatActivity {

    private EditText userEditText, passEditText;
    public static boolean LOGGED_IN;

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
        Intent intent = new Intent(this, PostLoginActivity.class);
        intent.putExtra("username", userString);
        HashMap<String, String> hashMap = null;
        loginQuery query = new loginQuery();
        try {

            hashMap = query.execute("select * from users where username = '" + userString + "';").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (!hashMap.isEmpty()) {
            if (hashMap.get(userString).equals(passEditText.getText().toString())) {
                startActivity(intent);
                LOGGED_IN = true;
            } else {
                Toast.makeText(this, "Invaild", Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(this, "User does not exsits", Toast.LENGTH_SHORT).show();

        }
    }
    public void verifyUser(String username, String password){

    }
}
