package com.andersen.internship.testproject.presenters;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.View;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MultithreadingPresenter implements Presenter, Presenter.PresenterWithAsyncTool {

    private View view;
    private String currentLoadType;
    private Disposable loadStatusDisposable;
    private String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
    private Handler handler;
    private Runnable removeCallbacks;
    private MyAsyncTask myAsyncTask;

    private Thread thread;


    private void initLoadStatusDisposable(){
        loadStatusDisposable = DummyServer.getDummyServer()
                .observeProgress()
                .subscribe(integer -> {

                    if (view != null){
                        view.setProgress(integer);
                    }
                });
    }

    private void deleteLoadStatusDisposable(){
        loadStatusDisposable.dispose();
        loadStatusDisposable = null;
    }

    @Override
    public void load(String loadType, int size) {
        if (loadStatusDisposable != null){
            deleteLoadStatusDisposable();
        }
        initLoadStatusDisposable();

        currentLoadType = loadType;

        if (loadType.equals(arrayTypes[0])){
            runHandler(size);

        }else if (loadType.equals(arrayTypes[1])){
            runAsyncTask(size);

        }else if (loadType.equals(arrayTypes[2])){
            runAyncLoader(size);

        }else if (loadType.equals(arrayTypes[3])){
            runIntentService(size);
        }
    }

    @Override
    public void stopLoading() {
        if (loadStatusDisposable != null) {
            loadStatusDisposable.dispose();
        }else {
            return;
        }

        if (currentLoadType == null){
            return;
        }

        if (currentLoadType.equals(arrayTypes[0])){
            stopHandler();

        }else if (currentLoadType.equals(arrayTypes[1])){
            stopAsyncTask();

        }else if (currentLoadType.equals(arrayTypes[2])){
            stopAyncLoader();

        }else if (currentLoadType.equals(arrayTypes[3])){
            stopIntentService();
        }
    }

    private void stopIntentService() {
        View.ViewWithService viewWithService = (View.ViewWithService) view;
        viewWithService.stopService();
    }

    private void stopAyncLoader() {
        View.ViewWithAsyncLoader viewWithAsyncLoader = (View.ViewWithAsyncLoader) view;
        viewWithAsyncLoader.stopLoader();

    }

    private void stopAsyncTask() {
        myAsyncTask.cancel(true);
    }

    private void stopHandler() {
        handler.removeCallbacks(removeCallbacks);
        thread.interrupt();
    }

    private void runIntentService(int size) {
        View.ViewWithService viewWithService = (View.ViewWithService) view;
        viewWithService.runService(size);
    }

    private void runAyncLoader(int size) {
        View.ViewWithAsyncLoader vieviewWithAsyncLoaderWAL = (View.ViewWithAsyncLoader) view;
        vieviewWithAsyncLoaderWAL.runLoader(size);
    }

    private void runHandler(int size){
        handler = new Handler();

        thread = new Thread(() -> {
            List<Double> list = DummyServer.getDummyServer().getDummyData(size);
            String rez = Presenter.handleData(list);
            removeCallbacks = () -> {
                setData(rez);
                view.showDownloadStatus(App.getContext().getResources().getString(R.string.finish_load));
            };
            handler.post(removeCallbacks);
        });


        thread.start();
    }

    private void runAsyncTask(int size){
        view.showDownloadStatus(App.getContext().getResources().getString(R.string.start_load));
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(size);
    }

    @Override
    public void onDetach() {
        view = null;
    }

    @Override
    public void onAttach(View view) {
        this.view = view;
    }

    @Override
    public void setData(String string) {
        view.setData(string);
    }


    private static class MyAsyncTask extends AsyncTask<Integer, Void, List<Double>> {

        private PresenterWithAsyncTool presenter;

        MyAsyncTask(PresenterWithAsyncTool presenter) {
            this.presenter = presenter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Double> doInBackground(Integer... integers) {
            List<Double> list = DummyServer.getDummyServer().getDummyData(integers[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<Double> list) {
            super.onPostExecute(list);
            presenter.setData(Presenter.handleData(list));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
