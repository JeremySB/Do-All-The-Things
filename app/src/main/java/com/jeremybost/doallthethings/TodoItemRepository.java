package com.jeremybost.doallthethings;

import android.content.Context;

import com.jeremybost.doallthethings.models.TodoItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jeremy on 11/12/2017.
 */

public class TodoItemRepository {
    private List<TodoItem> items;

    private static final TodoItemRepository ourInstance = new TodoItemRepository();

    private static final String FILE_NAME = "todo-items";

    public static TodoItemRepository getInstance() {
        return ourInstance;
    }

    private TodoItemRepository() {
        loadFromFile();

        if(!items.isEmpty()) return;
        for(int i = 0; i < 25; i++)
            items.add(new TodoItem("Test nameys", new Date()));
        saveToFile();
    }

    public List<TodoItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(TodoItem item) {
        items.add(item);
        saveToFile();
    }

    public void saveToFile() {
        FileOutputStream fileOut = null;
        ObjectOutputStream obOut = null;

        try {
            fileOut = App.get().getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            obOut = new ObjectOutputStream(fileOut);
            obOut.writeObject(items);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(obOut != null) {
                    obOut.close();
                }
                if(fileOut != null) {
                    fileOut.close();
                }
            }
            catch (Exception e ) {
                // ehh
            }
        }
    }

    public void loadFromFile() {
        FileInputStream fileIn = null;
        ObjectInputStream obIn = null;

        try {
            fileIn = App.get().getApplicationContext().openFileInput(FILE_NAME);
            obIn = new ObjectInputStream(fileIn);
            items = (List<TodoItem>)obIn.readObject();
        }
        catch (Exception e) {
            items = new ArrayList<>();
            e.printStackTrace();
        }
        finally {
            try {
                if(obIn != null) {
                    obIn.close();
                }
                if(fileIn != null) {
                    fileIn.close();
                }
            }
            catch (Exception e ) {
                // ehh
            }
        }
    }
}
