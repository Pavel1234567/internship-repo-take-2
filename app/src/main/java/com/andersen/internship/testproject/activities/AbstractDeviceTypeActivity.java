package com.andersen.internship.testproject.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.andersen.internship.testproject.routers.FragmentRouter;
import com.andersen.internship.testproject.R;

import butterknife.BindView;

public abstract class AbstractDeviceTypeActivity extends AppCompatActivity {

    FragmentRouter router;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router = new FragmentRouter(this);
        setTitle(title);
    }

    void initToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
    }
}