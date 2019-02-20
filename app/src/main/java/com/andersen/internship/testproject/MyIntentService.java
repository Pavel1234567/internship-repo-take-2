package com.andersen.internship.testproject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.andersen.internship.testproject.activities.MainActivity;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;

import java.util.List;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;


public class MyIntentService extends IntentService {

    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";

    public static final String RECEIVED_TYPE = "RECEIVED_TYPE";
    public static final String MESSAGE = "MESSAGE";

    public static final int DATA = 1;
    public static final int PROGRESS = 2;

    public MyIntentService() {
        super("myname");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        //TODO: сделать константу

        int size = intent.getIntExtra(SIZE, 0);
        List<Double> list = DummyServer.getDummyServer().getDummyData(size);
        String rez = Presenter.handleData(list);

        //TODO: добавить прогресс
        Intent intent1 = new Intent(BROADCAST_ACTION);
        intent1.putExtra(RECEIVED_TYPE, DATA);
        intent1.putExtra(MESSAGE, rez);
        sendBroadcast(intent1);

    }
}