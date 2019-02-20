package com.andersen.internship.testproject.presenters;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Model;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.View;

public class MultithreadingPresenter implements Presenter {

    private View view;
    private Model model = DummyServer.getDummyServer();


    @Override
    public void load(String loadType, int size) {
        String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
        if (loadType.equals(arrayTypes[0])){


        }else if (loadType.equals(arrayTypes[1])){


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
        //view.setProgress(percentageDownloaded);
    }
}
