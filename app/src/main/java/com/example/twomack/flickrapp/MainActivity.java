package com.example.twomack.flickrapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.twomack.flickrapp.data.FlickrResponse;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;
import com.example.twomack.flickrapp.recyclerView.MainRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.twomack.flickrapp.PhotoClickedActivity.RESULT_MORE_PHOTOS_FROM_USER;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.OnPhotoSelectedListener {

    private MainViewModel mainViewModel;
    private MainRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    boolean loadedImagesFromUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setNetworker(new Networker());
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        configureRecyclerView();
        mainViewModel.updatePhotoPages();
        configureObservables();
    }

    public void configureObservables(){
        mainViewModel.getPhotoPages().observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                if (photos != null) {
                    adapter.submitList(photos);
                }
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new MainRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPhotoClicked(int position, Photo photo) {

        //an empty observer. This is necessary because the LivePagedListBuilder doesn't do any work (and create new pagedLists) until it is observed.
        mainViewModel.getPhotoPages().observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {}
        });


        Intent intent = new Intent(this, PhotoClickedActivity.class);
        intent.putExtra("url", photo.getUrl());
        intent.putExtra("title", photo.getTitle());
        intent.putExtra("userId", photo.getOwner());
        intent.putExtra("position", position);
        ArrayList<Photo> photos = new ArrayList<>();
        if (mainViewModel.getPhotoPages().getValue() == null){
            return;
        }
        int size = mainViewModel.getPhotoPages().getValue().size();
        PagedList<Photo> page = mainViewModel.getPhotoPages().getValue();
        for (int i = 0; i < size; i++){
            photos.add(page.get(i));
        }
        intent.putParcelableArrayListExtra("photos", photos);
        startActivityForResult(intent, 8907);
    }

    public void getMoreFromUser(String userId){
        if(userId == null) {
            return;
        }
        mainViewModel.getPhotosFromUser(userId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 8907:
                if(resultCode == RESULT_MORE_PHOTOS_FROM_USER) {
                    loadedImagesFromUser = true;
                    getMoreFromUser(data.getStringExtra("user"));
                    //configureObservables needed to refresh UI
                    configureObservables();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (loadedImagesFromUser){
            mainViewModel.getPhotoPages().removeObservers(this);
            mainViewModel.updatePhotoPages();
            //configureObservables needed to refresh UI
            configureObservables();
            loadedImagesFromUser = false;
        }else {
            super.onBackPressed();
        }
    }
}