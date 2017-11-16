package com.jeremybost.doallthethings.models;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jeremy on 11/12/2017.
 */

public class TodoItem implements Serializable {
    private String name;
    private Date dueDate;
    private Double latitude, longitude;
    private boolean hasLocation = false;

    public TodoItem() {
        name = "";
        dueDate = null;
    }

    public TodoItem(String name, Date dueDate) {
        this(name, dueDate, null, null);
    }

    public TodoItem(String name, Date dueDate, Double latitude, Double longitude) {
        this.name = name;
        this.dueDate = dueDate;
        setLocation(latitude, longitude);
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if(latitude != null && longitude != null) {
            hasLocation = true;
        } else {
            hasLocation = false;
        }
    }

    public boolean hasLocation() { return hasLocation; }

    public String toString() {
        return name + ", " + dueDate.toString();
    }

    public boolean equals(TodoItem other) {
        if(!name.equals(other.getName())) {
            return false;
        }
        if(!dueDate.equals(other.getDueDate())) {
            return false;
        }

        return true;
    }
}
