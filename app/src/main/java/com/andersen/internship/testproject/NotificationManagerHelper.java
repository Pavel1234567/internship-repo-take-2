package com.andersen.internship.testproject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManagerHelper {

    private NotificationManager manager;
    private Context context = App.getContext();
    public static final int NOTIFICATION_ID = 1;


    public NotificationManagerHelper() {
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void showNotification(String text){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_tools)
                .setContentTitle(text);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
