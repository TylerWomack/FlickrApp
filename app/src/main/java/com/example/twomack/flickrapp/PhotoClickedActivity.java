package com.example.twomack.flickrapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.twomack.flickrapp.ViewPager.PhotoClickedFragment;
import com.example.twomack.flickrapp.data.Photo;

import java.util.ArrayList;

public class PhotoClickedActivity extends AppCompatActivity {
    public static final int RESULT_MORE_PHOTOS_FROM_USER = 678;

    TextView title;
    PhotoClickedViewModel viewModel;

    private String userId;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    ViewPager viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager);
        ArrayList<Photo> photos = getIntent().getParcelableArrayListExtra("photos");
        viewModel = ViewModelProviders.of(this).get(PhotoClickedViewModel.class);
        viewModel.setPhotos(photos);
        configureViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void getMorePhotosFromUser(MenuItem m){
        Intent intent = new Intent();
        int position = viewPager.getCurrentItem();
        userId = viewModel.getPhotos().get(position).getOwner();
        intent.putExtra("user", userId);
        setResult(RESULT_MORE_PHOTOS_FROM_USER, intent);
        finish();
    }

    public void configureViewPager(){
        viewPager = findViewById(R.id.viewPager);
        int position = getIntent().getIntExtra("position", -1);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), viewModel.getPhotos().size());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(position);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private int size;
        public ScreenSlidePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.size = size;
        }

        @Override
        public Fragment getItem(int position) { return PhotoClickedFragment.newInstance(position); }

        @Override
        public int getCount() {
            return size;
        }
    }
}