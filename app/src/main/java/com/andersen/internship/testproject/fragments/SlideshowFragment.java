package com.andersen.internship.testproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andersen.internship.testproject.GridImagesPresenter;
import com.andersen.internship.testproject.IMAGES_TYPES;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.adapters.GridImagesAdapter;
import com.andersen.internship.testproject.data.Child;
import com.andersen.internship.testproject.mvp.Presenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static com.andersen.internship.testproject.fragments.SlideshowFragment.LayoutManagerTypes.GRID;
import static com.andersen.internship.testproject.fragments.SlideshowFragment.LayoutManagerTypes.LINEAR;

public class SlideshowFragment extends AbstractFragment implements com.andersen.internship.testproject.mvp.View {

    @BindView(R.id.new_posts)
    Button newPosts;

    @BindView(R.id.top)
    Button topPosts;

    @BindView(R.id.image_list)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private GridImagesAdapter adapter;

    private Presenter presenter;

    private IMAGES_TYPES imagesTypes = IMAGES_TYPES.NEW;

    private LayoutManagerTypes layoutManagerType;

    public SlideshowFragment() {
        setIdTitle(R.string.slideshow);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManagerType = GRID;
        setHasOptionsMenu(true);
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

        initRecyclerView();
        initPresenter();
        setOnClickListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.slideshow_fragment, menu);

        MenuItem item = menu.findItem(R.id.manager_type);

        if (layoutManagerType == GRID){
            item.setIcon(R.drawable.ic_looks_one_black_24dp);
        }else {
            item.setIcon(R.drawable.ic_looks_two_black_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (layoutManagerType == GRID){
            setLinearLayoutManager();
            item.setIcon(R.drawable.ic_looks_two_black_24dp);

        }else {
            setGridLayoutManager();
            item.setIcon(R.drawable.ic_looks_one_black_24dp);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnClickListeners() {
        topPosts.setOnClickListener(view -> {
            presenter.loadImages(IMAGES_TYPES.TOP);
            imagesTypes = IMAGES_TYPES.TOP;
        });

        newPosts.setOnClickListener(view -> {
            presenter.loadImages(IMAGES_TYPES.NEW);
            imagesTypes = IMAGES_TYPES.NEW;

        });

    }

    private void setLinearLayoutManager() {
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);

        layoutManagerType = LINEAR;
    }

    private void setGridLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });

        recyclerView.setLayoutManager(layoutManager);

        layoutManagerType = GRID;
    }


    private void initPresenter() {
        presenter = new GridImagesPresenter(this);
        getLifecycle().addObserver(presenter);
        presenter.loadImages(imagesTypes);

    }

    private void initRecyclerView() {

        switch (layoutManagerType) {

            case GRID:
                setGridLayoutManager();
                break;

            case LINEAR:
                setLinearLayoutManager();
                break;
        }

        adapter = new GridImagesAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent(List<Child> children) {
        adapter.setList(children);
    }

    @Override
    public void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String cause) {
        Toast.makeText(getActivity(), cause, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgress(int value) {
        progressBar.setProgress(value);
    }

    enum LayoutManagerTypes {
        
        LINEAR,
        GRID
    }
}