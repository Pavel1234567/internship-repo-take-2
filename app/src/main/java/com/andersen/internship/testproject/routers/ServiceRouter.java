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

    boolean bound = false;
    ServiceConnection sConn;
    MyIntentService myService;

    public ServiceRouter(Context context) {
        weakReference = new WeakReference<>(context);
        intent = new Intent(context, MyIntentService.class);
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                myService = ((MyIntentService.ServiceBinding) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
    }

    public void startService(int size){
        Context context = weakReference.get();

        intent.putExtra(SIZE, size);
        context.bindService(intent, sConn, 0);
        context.startService(intent);

        //если вместо 2-х последних строчек написать
        // context.bindService(intent, sConn, BIND_AUTO_CREATE);
        //то код сервис, работать не будет, хотя при последующих нажатиях срабатывает onDestroy()
        //короче, помоги мне со всем этим, пожалуйста
    }

    public void stopService(){
        if (!bound) return;
        Context context = weakReference.get();
        myService.setStopped(true);
        context.unbindService(sConn);
        bound = false;
//        Intent intent = new Intent(context, MyIntentService.class);
//        intent.setAction("STOP");
//        context.stopService(intent);
    }
}
