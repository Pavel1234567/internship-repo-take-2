package com.andersen.internship.testproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Model;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.View;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MyAsyncLoader extends AsyncTaskLoader<String> {

    public static final int LOADER_ID = 1;
    public static final String SIZE = "SIZE";

    private Bundle bundle;
    private Model model;
    private Disposable progressObserverDisposable;



    public MyAsyncLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
        model = DummyServer.getDummyServer();
        progressObserverDisposable = model.observeProgress()
                .subscribe();
    }

    @Override
    public String loadInBackground() {
        int size = bundle.getInt(SIZE);
        List<Double> rez = model.getDummyData(size);
        return Presenter.handleData(rez);
    }
}