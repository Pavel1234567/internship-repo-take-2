package com.andersen.internship.testproject.mvp.multithreading;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public interface Model {
    List<Double> getDummyData(int dataSize);

    PublishSubject<Integer> observeProgress();

}
