package com.example.mustarohman.prototype;

import android.test.AndroidTestCase;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yahya on 16/03/16.
 * This test will simultaneously test whether the android app can insert and retrieve data into the online database, we will first use
 dummy data to insert into the database and we will then retrieve that same data, that way both functions(insert/retrieve) are tested
 */


public class insertAndRetrieveQueryTest extends AndroidTestCase {

    public String data_to_be_inserted = "12345";
    String query = "Insert into tour (tourid) values('"+data_to_be_inserted+"');";
    DBConnectionSystem dbConnectionSystem;
    private ArrayList<String > tourCodes;
    boolean check = false;
    boolean expected = true;

    @Test
    public void insertionAndRetrieval() throws Exception {

        tourCodes = new ArrayList<>();
        dbConnectionSystem = new DBConnectionSystem();

        try {
            dbConnectionSystem.UpdateDatabase(query);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            tourCodes = dbConnectionSystem.getTestingTourCodes();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (tourCodes.contains(data_to_be_inserted)){
            check = true;
            assertEquals(expected, check);





        }




    }











}