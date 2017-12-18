package com.jeremybost.doallthethings.models;

import android.location.Location;

import com.jeremybost.doallthethings.TodoItemRepository;

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
    private boolean completed = false;
    private boolean notified = false;


    private int notificationCode;


    private int reminder = 60;

    public TodoItem() {
        name = "";
        dueDate = null;
    }

    public TodoItem(String name, Date dueDate) {
        this(name, dueDate, null, null, 60);
    }

    public TodoItem(String name, Date dueDate, Double latitude, Double longitude, int reminder) {
        this.name = name;
        this.dueDate = dueDate;
        setLocation(latitude, longitude);
        this.reminder = reminder;
    }

    public boolean shouldNotify() {
        boolean result = !notified && !completed && dueDate.getTime() > System.currentTimeMillis();
        notified = true;
        return result;
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

    public boolean getCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
        TodoItemRepository.getInstance().generateNotifications();
    }

    public int getReminder() {
        return reminder;
    }
    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public int getNotificationCode() {
        return notificationCode;
    }
    public void setNotificationCode(int notificationCode) {
        this.notificationCode = notificationCode;
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
        if(!(completed == other.getCompleted())) {
            return false;
        }
        if(hasLocation && !latitude.equals(other.getLatitude())) {
            return false;
        }
        if(hasLocation && !longitude.equals(other.getLongitude())) {
            return false;
        }

        return true;
    }
}
