package com.andersen.internship.testproject.routers;

import android.support.v7.app.AppCompatActivity;

import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.fragments.AbstractFragment;
import com.andersen.internship.testproject.fragments.CameraFragment;
import com.andersen.internship.testproject.fragments.ForegroundServiceFragment;
import com.andersen.internship.testproject.fragments.GalleryFragment;
import com.andersen.internship.testproject.fragments.SlideshowFragment;
import com.andersen.internship.testproject.fragments.ToolsFragment;

import java.lang.ref.WeakReference;

public class FragmentRouter {

    private WeakReference<? extends AppCompatActivity> activity;

    public FragmentRouter(AppCompatActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public void openCameraFragment(){
        startNewFragment(new CameraFragment());
    }

    public void openGalleryFragment(){
        startNewFragment(new GalleryFragment());
    }

    public void openSlideshowFragment(){
        startNewFragment(new SlideshowFragment());
    }

    public void openToolsFragment(){
        startNewFragment(new ToolsFragment());
    }

    public void openForegroundServiceFragment(){
        startNewFragment(new ForegroundServiceFragment());
    }

    private void startNewFragment(AbstractFragment fragment){
        activity.get()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_place_holder, fragment)
                .commit();

    }
}
