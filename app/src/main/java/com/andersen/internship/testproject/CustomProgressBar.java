package com.andersen.internship.testproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class CustomProgressBar extends ProgressBar {

    private int currentColor;

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

        if (setColorConditional(progress, 0, 10, YELLOW)){
            setColor(YELLOW);

        } else if(setColorConditional(progress, 10, 30, RED)){
            setColor(Color.RED);
            
        }else if(setColorConditional(progress, 30, 70, BLUE)){
            setColor(Color.BLUE);

        }else if(setColorConditional(progress, 70, 100, GREEN)){
            setColor(Color.GREEN);

        }

    }

    private void setColor(int color){
        Drawable progressDrawable = getProgressDrawable().mutate();
        progressDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        setProgressDrawable(progressDrawable);

        currentColor = color;

    }

    private boolean setColorConditional(int progress, int start, int finish, int color){
        return progress >= start && progress < finish && color != currentColor;
    }
}
