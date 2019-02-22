package com.andersen.internship.testproject.mvp.multithreading;

public interface ViewForMT {

    void showDownloadStatus(String message);
    void setText(String s);
    void setProgress(int progress);
}

