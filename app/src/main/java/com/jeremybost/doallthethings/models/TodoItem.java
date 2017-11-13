package com.jeremybost.doallthethings.models;

import java.util.Date;

/**
 * Created by Jeremy on 11/12/2017.
 */

public class TodoItem {
    private String name;
    private Date dueDate;

    public TodoItem() {
        name = "";
        dueDate = null;
    }

    public TodoItem(String name, Date dueDate) {
        this.name = name;
        this.dueDate = dueDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
}
