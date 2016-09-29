package com.example.mustarohman.prototype;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.DataBase.DBQueryAsyncTask;
import com.example.mustarohman.prototype.Frontend.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Yahya on 09/03/16.
 * This test is used to check when entering the tour code in the main activity, it remains consistent with the ones created on the online
 database
 * using espresso API which allows simulation of application in real time

 */





@RunWith(AndroidJUnit4.class)
@LargeTest
public class TourCodeTest {

    //database connection
    DBConnectionSystem dbConnectionSystem;


    //defining the activity(class) we will be testing
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<MainActivity>(MainActivity.class);


    //Test
    @Test
    public void setCodeToBeTested () throws Exception{

        //The tour code that we will be testing which exists on the database
        String codeToBeTested= "TOR124";
        String query = "Select * from tour where tourid = '" + codeToBeTested + "';";
        HashMap<String, String> tourIds = null;
        Log.d("checkTourCode", "Retrieving tourIds from database...");

        tourIds = DBQueryAsyncTask.retrieveTours(query);



        //simulated event of typing in the code
        // Type text and then press the button.
        onView(ViewMatchers.withId(R.id.code_edit)).perform(typeText(codeToBeTested), closeSoftKeyboard());


        //checks if the tour code exists in the database
        if (tourIds.containsKey(codeToBeTested)){

            //then we can verify that the same code exists on the text view
            onView(withId(R.id.code_edit)).check(matches(withText(codeToBeTested)));
        }}
}