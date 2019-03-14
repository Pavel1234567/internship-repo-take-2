package com.andersen.internship.testproject.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.routers.ServiceRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andersen.internship.testproject.services.ForegroundService.BROADCAST_FOR_FOREGROUND_SERVICE;
import static com.andersen.internship.testproject.services.ForegroundService.CURRENT_PROGRESS;
import static com.andersen.internship.testproject.services.ForegroundService.DOWNLOAD_FINISHED;

public class ForegroundServiceFragment extends AbstractFragment {

    @BindView(R.id.buttonStart)
    Button buttonStart;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ServiceRouter serviceRouter;
    private BroadcastReceiver broadcastReceiver;

    private static final int PROGRESS_NOT_RECEIVED = -1;

    public ForegroundServiceFragment(){
        setIdTitle(R.string.service);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_foreground_service, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        buttonStart.setOnClickListener(v -> startForegroundService());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int i = intent.getIntExtra(CURRENT_PROGRESS, PROGRESS_NOT_RECEIVED);

                if (i != PROGRESS_NOT_RECEIVED){
                    progressBar.setProgress(i);
                }else {
                    String messageFromService = intent.getStringExtra(DOWNLOAD_FINISHED);
                    Toast.makeText(getActivity(), messageFromService, Toast.LENGTH_SHORT).show();
                }
            }
        };
        serviceRouter = new ServiceRouter(getActivity());
    }

    @Override
    public void onResume() {

        super.onResume();
        IntentFilter intFilt = new IntentFilter(BROADCAST_FOR_FOREGROUND_SERVICE);
        getActivity().registerReceiver(broadcastReceiver, intFilt);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void startForegroundService(){
        serviceRouter.startFregroundService();
    }
}