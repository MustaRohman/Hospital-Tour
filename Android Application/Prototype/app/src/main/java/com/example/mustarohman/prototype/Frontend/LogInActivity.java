package com.example.mustarohman.prototype.Frontend;

import android.content.Intent;
import android.os.Bundle;
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
    public static boolean LOGGED_IN;
    public static String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
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
                startActivity(intent);
                LOGGED_IN = true;
            } else {
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "User does not exsits", Toast.LENGTH_SHORT).show();
        }
    }
    public void verifyUser(String username, String password){

    }
}
