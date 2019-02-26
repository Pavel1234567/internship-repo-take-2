package com.andersen.internship.testproject.routers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.andersen.internship.testproject.MyIntentService;

import java.lang.ref.WeakReference;

import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;

public class ServiceRouter {

    private WeakReference<Context> weakReference;
    private Intent intent;


    public ServiceRouter(Context context) {
        weakReference = new WeakReference<>(context);
        intent = new Intent(context, MyIntentService.class);
    }

    public void startService(int size){
        Context context = weakReference.get();

        intent.putExtra(SIZE, size);
        context.startService(intent);
    }

    public void stopService(){
        weakReference.get().stopService(intent);
    }
}