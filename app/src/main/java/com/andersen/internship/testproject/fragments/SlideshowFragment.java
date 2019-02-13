package com.andersen.internship.testproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.andersen.internship.testproject.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SlideshowFragment extends AbstractFragment {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SlideshowFragment() {
        setIdTitle(R.string.slideshow);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slideshow, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (compositeDisposable.size() > 0){
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();

        Disposable disposable = Flowable.interval(50, TimeUnit.MILLISECONDS)
                .take(100)
                .map(aLong -> {
                    int i = aLong.intValue();
                    return ++i;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> progressBar.setProgress(i));

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}