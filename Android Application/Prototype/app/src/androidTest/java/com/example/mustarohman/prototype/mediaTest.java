package com.example.mustarohman.prototype;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.Frontend.MainActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yahya on 24/03/16.
 *  In this test I will test whether media is retrievable through calling the query to return the media online
 and store it in an arrayList, if the arrayList contains a bitmap Image then we can conclude media
 is retrievable
 *
 */





public class mediaTest extends AndroidTestCase {

    private ArrayList<Media> mediaArrayList;
    boolean expected= true;
    boolean actual = false;






    public void testMedia() throws Exception {

        //using datacaching to retrieve media from the storage and storing it as an arrayList
        DataCaching dataCaching = new DataCaching(this.getContext());
        ArrayList<TourLocation> tourLocations = null;

        tourLocations = dataCaching.readFromInternalStorage(MainActivity.PACKAGE + ".tourLocations");
        TourLocation currentTourLocation = null;
        for (TourLocation tourLocation: tourLocations){
         {
             currentTourLocation = tourLocation;
            }
        }


        //media arraylist data
        mediaArrayList = currentTourLocation.getMediaList();

        //retrieving the media image(bitmap), video(file) if they were successfully retrieved then they will not return null,
        //this is because they were retrieved from online so if they contain data, they will not return null
        Bitmap bitmap=null;
        File file = null;

        //loop to check whether they return null
        for (Media media : mediaArrayList) {
            if (media.getDatatype() == Media.DataType.IMAGE) {
                 bitmap = media.returnBitmap();


            }
            else {

                file = media.getVidFile();
            }

        }

        if (bitmap!=null && file!= null) {
          actual= true;
        }


        assertEquals(expected,actual);








    }

}


