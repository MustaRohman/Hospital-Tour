package com.example.mustarohman.prototype.Backend.Objects;

import java.util.ArrayList;

/**
 * Created by yezenalnafei on 25/02/2016.
 */
public class Tour {

    private String tourId;
    private String name;
    private String date;
    private ArrayList<TourLocation> locations;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<TourLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<TourLocation> locations) {
        this.locations = locations;
    }
}
