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

        onShow();
        scrolledDistance = 0;
        controlsVisible = true;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(scrolledDistance < HIDE_THRESHOLD) {
            scrolledDistance += Math.abs(dy);
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
            }
        }
    }

    public abstract void onHide();
    public abstract void onShow();
}