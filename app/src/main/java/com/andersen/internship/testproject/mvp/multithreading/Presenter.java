package com.andersen.internship.testproject.mvp.multithreading;

import java.util.List;

public interface Presenter {

    default String handleData(List<Double> list){

        StringBuilder dataS = new StringBuilder();
        for (double d: list){
            dataS.append(d+"\n");
        }
        return dataS.toString();
    }

    void load(String loadType, String arg);
    void stopLoading();
    void onStop();
    void onCreate(ViewForMT viewForMT);
}