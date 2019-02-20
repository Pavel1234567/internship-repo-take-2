package com.andersen.internship.testproject.mvp.multithreading;

import android.support.v4.app.LoaderManager;

public interface View {

    void showDownloadStatus(String message);

    void setData(String s);
    void setProgress(int progress);

    public interface ViewWithAsyncLoader extends LoaderManager.LoaderCallbacks<String>, View{

        void runLoader(int size);
    }
}
