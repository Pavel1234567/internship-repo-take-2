package com.andersen.internship.testproject.presenters;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.andersen.internship.testproject.App;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.models.DummyServer;
import com.andersen.internship.testproject.mvp.multithreading.Model;
import com.andersen.internship.testproject.mvp.multithreading.Presenter;
import com.andersen.internship.testproject.mvp.multithreading.View;

import static com.andersen.internship.testproject.MyAsyncLoader.LOADER_ID;
import static com.andersen.internship.testproject.MyAsyncLoader.SIZE;

public class MultithreadingPresenter implements Presenter {

    private View.ViewWithAsyncLoader view;
    private Model model = DummyServer.getDummyServer();

    private int percentageDownloaded = 0;


    public MultithreadingPresenter() {
    }

    @Override
    public void load(String loadType, int size) {
        String[] arrayTypes = App.getContext().getResources().getStringArray(R.array.multithreading_items);
        if (loadType.equals(arrayTypes[0])){


        }else if (loadType.equals(arrayTypes[1])){


        }else if (loadType.equals(arrayTypes[2])){

            view.runLoader(size);


        }else if (loadType.equals(arrayTypes[3])){


        }

    }

    @Override
    public void onDetach() {
        view = null;
    }


    @Override
    public void onAttach(View view) {
        this.view = (View.ViewWithAsyncLoader) view;
        //view.setProgress(percentageDownloaded);
    }
}
