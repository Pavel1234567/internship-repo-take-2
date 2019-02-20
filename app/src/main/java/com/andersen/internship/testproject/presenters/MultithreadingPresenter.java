package com.andersen.internship.testproject.presenters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Model;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.View;

import java.util.List;

public class MultithreadingPresenter implements Presenter, Presenter.PresenterWithAsyncTask {

    private View view;



    @Override
    public void load(String loadType, int size) {
        String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
        if (loadType.equals(arrayTypes[0])){
            

        }else if (loadType.equals(arrayTypes[1])){
            view.showDownloadStatus("Загрузка началась");
            MyAsyncTask myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(size);

        }else if (loadType.equals(arrayTypes[2])){
            View.ViewWithAsyncLoader vieviewWithAsyncLoaderWAL = (View.ViewWithAsyncLoader) view;
            vieviewWithAsyncLoaderWAL.runLoader(size);

        }else if (loadType.equals(arrayTypes[3])){
            View.ViewWithService viewWithService = (View.ViewWithService) view;
            viewWithService.runService(size);
        }
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
    public void setRawData(List<Double> list) {
        String rez = Presenter.handleData(list);
        view.showDownloadStatus(rez);
    }


    private static class MyAsyncTask extends AsyncTask<Integer, Void, List<Double>> {

        private Presenter presenter;

        MyAsyncTask(Presenter presenter) {
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
            PresenterWithAsyncTask presenterWithAsyncTask = (PresenterWithAsyncTask) presenter;
            presenterWithAsyncTask.setRawData(list);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
