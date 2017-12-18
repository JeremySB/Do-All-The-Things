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

    private int notificationCodeCounter = 0;

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

    public List<TodoItem> getItems() {
        return getItems(false);
    }

    public List<TodoItem> getItems(boolean completedItems) {
        return items.stream().filter(todoItem -> todoItem.getCompleted() == completedItems).collect(Collectors.toList());
    }

    public void addItem(TodoItem item) {
        addItem(item, true);
    }

    public void addItem(TodoItem item, boolean sortAndSave) {
        if(item == null) return;

        items.add(item);
        item.setNotificationCode(++notificationCodeCounter);

        if(!sortAndSave) return;

        items.sort(Comparator.comparing(TodoItem::getDueDate));

        saveToFile();
        if(listener != null) listener.OnTodoItemsChanged();

        generateNotifications();
    }

//    public boolean removeItem(TodoItem item) {
//        for (int i = 0; i < items.size(); i++) {
//            if(items.get(i).equals(item)) {
//                items.remove(i);
//                saveToFile();
//                if(listener != null) listener.OnTodoItemsChanged();
//                return true;
//            }
//        }
//
//        return false;
//    }

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

    public void generateNotifications() {
        new NotificationScheduler().scheduleNotifications(items);
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
                    addItem(new TodoItem("Filler item " + (i+1) + " with location", new Date(), lat, lon, 60), false);
                }
            }

            items.sort(Comparator.comparing(TodoItem::getDueDate));

            notificationCodeCounter = items.stream().mapToInt(TodoItem::getNotificationCode).max().orElse(0);
            generateNotifications();

            if(listener != null) listener.OnTodoItemsChanged();
        }
    }
}
