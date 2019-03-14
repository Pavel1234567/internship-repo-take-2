package com.andersen.internship.testproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.andersen.internship.testproject.routers.FragmentRouter;
import com.andersen.internship.testproject.R;

import butterknife.BindView;

import static com.andersen.internship.testproject.NotificationManagerHelper.OPEN_FOREGROUND_SERVICE_FRAGMENT;

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

    @Override
    protected void onStart() {
        super.onStart();
        String action = getIntent().getAction();

        if (action != null && action.equals(OPEN_FOREGROUND_SERVICE_FRAGMENT)){
            router.openForegroundServiceFragment();
        }
    }

    void initToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
    }
}