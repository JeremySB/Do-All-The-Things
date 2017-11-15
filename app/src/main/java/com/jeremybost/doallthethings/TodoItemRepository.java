package com.jeremybost.doallthethings;

import android.location.Location;

import com.jeremybost.doallthethings.models.TodoItem;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Jeremy on 11/12/2017.
 */

public class TodoItemRepository {
    private List<TodoItem> items;

    private static final TodoItemRepository ourInstance = new TodoItemRepository();

    public static TodoItemRepository getInstance() {
        return ourInstance;
    }

    private TodoItemRepository() {
        items = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            Location loc = new Location("");
            Random r = new Random();
            double lat = 40 + (43 - 40) * r.nextDouble();
            double lon = 0-(79 + (79 - 81) * r.nextDouble());
            loc.setLatitude(lat);
            loc.setLongitude(lon);
            items.add(new TodoItem("Test name", new Date(), loc));
        }
        for(int i = 0; i < 10; i++)
            items.add(new TodoItem("Test name", new Date()));
    }

    public List<TodoItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(TodoItem item) {
        items.add(item);
    }

    public void save() {
        //FileOutputStream fileOut getC
    }
}
