package com.andersen.internship.testproject;

import com.andersen.internship.testproject.data.Post;

import java.util.HashMap;
import java.util.Map;


public class Cache {

    private static Cache cache;

    private Map<IMAGES_TYPES, Post> map = new HashMap<>();
    private Cache(){}

    public static synchronized Cache getCache(){
        if (cache == null)
            cache = new Cache();
        return cache;
    }


    public Post getImages(IMAGES_TYPES imagesTypes) {
        return map.get(imagesTypes);
    }

    public void setImages(Post post, IMAGES_TYPES imagesType) {
        map.put(imagesType, post);
    }

    public boolean contains(IMAGES_TYPES imagesTypes){
        return map.containsKey(imagesTypes);
    }
}
