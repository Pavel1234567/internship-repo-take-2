package com.andersen.internship.testproject.models;

import com.andersen.internship.testproject.IMAGES_TYPES;
import com.andersen.internship.testproject.data.Post;
import com.andersen.internship.testproject.mvp.reddit.Model;

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

                    });
        }else {
            observable = Observable.just(getCache().getImages(imagesType));
        }
        return observable;
    }
}