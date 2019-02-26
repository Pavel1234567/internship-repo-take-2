package com.andersen.internship.testproject.mvp.reddit;

import android.arch.lifecycle.LifecycleObserver;

import com.andersen.internship.testproject.IMAGES_TYPES;

public interface Presenter extends LifecycleObserver {


    void loadImages(IMAGES_TYPES imagesType);
}
