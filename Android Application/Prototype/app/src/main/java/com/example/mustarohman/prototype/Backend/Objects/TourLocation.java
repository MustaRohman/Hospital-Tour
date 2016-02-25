package com.example.mustarohman.prototype.Backend.Objects;

import java.io.Serializable;

/**
 * Created by yezenalnafei on 25/02/2016.
 */
public class TourLocation implements Serializable{

    private int locationId;
    private String name;
    private float latitude;
    private float longitude;

    public TourLocation(int locationId, String name, float latitude, float longitude){

        this.locationId = locationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return locationId;
    }

    public void setId(int id) {
        this.locationId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
