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

    private String unreceivedText = "";

    private static MultithreadingPresenter presenter;
    private MultithreadingPresenter(){}

    private static final String intMatcher = "([0-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|[1-8][0-9]{4}|9[0-8][0-9]{3}|99[0-8][0-9]{2}|999[0-8][0-9]|9999[0-9]|[1-8][0-9]{5}|9[0-8][0-9]{4}|99[0-8][0-9]{3}|999[0-8][0-9]{2}|9999[0-8][0-9]|99999[0-9]|[1-8][0-9]{6}|9[0-8][0-9]{5}|99[0-8][0-9]{4}|999[0-8][0-9]{3}|9999[0-8][0-9]{2}|99999[0-8][0-9]|999999[0-9]|[1-8][0-9]{7}|9[0-8][0-9]{6}|99[0-8][0-9]{5}|999[0-8][0-9]{4}|9999[0-8][0-9]{3}|99999[0-8][0-9]{2}|999999[0-8][0-9]|9999999[0-9]|[1-8][0-9]{8}|9[0-8][0-9]{7}|99[0-8][0-9]{6}|999[0-8][0-9]{5}|9999[0-8][0-9]{4}|99999[0-8][0-9]{3}|999999[0-8][0-9]{2}|9999999[0-8][0-9]|99999999[0-9]|1[0-9]{9}|20[0-9]{8}|21[0-3][0-9]{7}|214[0-6][0-9]{6}|2147[0-3][0-9]{5}|21474[0-7][0-9]{4}|214748[0-2][0-9]{3}|2147483[0-5][0-9]{2}|21474836[0-3][0-9]|214748364[0-7])";


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
        if (!arg.matches("[-+]?\\d+")){
            showMessageToView(R.string.enter_number);
            return;
        }

        if (!arg.matches(intMatcher)){
            showMessageToView(R.string.too_long_number);
            return;
        }


        int size = Integer.parseInt(arg);

        showMessageToView(R.string.start_load);
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
                String result = MultithreadingPresenter.getPresenter().handleData(list);
                removeCallbacks = () -> {
                    setText(result);
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

    private void showMessageToView(int resID){
        viewForMT.showMessage(App.getContext().getResources().getString(resID));

    }

    @Override
    public void onStop() {
        viewForMT = null;
    }

    @Override
    public void onCreate(ViewForMT viewForMT) {
        this.viewForMT = viewForMT;
        if (!unreceivedText.isEmpty()){
            viewForMT.setText(unreceivedText);
            unreceivedText = "";
        }
    }

    @Override
    public void setText(String string) {
        if (viewForMT != null) {
            viewForMT.setText(string);
        }else {
            unreceivedText = string;
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
            presenter.setText(MultithreadingPresenter.getPresenter().handleData(list));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}