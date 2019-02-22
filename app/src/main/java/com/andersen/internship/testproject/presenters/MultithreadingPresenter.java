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

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MultithreadingPresenter implements Presenter, PresenterWithAsyncTool {

    private ViewForMT viewForMT;
    private String currentLoadType;
    private Disposable loadStatusDisposable;
    private List<String> listTypes = Arrays.asList(App.getContext().getResources().getStringArray(R.array.multithreading_items));
    private Handler handler;
    private Runnable removeCallbacks;
    private MyAsyncTask myAsyncTask;

    private Thread thread;

    private boolean isRunning = false;


    public static final int HANDLER = 0;
    public static final int ASYNC_TASK = 1;
    public static final int ASYNC_TASK_LOADER = 2;
    public static final int INTENT_SERVICE = 3;

    private static MultithreadingPresenter presenter;
    private MultithreadingPresenter(){}

    public static synchronized MultithreadingPresenter getPresenter(){
        if (presenter == null){
            presenter = new MultithreadingPresenter();
        }
        return presenter;
    }

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

        if (loadType.equals(listTypes.get(HANDLER))){
            runHandler(size);

        }else if (loadType.equals(listTypes.get(ASYNC_TASK))){
            runAsyncTask(size);

        }else if (loadType.equals(listTypes.get(ASYNC_TASK_LOADER))){
            runAyncLoader(size);

        }else if (loadType.equals(listTypes.get(INTENT_SERVICE))){
            runIntentService(size);
        }
    }

    @Override
    public void stopLoading() {

        if (!isRunning) return;

        isRunning = false;
        deleteLoadStatusDisposable();
        viewForMT.setProgress(0);

        if (currentLoadType.equals(listTypes.get(HANDLER))){
            stopHandler();

        }else if (currentLoadType.equals(listTypes.get(ASYNC_TASK))){
            stopAsyncTask();

        }else if (currentLoadType.equals(listTypes.get(ASYNC_TASK_LOADER))){
            stopAyncLoader();

        }else if (currentLoadType.equals(listTypes.get(INTENT_SERVICE))){
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
                String rez = MultithreadingPresenter.getPresenter().handleData(list);
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
    public void onStop() {
        viewForMT = null;
        Log.d("myLogs", "onStop");
    }

    @Override
    public void onCreate(ViewForMT viewForMT) {
        this.viewForMT = viewForMT;
        Log.d("myLogs", "onCreate");
    }

    @Override
    public void setData(String string) {
        if (viewForMT != null) {
            viewForMT.setText(string);
        }
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
            presenter.setData(MultithreadingPresenter.getPresenter().handleData(list));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public enum MultithreadingTypes{
        Handler,
        AsyncTask,
        AsyncTaskLoad,
        IntentService
    }
}