package com.andersen.internship.testproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.andersen.internship.testproject.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManagerHelper {

    public static final int NOTIFICATION_ID = 1;
    public static final int MAX = 100;

    private NotificationManager manager;
    private Context context = App.getContext();
    private NotificationCompat.Builder progressNotificationBuilder;

    private Intent intent = new Intent(context, MainActivity.class);
    private PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    public NotificationManagerHelper() {

        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        progressNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_tools)
                .setContentTitle("Идёт загрузка")
                .setProgress(MAX, 0, false)
                .setContentIntent(resultPendingIntent);
    }

    public void showNotification(String text) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_tools)
                .setContentTitle(text)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                ;

        manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void showProgress(int progress) {

        progressNotificationBuilder
                .setProgress(MAX, progress, false)
                .setContentText(progress + " of " + MAX)
                .setOngoing(true);

        manager.notify(NOTIFICATION_ID, progressNotificationBuilder.build());
    }

    public Notification getProgressBarForForegroundService() {
        return progressNotificationBuilder.build();
    }

    public void closeAllNotifications(){
        manager.cancelAll();
    }
}