package com.jeremybost.doallthethings;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 11/15/2017.
 *
 * Purpose: global access to the application context
 */

public class App extends Application {
    private static App instance;
    public static App get() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        addNotificationChannels();
        instance = this;
    }

    public void addNotificationChannels() {
        if(android.os.Build.VERSION.SDK_INT < 26)
            return;

        List<NotificationChannel> channels = new ArrayList<>();
        channels.add(new NotificationChannel("channel_01", "Reminders", NotificationManager.IMPORTANCE_DEFAULT));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannels(channels);

    }
}
