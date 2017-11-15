package com.jeremybost.doallthethings.models;

import android.location.Location;

import java.util.Date;

/**
 * Created by Jeremy on 11/12/2017.
 */

public class TodoItem {
    private String name;
    private Date dueDate;
    private Location location;

    public TodoItem() {
        name = "";
        dueDate = null;
    }

    public TodoItem(String name, Date dueDate) {
        this(name, dueDate, null);
    }

    public TodoItem(String name, Date dueDate, Location location) {
        this.name = name;
        this.dueDate = dueDate;
        this.location = location;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }


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
