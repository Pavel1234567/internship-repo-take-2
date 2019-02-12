package com.andersen.internship.testproject.api;

import com.andersen.internship.testproject.data.Child;
import com.andersen.internship.testproject.data.Post;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditAPI {

    @GET("r/EarthPorn/{path}/.json?limit=100")
    Observable<Post> getList(@Path("path") String path);
}