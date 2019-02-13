package com.andersen.internship.testproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

        if (progress <= 10){
            setColor(Color.YELLOW);

        } else if(progress <= 30){
            setColor(Color.RED);


        }else if(progress <= 70){
            setColor(Color.BLUE);


        }else if(progress <= 100){
            setColor(Color.GREEN);

        }

    }

    private void setColor(int color){
        Drawable progressDrawable = getProgressDrawable().mutate();
        progressDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        setProgressDrawable(progressDrawable);

    }
}
