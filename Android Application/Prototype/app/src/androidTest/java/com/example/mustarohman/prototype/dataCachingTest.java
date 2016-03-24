package com.example.mustarohman.prototype;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.Frontend.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yahya on 16/03/16.
 * I will test the dataCaching class in backend/objects/DataCaching, the purpose of this class is to save data to an internal
 storage in the form of a file and arrayList, I will test this by using the saveInternalStorageMethod() and then test whether the data exists
 in the arrayList instantiated then I will use the readFrominternalStorage() which returns an arrayList of the data in the file to
 test whether the file contained the data which was first saved to it
 */



public class dataCachingTest {


    DataCaching dataCaching;
    Context context;
    ArrayList<TourLocation> dummyList = new ArrayList<TourLocation>();
    ArrayList<TourLocation> outputDummyList = new ArrayList<TourLocation>();
    String fileName= "test.text";
    File file;
    String dummyName ="espresso";
    boolean actual = false;
    boolean expected = true;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);






    @Test
    public void testDataCaching() throws Exception {
        context = mActivityRule.getActivity().getBaseContext();
        //dummy test file
        file = new File(fileName);

        dummyList.add(new TourLocation(1234, dummyName, 22, 44));
        dataCaching = new DataCaching(context);

        //saving to the internal storage
        dataCaching.saveDataToInternalStorage(fileName, dummyList);


        outputDummyList = dataCaching.readFromInternalStorage(fileName);

        if (outputDummyList.get(0).getId() == 1234  && outputDummyList.get(0).getLatitude()==22 &&
                outputDummyList.get(0).getLongitude()== 44

        ) {

            actual = true;
        }

        Assert.assertEquals(expected, actual);


    }
}


