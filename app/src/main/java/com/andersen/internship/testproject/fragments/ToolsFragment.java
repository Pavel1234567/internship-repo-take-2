package com.andersen.internship.testproject.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
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
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.ViewForMultiThreading;
import com.andersen.internship.testproject.mvp.multithreading.ViewWithAsyncLoader;
import com.andersen.internship.testproject.mvp.multithreading.ViewWithService;
import com.andersen.internship.testproject.presenters.MultithreadingPresenter;
import com.andersen.internship.testproject.routers.ServiceRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andersen.internship.testproject.MyAsyncLoader.LOADER_ID;
import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;
import static com.andersen.internship.testproject.services.MyIntentService.BROADCAST_ACTION;
import static com.andersen.internship.testproject.services.MyIntentService.DATA;
import static com.andersen.internship.testproject.services.MyIntentService.MESSAGE;
import static com.andersen.internship.testproject.services.MyIntentService.RECEIVED_TYPE;

public class ToolsFragment extends AbstractFragment implements ViewForMultiThreading, ViewWithAsyncLoader, ViewWithService {

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

    @BindView(R.id.clear)
    Button clear;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Presenter presenter;
    private BroadcastReceiver broadcastReceiver;
    private ServiceRouter serviceRouter;

    public ToolsFragment() {
        setIdTitle(R.string.tools);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = MultithreadingPresenter.getPresenter();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tools, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.onCreate(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int type = intent.getIntExtra(RECEIVED_TYPE, 0);

                if (type == DATA){
                    String result = intent.getStringExtra(MESSAGE);
                    setText(result);
                }
            }
        };
        serviceRouter = new ServiceRouter(getActivity());
    }

    @Override
    public void onResume() {

        super.onResume();
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(broadcastReceiver, intFilt);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    private void setOnClickListeners() {

        start.setOnClickListener(view -> startClick());
        stop.setOnClickListener(view -> stopClick());
        clear.setOnClickListener(view -> outputData.setText(""));
    }

    private void startClick() {

        String type = spinner.getSelectedItem().toString();
        String arg = inputSize.getEditableText().toString();
        presenter.load(type, arg);
    }

    private void stopClick() {
        presenter.stopLoading();
    }

    //ViewForMultiThreading
    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setText(String s) {
        outputData.setText(s);
        showMessage(getString(R.string.finish_load));
    }

    @Override
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }


    //LoaderManager.LoaderCallbacks
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        Loader<String> loader = new MyAsyncLoader(getContext(), bundle);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        setText(s);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }


    @Override
    public void runLoader(int size) {
        Bundle bundle = new Bundle();
        bundle.putInt(SIZE, size);

        Loader<String> loader = getLoaderManager().restartLoader(LOADER_ID, bundle, this);
        loader.forceLoad();
    }

    @Override
    public void stopLoader() {
        getLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public void runService(int size) {
        serviceRouter.startIntentService(size);
    }

    @Override
    public void stopService() {
        serviceRouter.stopIntentService();
    }
}