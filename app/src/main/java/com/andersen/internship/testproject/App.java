package com.andersen.internship.testproject;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static Context getContext() {
        return context;
    }

    private static Context context = getContext();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
