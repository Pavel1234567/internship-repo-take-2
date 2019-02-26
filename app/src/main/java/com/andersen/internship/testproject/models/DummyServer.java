package com.andersen.internship.testproject.models;

import android.util.Log;

import com.andersen.internship.testproject.mvp.multithreading.Model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class DummyServer implements Model {

    private static DummyServer dummyServer;

    private DummyServer(){}

    private PublishSubject<Integer> subject = PublishSubject.create();

    public static synchronized DummyServer getDummyServer(){
        if (dummyServer == null){
            dummyServer = new DummyServer();
        }
        return dummyServer;
    }

    @Override
    public List<Double> getDummyData(int dataSize) {
        List<Double> dummyData = new ArrayList<>();

        for (int i = 0; i < dataSize; i++){
            dummyData.add(i + 0.0);
            try {
                Thread.sleep(200);
                double percentage =  (double) i / (dataSize-1) * 100.0;
                subject.onNext((int) percentage);

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;

            }
        }
        subject.onComplete();
        subject = PublishSubject.create();
        return dummyData;

    }

    @Override
    public PublishSubject<Integer> observeProgress() {
        return subject;
    }
}
