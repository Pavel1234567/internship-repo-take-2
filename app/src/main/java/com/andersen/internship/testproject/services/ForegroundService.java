package com.andersen.internship.testproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.andersen.internship.testproject.NotificationManagerHelper;

public class ForegroundService extends IntentService {

    public static final String BROADCAST_FOR_FOREGROUND_SERVICE = "BROADCAST_FOR_FOREGROUND_SERVICE";
    public static final String CURRENT_PROGRESS = "CURRENT_PROGRESS";
    public static final String DOWNLOAD_FINISHED = "DOWNLOAD_FINISHED";
    private static final int FOREGROUND_ID = 1;

    private NotificationManagerHelper notificationManagerHelper = new NotificationManagerHelper();

    public ForegroundService() {
        super("ForegroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        startForeground(FOREGROUND_ID, notificationManagerHelper.getProgressBarForForegroundService());
        Intent progressReport = new Intent(BROADCAST_FOR_FOREGROUND_SERVICE);
        for (int i = 0; i < 100; i++){

            try {
                Thread.sleep(200);

                progressReport.putExtra(CURRENT_PROGRESS, i);
                sendBroadcast(progressReport);
                notificationManagerHelper.showProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        progressReport.putExtra(DOWNLOAD_FINISHED, "finished");
        notificationManagerHelper.showNotification("finished");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManagerHelper.closeAllNotifications();
    }
}
