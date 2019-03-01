package com.andersen.internship.testproject.routers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.andersen.internship.testproject.services.ForegroundService;
import com.andersen.internship.testproject.services.MyIntentService;

import java.lang.ref.WeakReference;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;

public class ServiceRouter {

    private WeakReference<Context> weakReference;
    private Intent intentForIntentService;

    public ServiceRouter(Context context) {
        weakReference = new WeakReference<>(context);
        intentForIntentService = new Intent(context, MyIntentService.class);
    }

    public void startIntentService(int size){

        Context context = weakReference.get();
        intentForIntentService.putExtra(SIZE, size);
        context.startService(intentForIntentService);
    }

    public void stopIntentService(){
        weakReference.get().stopService(intentForIntentService);
    }

    public void startFregroundService(){
        Context context = weakReference.get();
        Intent intent = new Intent(context, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else {
            context.startService(intent);

        }
    }
}