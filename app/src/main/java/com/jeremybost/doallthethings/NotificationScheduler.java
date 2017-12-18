package com.jeremybost.doallthethings;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.jeremybost.doallthethings.models.TodoItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeremy on 12/18/2017.
 */

public class NotificationScheduler {
    Context context;
    SimpleDateFormat sdf;
    public NotificationScheduler() {
        this.context = App.get().getApplicationContext();
        sdf = new SimpleDateFormat("h:mm a");
    }

    public void scheduleNotification(TodoItem item) {
        if(!item.shouldNotify()) return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_01")
                .setSmallIcon(R.drawable.ic_list_black_24dp)
                .setContentTitle("Do All The Things!")
                .setContentText(item.getName() + " due at " + sdf.format(item.getDueDate()) + ".");

        Notification notification = builder.build();

        long notifyInMillis = item.getDueDate().getTime() - TimeUnit.MINUTES.toMillis(item.getReminder());

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, item.getNotificationCode());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.getNotificationCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notifyInMillis, pendingIntent);
    }

    public void scheduleNotifications(List<TodoItem> items) {
        items.forEach(this::scheduleNotification);
    }
}
