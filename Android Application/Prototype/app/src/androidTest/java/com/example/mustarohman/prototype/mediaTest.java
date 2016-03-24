package com.example.mustarohman.prototype;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.example.mustarohman.prototype.Backend.DataCaching;
import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.Frontend.MainActivity;

import java.util.ArrayList;

/**
 * Created by Yahya on 24/03/16.
 */

/*
 In this test I will test whether media is retrievable through calling the query to return the media online
 and store it in an arrayList, if the arrayList contains a bitmap Image then we can conclude media
 is retrievable


  */



public class mediaTest extends AndroidTestCase {

    private ArrayList<Media> mediaArrayList;
    boolean expected= true;
    boolean actual = false;






    public void testMedia() throws Exception{



        DataCaching dataCaching = new DataCaching(this.getContext());
        ArrayList<TourLocation> tourLocations = null;

        tourLocations = dataCaching.readFromInternalStorage(MainActivity.PACKAGE + ".tourLocations");
        TourLocation currentTourLocation = null;
        for (TourLocation tourLocation: tourLocations){
         {
             currentTourLocation = tourLocation;
            }
        }

        mediaArrayList = currentTourLocation.getMediaList();

        Bitmap bitmap=null;

        for (Media media : mediaArrayList) {
            if (media.getDatatype() == Media.DataType.IMAGE) {
                 bitmap = media.returnBitmap();


            }}

        if (bitmap!=null) {
          actual= true;
        }

        assertEquals(expected,actual);









    }

}


