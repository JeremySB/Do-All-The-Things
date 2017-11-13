package com.jeremybost.doallthethings;

import com.jeremybost.doallthethings.models.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        for(int i = 0; i < 25; i++)
            items.add(new TodoItem("Test name", new Date()));
    }

    public List<TodoItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(TodoItem item) {
        items.add(item);
    }
}
