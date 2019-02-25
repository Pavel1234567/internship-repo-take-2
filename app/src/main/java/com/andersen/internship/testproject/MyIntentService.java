package com.andersen.internship.testproject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.presenters.MultithreadingPresenter;

import java.util.List;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;


public class MyIntentService extends IntentService {

    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";

    public static final String RECEIVED_TYPE = "RECEIVED_TYPE";
    public static final String MESSAGE = "MESSAGE";

    public static final int DATA = 1;

    private ServiceBinding binder = new ServiceBinding();

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    private boolean isStopped = false;

    public MyIntentService() {
        super("myname");
    }

    @Override
    protected void onHandleIntent( Intent intent) {

        int size = intent.getIntExtra(SIZE, 0);
        List<Double> list = DummyServer.getDummyServer().getDummyData(size);
        String result = MultithreadingPresenter.getPresenter().handleData(list);
        if (isStopped) return;
        Intent intentGiveBack = new Intent(BROADCAST_ACTION);
        intentGiveBack.putExtra(RECEIVED_TYPE, DATA);
        intentGiveBack.putExtra(MESSAGE, result);
        sendBroadcast(intentGiveBack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("myLogs", "onDestroy");
    }

    public IBinder onBind(Intent arg0) {
        return binder;
    }

    public class ServiceBinding extends Binder {
        public MyIntentService getService() {
            return MyIntentService.this;
        }
    }
}