package com.andersen.internship.testproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.andersen.internship.testproject.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManagerHelper {

    private static final int ID_PROGRESS_NOTIFICATION = 1;
    private static final int ID_FOR_NOTIFICATION_WITH_MESSAGE = 2;
    private static final int MAX = 100;
    public static final String OPEN_FOREGROUND_SERVICE_FRAGMENT = "OPEN_FOREGROUND_SERVICE_FRAGMENT";

    //android 8
    private final static String CHANNEL_ID = "1";
    private final static String CHANNEL_NAME = "CHANNEL_";
    private final static String CHANNEL_DESCRIPTION = "description";


    private NotificationManager manager;
    private Context context = App.getContext();
    private NotificationCompat.Builder progressNotificationBuilder;

    private Intent intent;
    private PendingIntent resultPendingIntent;


    @RequiresApi(Build.VERSION_CODES.O)
    public NotificationChannel createMainNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        );

        channel.setDescription(CHANNEL_DESCRIPTION);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        return channel;
    }


    public NotificationManagerHelper() {

        intent = new Intent(context, MainActivity.class);
        intent.setAction(OPEN_FOREGROUND_SERVICE_FRAGMENT);
        resultPendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = createMainNotificationChannel();
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }else {
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        }

        progressNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_arrow_downward)
                .setContentTitle("Идёт загрузка")
                .setProgress(MAX, 0, false)
                .setContentIntent(resultPendingIntent);
    }

    public void showMessage(String text) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_arrow_downward)
                .setContentTitle(text)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);


        manager.notify(ID_FOR_NOTIFICATION_WITH_MESSAGE, builder.build());
    }

    public void showProgress(int progress) {

        progressNotificationBuilder
                .setProgress(MAX, progress, false)
                .setContentText(progress + " of " + MAX)
                .setOngoing(true);

        manager.notify(ID_PROGRESS_NOTIFICATION, progressNotificationBuilder.build());
    }

    public Notification getProgressBarForForegroundService() {
        return progressNotificationBuilder.build();
    }
}