package com.andersen.internship.testproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.andersen.internship.testproject.R;

import static com.andersen.internship.testproject.NotificationManagerHelper.OPEN_FOREGROUND_SERVICE_FRAGMENT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if (getResources().getBoolean(R.bool.isTablet)){
            intent = new Intent(this, TabletActivity.class);
        }
        else {
            intent = new Intent(this, MobileActivity.class);
        }

        //TODO: сделать нормальный коммент
        //если активити была открыта через status bar
        intent.setAction(getIntent().getAction());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}