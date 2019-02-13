package com.andersen.internship.testproject.data;

import com.google.gson.annotations.SerializedName;

public class Data_ {


    private String name;

    private String thumbnail;

    private String postHint;

    private String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getThumbnail() {
        return thumbnail;
    }


    public String getPostHint() {
        return postHint;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
