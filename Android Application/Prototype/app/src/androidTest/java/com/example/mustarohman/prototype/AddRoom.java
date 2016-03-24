package com.example.mustarohman.prototype;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Frontend.PostLoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by Yahya on 10/03/16.
 * test used to check whether the application adds a tour in the postlogin activity
 * using espresso API which allows simulation of application in real time
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddRoom {


    private static final String newRoom = "testing123";
    private ArrayList<String> tourNames = null;
    private DBConnectionSystem dbConnectionSystem;
    private boolean test= false;


    @Rule
    public ActivityTestRule<PostLoginActivity> main = new ActivityTestRule<PostLoginActivity>(PostLoginActivity.class);


    /**
     * @throws Exception
     */
    @Test
    public void addTourTest() throws Exception{


        //simulation of gui, on view retrieves the view, and functions called in the performClick() method
        //simulates an action e.g. pressing, typing
        onView(ViewMatchers.withId(R.id.add_room_button)).perform(click());
        onView(withId(R.id.AddLocationName)).perform(typeText(newRoom),closeSoftKeyboard());
        onView(withId(R.id.retrieve_location_button)).perform(click());

        //after adding the tour an update query is sent to the database, this is used to retrieve the same data
        //that was just sent
        dbConnectionSystem = new DBConnectionSystem();
        try {
            tourNames = dbConnectionSystem.getTourName();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tourNames.contains(newRoom)){
            test = true;


            assertThat(Boolean.TRUE , is(test));
        }





















    }



}