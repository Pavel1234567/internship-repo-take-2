package com.andersen.internship.testproject.fragments;

import android.os.Bundle;
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
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.presenters.MultithreadingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andersen.internship.testproject.MyAsyncLoader.LOADER_ID;
import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;

public class ToolsFragment extends AbstractFragment implements com.andersen.internship.testproject.mvp.multithreading.View,
        com.andersen.internship.testproject.mvp.multithreading.View.ViewWithAsyncLoader {

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



    public ToolsFragment() {
        setIdTitle(R.string.tools);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tools, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Log.d("myLogs", "onCreateView"+String.valueOf(progressBar == null));
        Log.d("myLogs", "onCreateView"+String.valueOf(hashCode()));


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();

        presenter = new MultithreadingPresenter();
        presenter.onAttach(this);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Log.d("myLogs", "initLoader");

    }

    private void setOnClickListeners(){
        start.setOnClickListener(view -> startClick());
        stop.setOnClickListener(view -> stopClick());
    }

    private void startClick(){
        String type = spinner.getSelectedItem().toString();

        int size = Integer.parseInt(inputSize.getEditableText().toString());
        presenter.load(type, size);
    }

    private void stopClick(){

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
        if (progressBar != null)
            progressBar.setProgress(progress);
        Log.d("myLogs", String.valueOf(progressBar == null));
        Log.d("myLogs", String.valueOf(hashCode()));

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
    }
}