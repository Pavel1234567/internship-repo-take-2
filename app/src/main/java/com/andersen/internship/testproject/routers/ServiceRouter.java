package com.andersen.internship.testproject.routers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.andersen.internship.testproject.services.ForegroundService;
import com.andersen.internship.testproject.services.MyIntentService;

import java.lang.ref.WeakReference;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;

public class ServiceRouter {

    private WeakReference<Context> weakReferenceToContext;
    private Intent intentForIntentService;

    public ServiceRouter(Context context) {
        weakReferenceToContext = new WeakReference<>(context);
        intentForIntentService = new Intent(context, MyIntentService.class);
    }

    public void startIntentService(int size){
        intentForIntentService.putExtra(SIZE, size);
        weakReferenceToContext.get().startService(intentForIntentService);
    }

    public void stopIntentService(){
        weakReferenceToContext.get().stopService(intentForIntentService);
    }

    public void startFregroundService(){
        Context context = weakReferenceToContext.get();
        Intent intent = new Intent(context, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else {
            context.startService(intent);

        }
    }
}