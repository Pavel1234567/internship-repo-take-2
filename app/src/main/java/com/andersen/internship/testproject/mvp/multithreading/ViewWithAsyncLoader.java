package com.andersen.internship.testproject.mvp.multithreading;

import android.support.v4.app.LoaderManager;

public interface ViewWithAsyncLoader extends LoaderManager.LoaderCallbacks<String> {
    void runLoader(int size);
    void stopLoader();
}
