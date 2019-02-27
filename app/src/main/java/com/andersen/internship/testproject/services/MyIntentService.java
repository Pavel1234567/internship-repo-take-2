package com.andersen.internship.testproject.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.NotificationManagerHelper;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.presenters.MultithreadingPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;


public class MyIntentService extends IntentService {

    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";
    public static final String RECEIVED_TYPE = "RECEIVED_TYPE";
    public static final String MESSAGE = "MESSAGE";
    public static final int DATA = 1;

    private boolean isLoaded = false;
    private String result;

    public MyIntentService() {
        super("myname");
    }

    @Override
    protected void onHandleIntent( Intent intent) {

        int size = intent.getIntExtra(SIZE, 0);
        List<Double> list = DummyServer.getDummyServer().getDummyData(size);
        result = MultithreadingPresenter.getPresenter().handleData(list);

        isLoaded = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("myLogs", "onDestroy");

        if (isLoaded){
            Intent intentGiveBack = new Intent(BROADCAST_ACTION);
            intentGiveBack.putExtra(RECEIVED_TYPE, DATA);
            intentGiveBack.putExtra(MESSAGE, result);
            sendBroadcast(intentGiveBack);
        }
    }
}