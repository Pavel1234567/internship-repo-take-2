package com.andersen.internship.testproject.presenters;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.PresenterWithAsyncTool;
import com.andersen.internship.testproject.mvp.multithreading.ViewForMT;
import com.andersen.internship.testproject.mvp.multithreading.ViewWithAsyncLoader;
import com.andersen.internship.testproject.mvp.multithreading.ViewWithService;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MultithreadingPresenter implements Presenter, PresenterWithAsyncTool {

    private ViewForMT viewForMT;
    private String currentLoadType;
    private Disposable loadStatusDisposable;
    private String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
    private Handler handler;
    private Runnable removeCallbacks;
    private MyAsyncTask myAsyncTask;

    private Thread thread;

    private boolean isRunning = false;


    private void initLoadStatusDisposable(){
        loadStatusDisposable = DummyServer.getDummyServer()
                .observeProgress()
                .subscribe(integer -> {

                    if (viewForMT != null){
                        viewForMT.setProgress(integer);

                    }
                },
                        e -> isRunning = false,
                        () -> isRunning = false

                );

    }

    private void deleteLoadStatusDisposable(){
        loadStatusDisposable.dispose();
    }

    @Override
    public void load(String loadType, String arg) {
        //if (!type.matches("[-+]?\\d+"))  return;
        int size = Integer.parseInt(arg);

        viewForMT.showDownloadStatus(App.getContext().getResources().getString(R.string.start_load));

        if (isRunning){
            stopLoading();
        }
        isRunning = true;
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

        if (!isRunning) return;

        isRunning = false;
        deleteLoadStatusDisposable();
        viewForMT.setProgress(0);

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
        ViewWithService viewWithService = (ViewWithService) viewForMT;
        viewWithService.stopService();
    }

    private void stopAyncLoader() {
        ViewWithAsyncLoader viewWithAsyncLoader = (ViewWithAsyncLoader) viewForMT;
        viewWithAsyncLoader.stopLoader();
    }

    private void stopAsyncTask() {
        myAsyncTask.cancel(true);
    }

    private void stopHandler() {
        thread.interrupt();
    }

    private void runIntentService(int size) {
        ViewWithService viewWithService = (ViewWithService) viewForMT;
        viewWithService.runService(size);
    }

    private void runAyncLoader(int size) {
        ViewWithAsyncLoader vieviewWithAsyncLoaderWAL = (ViewWithAsyncLoader) viewForMT;
        vieviewWithAsyncLoaderWAL.runLoader(size);
    }

    private void runHandler(int size){
        handler = new Handler();

        thread = new Thread(() -> {
            try {
                List<Double> list = DummyServer.getDummyServer().getDummyData(size);
                String rez = Presenter.handleData(list);
                removeCallbacks = () -> {
                    setData(rez);
                };
                handler.post(removeCallbacks);
            } catch (Exception e) {
                e.printStackTrace();

            }
        });
        thread.start();
    }

    private void runAsyncTask(int size){
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(size);
    }

    @Override
    public void onDetach() {
        viewForMT = null;
        Log.d("myLogs", "onDetach");
    }

    @Override
    public void onCreate(ViewForMT viewForMT) {
        this.viewForMT = viewForMT;
        Log.d("myLogs", "onCreate");

    }

    @Override
    public void onDestroy() {
        stopLoading();
        onDetach();
    }

    @Override
    public void setData(String string) {
        viewForMT.setText(string);
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
