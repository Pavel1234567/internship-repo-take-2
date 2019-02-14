package com.andersen.internship.testproject.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.andersen.internship.testproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobileActivity extends AbstractDeviceTypeActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title = getResources().getString(R.string.mobile_edition);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initToolbar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(view ->
                Snackbar.make(view, R.string.hello, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_camera:
                router.openCameraFragment();
                break;

            case R.id.nav_gallery:
                router.openGalleryFragment();
                break;

            case R.id.nav_slideshow:
                router.openSlideshowFragment();
                break;

            case R.id.nav_manage:
                router.openToolsFragment();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
