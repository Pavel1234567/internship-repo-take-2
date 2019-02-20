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

public class MultithreadingPresenter implements Presenter, Presenter.PresenterWithAsyncTool {

    private View view;


    public MultithreadingPresenter() {
        DummyServer.getDummyServer()
                .observeProgress()
                .subscribe(integer -> {

                    if (view != null){
//                        Log.d("myLogs3", "setProgress " + String.valueOf(view.hashCode()));

                        view.setProgress(integer);
                    }
        });
    }

    @Override
    public void load(String loadType, int size) {
        String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
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

    private void runIntentService(int size) {
        View.ViewWithService viewWithService = (View.ViewWithService) view;
        viewWithService.runService(size);
    }

    private void runAyncLoader(int size) {
        View.ViewWithAsyncLoader vieviewWithAsyncLoaderWAL = (View.ViewWithAsyncLoader) view;
        vieviewWithAsyncLoaderWAL.runLoader(size);
    }

    private void runHandler(int size){
        Handler handler = new Handler();

        Thread thread = new Thread(() -> {
            List<Double> list = DummyServer.getDummyServer().getDummyData(size);
            String rez = Presenter.handleData(list);
            Log.d("myLogs3", "thread " + String.valueOf(view == null));

            handler.post(() -> {
                Log.d("myLogs3", "post " + String.valueOf(view == null));
                setData(rez);
                view.showDownloadStatus("конец загрузки");
            });
        });

        thread.start();
    }

    private void runAsyncTask(int size){
        view.showDownloadStatus("Загрузка началась");
        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(size);
    }

    @Override
    public void onDetach() {
        Log.d("myLogs3", "onDetach " + String.valueOf(view.hashCode()));
        view = null;
    }

    @Override
    public void onAttach(View view) {
        this.view = view;
        Log.d("myLogs3", "onAttach " + String.valueOf(view.hashCode()));
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
