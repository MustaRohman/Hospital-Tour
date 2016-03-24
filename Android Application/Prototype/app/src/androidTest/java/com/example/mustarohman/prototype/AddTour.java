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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by Yahya on 10/03/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddTour {


    private static final String newRoom = "testing123";
    private ArrayList<String> tourNames = null;
    private DBConnectionSystem dbConnectionSystem;
    private boolean test= false;


    @Rule
    public ActivityTestRule<PostLoginActivity> main = new ActivityTestRule<PostLoginActivity>(PostLoginActivity.class);


    @Test
    public void addTourTest() throws Exception{



        onView(ViewMatchers.withId(R.id.add_room_button)).perform(click());
        onView(withId(R.id.AddLocationName)).perform(typeText(newRoom));
        onView(withId(R.id.retrieve_location_button)).perform(click());

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