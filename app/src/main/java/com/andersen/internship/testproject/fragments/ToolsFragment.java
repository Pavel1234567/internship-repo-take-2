package com.andersen.internship.testproject.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andersen.internship.testproject.MyAsyncLoader;
import com.andersen.internship.testproject.MyIntentService;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.presenters.MultithreadingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andersen.internship.testproject.MyAsyncLoader.LOADER_ID;
import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;
import static com.andersen.internship.testproject.MyIntentService.BROADCAST_ACTION;
import static com.andersen.internship.testproject.MyIntentService.DATA;
import static com.andersen.internship.testproject.MyIntentService.MESSAGE;
import static com.andersen.internship.testproject.MyIntentService.RECEIVED_TYPE;

public class ToolsFragment extends AbstractFragment implements com.andersen.internship.testproject.mvp.multithreading.View,
        com.andersen.internship.testproject.mvp.multithreading.View.ViewWithAsyncLoader,
        com.andersen.internship.testproject.mvp.multithreading.View.ViewWithService {

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.input_size)
    EditText inputSize;

    @BindView(R.id.output_data)
    TextView outputData;

    @BindView(R.id.start)
    Button start;

    @BindView(R.id.stop)
    Button stop;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Presenter presenter;
    private BroadcastReceiver broadcastReceiver;


    public ToolsFragment() {
        setIdTitle(R.string.tools);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tools, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();

        presenter = new MultithreadingPresenter();
        presenter.onAttach(this);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra(RECEIVED_TYPE, 0);

                if (type == DATA){
                    String rez = intent.getStringExtra(MESSAGE);
                    setData(rez);
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(broadcastReceiver, intFilt);
    }


    private void setOnClickListeners() {
        start.setOnClickListener(view -> startClick());
        stop.setOnClickListener(view -> stopClick());
    }

    private void startClick() {
        String type = spinner.getSelectedItem().toString();

        int size = Integer.parseInt(inputSize.getEditableText().toString());
        presenter.load(type, size);
    }

    private void stopClick() {

    }


    //View
    @Override
    public void showDownloadStatus(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(String s) {
        outputData.setText(s);
    }

    @Override
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }


    //LoaderManager.LoaderCallbacks
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.d("myLogs", "onCreateLoader");
        Loader<String> loader = new MyAsyncLoader(getActivity(), bundle);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        setData(s);
        Log.d("myLogs", "onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d("myLogs", "onLoaderReset");
    }

    @Override
    public void runLoader(int size) {
        showDownloadStatus("начало загрузки");
        Bundle bundle = new Bundle();
        bundle.putInt(SIZE, size);

        Loader<String> loader = getLoaderManager().restartLoader(LOADER_ID, bundle, this);
        loader.forceLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void runService(int size) {
        Intent intent = new Intent(getActivity(), MyIntentService.class);
        intent.putExtra(SIZE, size);
        getActivity().startService(intent);

    }
}