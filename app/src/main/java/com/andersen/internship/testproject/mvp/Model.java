package com.andersen.internship.testproject.mvp;

import com.andersen.internship.testproject.IMAGES_TYPES;
import com.andersen.internship.testproject.data.Post;

import io.reactivex.Observable;

public interface Model {

    Observable<Post> loadImages(IMAGES_TYPES imagesType);
}
