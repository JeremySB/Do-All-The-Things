package com.jeremybost.doallthethings;

import android.location.Location;
import android.content.Context;
import android.os.AsyncTask;


import com.jeremybost.doallthethings.models.TodoItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    private OnChangeListener listener;

    private TodoItemRepository() {
        items = new ArrayList<>();

        loadFromFile();
    }

    public List<TodoItem> getActiveItems() {
        return items.stream().filter(todoItem -> !todoItem.getCompleted()).collect(Collectors.toList());
    }

    public void addItem(TodoItem item) {
        if(item == null) return;

        items.add(item);
        items.sort(Comparator.comparing(TodoItem::getDueDate));

        saveToFile();
        if(listener != null) listener.OnTodoItemsChanged();
    }

    public boolean removeItem(TodoItem item) {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).equals(item)) {
                items.remove(i);
                saveToFile();
                if(listener != null) listener.OnTodoItemsChanged();
                return true;
            }
        }

        return false;
    }

    public void saveToFile() {
        new SaveAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, items);
    }

    public void loadFromFile() {
        new LoadAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public interface OnChangeListener {
        void OnTodoItemsChanged();
    }

    private class SaveAsyncTask extends AsyncTask<List<TodoItem>, Void, Void> {

        @Override
        protected Void doInBackground(List<TodoItem>... lists) {
            FileOutputStream fileOut = null;
            ObjectOutputStream obOut = null;

            List<TodoItem> toSave = lists[0];

            try {
                fileOut = App.get().getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                obOut = new ObjectOutputStream(fileOut);
                obOut.writeObject(toSave);
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
            return null;
        }
    }

    private class LoadAsyncTask extends AsyncTask<Void, Void, List<TodoItem>> {

        @Override
        protected List<TodoItem> doInBackground(Void... voids) {
            FileInputStream fileIn = null;
            ObjectInputStream obIn = null;

            List<TodoItem> result;

            try {
                fileIn = App.get().getApplicationContext().openFileInput(FILE_NAME);
                obIn = new ObjectInputStream(fileIn);
                result = (List<TodoItem>)obIn.readObject();
            }
            catch (Exception e) {
                result = new ArrayList<>();
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
            return result;
        }

        @Override
        protected void onPostExecute(List<TodoItem> todoItems) {
            items = todoItems;

            if(items.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    Random r = new Random();
                    double lat = 40 + (43 - 40) * r.nextDouble();
                    double lon = 0 - (79 + (79 - 81) * r.nextDouble());
                    items.add(new TodoItem("Test name with location", new Date(), lat, lon));
                }
            }

            if(listener != null) listener.OnTodoItemsChanged();
        }
    }
}
