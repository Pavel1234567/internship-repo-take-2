package com.andersen.internship.testproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Model;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;

import java.util.List;

public class MyAsyncLoader extends AsyncTaskLoader<String> {

    public static final int LOADER_ID = 1;
    public static final String SIZE = "SIZE";

    private Bundle bundle;
    private Model model;



    public MyAsyncLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
        model = DummyServer.getDummyServer();
    }

    @Override
    public String loadInBackground() {
        int size = bundle.getInt(SIZE);
        List<Double> listResult = model.getDummyData(size);
        return Presenter.handleData(listResult);
    }
}