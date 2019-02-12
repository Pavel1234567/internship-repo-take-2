package com.andersen.internship.testproject.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.andersen.internship.testproject.FragmentRouter;
import com.andersen.internship.testproject.R;

import butterknife.BindView;

public abstract class AbstractDeviceTypeActivity extends AppCompatActivity {

    FragmentRouter router;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router = new FragmentRouter(this);
        setTitle(title);


    }
}