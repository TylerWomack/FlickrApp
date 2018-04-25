package com.example.twomack.flickrapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
    boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setNetworker(new Networker());
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        configureRecyclerView();
        configureObservables();

        mainViewModel.getPopularPhotos();
    }

    public void configureObservables(){
        mainViewModel.getPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                if (photos.size() == 0){
                    mainViewModel.getPopularPhotos();
                    Toast.makeText(getApplicationContext(), "Sorry, we couldn't find any more photos \n from that user", Toast.LENGTH_LONG).show();
                }
                adapter.setPhotoList(photos);
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new MainRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,
                4,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onPhotoClicked(int position, Photo photo) {
        Intent intent = new Intent(this, PhotoClickedActivity.class);
        intent.putExtra("url", photo.getUrl());
        intent.putExtra("title", photo.getTitle());
        intent.putExtra("userId", photo.getOwner());
        intent.putExtra("position", position);
        ArrayList<Photo> photos = new ArrayList<>(mainViewModel.getPhotos().getValue());
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
                    backPressed = false;
                    getMoreFromUser(data.getStringExtra("user"));
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        if (backPressed != true){
            mainViewModel.getPopularPhotos();
            backPressed = true;
        }else {
            super.onBackPressed();
        }
    }
}