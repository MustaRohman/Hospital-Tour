package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.R;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LogInActivity extends AppCompatActivity {

    private EditText userEditText, passEditText;
    private CoordinatorLayout coordinatorLayout;
    public static boolean LOGGED_IN;
    public static String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Log In");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.out.println("------------------------");
            }
        });

        userEditText = (EditText) findViewById(R.id.user_edit);
        passEditText = (EditText) findViewById(R.id.pass_edit);
    }

    public void onLogInBtn(View view) {
        //TODO
        //Check database if username and password combination is correct

        String userString = userEditText.getText().toString();
        USER_NAME = userString;
        Intent intent = new Intent(this, PostLoginActivity.class);
        intent.putExtra("username", userString);
        HashMap<String, String> hashMap = null;
        DBConnectionSystem query = new DBConnectionSystem();

        try {
            hashMap = query.loginQueryFetch("select * from users where username = '" + userString + "';");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (!hashMap.isEmpty()) {
            if (hashMap.get(userString).equals(passEditText.getText().toString())) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        LogInActivity.this);
                ActivityCompat.startActivity(LogInActivity.this, intent, options.toBundle());
                LOGGED_IN = true;
            } else {
                Snackbar.make(coordinatorLayout, "Invalid", Snackbar.LENGTH_SHORT).show();
            }

        }else{
            Snackbar.make(coordinatorLayout, "User does not exist", Snackbar.LENGTH_SHORT).show();
        }
    }
    public void verifyUser(String username, String password){

    }
}
