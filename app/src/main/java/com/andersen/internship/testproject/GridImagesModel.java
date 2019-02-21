package com.andersen.internship.testproject;

import android.util.Log;

import com.andersen.internship.testproject.data.Post;
import com.andersen.internship.testproject.mvp.Model;

import io.reactivex.Observable;

import static com.andersen.internship.testproject.Cache.getCache;
import static com.andersen.internship.testproject.NetworkService.getNetworkService;

public class GridImagesModel implements Model {

    private static GridImagesModel model;

    private GridImagesModel(){}

    public static synchronized GridImagesModel getModel(){
        if (model == null){
            model = new GridImagesModel();
        }
        return model;
    }

    @Override
    public Observable<Post> loadImages(IMAGES_TYPES imagesType) {
        Observable<Post> observable;
        if (!getCache().contains(imagesType)){
            observable = getNetworkService()
                    .getJSONApi()
                    .getList(imagesType.toString())
                    .doOnNext( items -> {
                        getCache().setImages(items, imagesType);
                        Log.d("myLogs", "network");

                    });
        }else {
            observable = Observable.just(getCache().getImages(imagesType));
            Log.d("myLogs", "cache");
        }
        return observable;
    }
}