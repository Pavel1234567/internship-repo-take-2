package com.andersen.internship.testproject.mvp.multithreading;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.util.List;

public interface Presenter {

    static String handleData(List<Double> list){

        StringBuilder dataS = new StringBuilder();
        for (double d: list){
            dataS.append(d+"\n");
        }
        return dataS.toString();
    }

    void load(String loadType, String arg);
    void stopLoading();
    void onDetach();
    void onAttach(View view);
    void onDestroy();

    public interface PresenterWithAsyncTool {
        void setData(String string);
    }
}