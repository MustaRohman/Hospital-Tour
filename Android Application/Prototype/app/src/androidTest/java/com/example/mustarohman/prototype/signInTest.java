package com.example.mustarohman.prototype;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Frontend.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Yahya on 09/03/16.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class signInTest  extends AndroidTestCase{

    DBConnectionSystem dbConnectionSystem;
    private static final String userName = "jimmy";
    private static final String password ="12345";
    boolean actual = false;
    boolean expected = true;




    @Rule
    public final ActivityTestRule<MainActivity> login = new ActivityTestRule<MainActivity>(MainActivity.class);






    @Test
    public void testLogin() throws Exception{

        HashMap<String, ArrayList<String >> hashMap = null;
        DBConnectionSystem query = new DBConnectionSystem();

        ArrayList<String> passwords =null;

        try {
            hashMap = query.loginQueryFetch("select * from users where username = '" + userName + "';");

            for (int i =0; i<hashMap.size();i++) {
                passwords.add(hashMap.get(i).toString());

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        if (!hashMap.isEmpty()) {

            onView(ViewMatchers.withId(R.id.sign_button)).perform(click());
            onView(withId(R.id.user_edit)).perform(typeText(userName), closeSoftKeyboard());
            onView(withId(R.id.pass_edit)).perform(typeText(password), closeSoftKeyboard());
            onView(withId(R.id.log_btn)).perform(click());
            onView(withId(R.id.user_textview)).check(matches(withText("Logged in as: " + userName)));






        }

        if (password.contains(password)){
             actual = true;

        }

        assertEquals(expected,actual);


    }






}



