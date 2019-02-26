package com.andersen.internship.testproject.presenters;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.andersen.internship.testproject.IMAGES_TYPES;
import com.andersen.internship.testproject.data.Child;
import com.andersen.internship.testproject.data.Post;
import com.andersen.internship.testproject.mvp.reddit.Model;
import com.andersen.internship.testproject.mvp.reddit.Presenter;
import com.andersen.internship.testproject.mvp.reddit.ViewForReddit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.andersen.internship.testproject.models.GridImagesModel.getModel;

public class GridImagesPresenter implements Presenter {

    public static final String IMAGE = "image";

    private ViewForReddit viewForReddit;
    private Model model;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Flowable<Integer> progressImitation;

    public GridImagesPresenter(ViewForReddit viewForReddit) {
        this.viewForReddit = viewForReddit;
        model = getModel();

        progressImitation = Flowable
                .interval(50, TimeUnit.MILLISECONDS)
                .take(100)
                .map(aLong -> {
                    int i = aLong.intValue();
                    return ++i;
                });
    }

    @Override
    public void loadImages(IMAGES_TYPES imagesType) {
        viewForReddit.showProgress();
        if (compositeDisposable.size() > 0){
            compositeDisposable.dispose();
            compositeDisposable = new CompositeDisposable();
        }
        Disposable disposable = model.loadImages(imagesType)
                .map((Function<Post, List>) post -> {
                    List<Child> list = post.getData().getChildren();
                    for (int i = 0; i < list.size(); ) {
                        
                        String postHint = list.get(i).getData().getPostHint();
                        if (postHint == null || !postHint.equals(IMAGE)){
                            list.remove(i);
                        }

                        else i++;
                    }
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> viewForReddit.showContent(list),

                        e -> {
                            viewForReddit.hideProgress();
                            viewForReddit.showError(e.getMessage());
                        },

                        () -> {
                            Disposable progressImitationDisposable = progressImitation
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            i -> viewForReddit.setProgress(i),
                                            e -> {
                                                viewForReddit.showError(e.getMessage());
                                                viewForReddit.hideProgress();
                                            },
                                            () -> viewForReddit.hideProgress()
                                    );

                            compositeDisposable.add(progressImitationDisposable);
                        });

        compositeDisposable.add(disposable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onDisconnect() {
        compositeDisposable.dispose();
        viewForReddit = null;
    }
}