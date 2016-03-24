package com.example.mustarohman.prototype;

import android.test.AndroidTestCase;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;

import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yahya on 16/03/16.
 */

/*
This test will simultaneously test whether the android app can insert and retrieve data into the online database, we will first use
dummy data to insert into the database and we will then retrieve that same data, that way both functions(insert/retrieve) are tested
*/
public class insertAndRetrieveQueryTest extends AndroidTestCase {

    private String data_to_be_inserted = "12345";
    private String TourIDquery = "Insert into tour (tourid) values('"+data_to_be_inserted+"');";
    private DBConnectionSystem dbConnectionSystem;
    private HashMap<String,String > tourCodes;
    boolean actual = false;
    boolean expected = true;

    @Test
    public void testName() throws Exception {

        tourCodes = new HashMap<>();
        dbConnectionSystem = new DBConnectionSystem();

        try {
            dbConnectionSystem.UpdateDatabase(TourIDquery);


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        try {
//            tourCodes = dbConnectionSystem.getTourCodes();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        if (tourCodes.containsKey(data_to_be_inserted)){
            actual = true;

        }



        assertEquals(expected, actual);





    }











}