package com.andersen.internship.testproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class HideScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        scrolledDistance = 0;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(Math.abs(scrolledDistance) < Math.abs(HIDE_THRESHOLD)) {
            scrolledDistance += dy;
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
            }else if (scrolledDistance < - HIDE_THRESHOLD && !controlsVisible){
                onShow();
                controlsVisible = true;
            }
        }
    }

    public abstract void onHide();
    public abstract void onShow();
}