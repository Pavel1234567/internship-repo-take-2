package com.andersen.internship.testproject.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andersen.internship.testproject.DividerItemDecoration;
import com.andersen.internship.testproject.R;
import com.andersen.internship.testproject.adapters.TabletMenuItemsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabletActivity extends AbstractDeviceTypeActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TabletMenuItemsAdapter adapter;
    public static final String POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = getResources().getString(R.string.tablet_edition);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initToolbar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider));

        adapter = new TabletMenuItemsAdapter();
        if (savedInstanceState != null){
            int pos = savedInstanceState.getInt(POSITION);
            adapter.setLastSelectedPosition(pos);
        }
        adapter.setListener(position -> {

            switch (position){
                case 0:
                    router.openCameraFragment();
                    break;

                case 1:
                    router.openGalleryFragment();
                    break;

                case 2:
                    router.openSlideshowFragment();
                    break;

                case 3:
                    router.openToolsFragment();
                    break;

                case 4:
                    router.openForegroundServiceFragment();
                    break;
            }
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view ->
                Snackbar.make(view, R.string.hello, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, adapter.getLastSelectedPosition());
    }
}