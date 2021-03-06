package com.example.twomack.flickrapp;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;

public class MainViewModel extends ViewModel {

    private Networker networker;

    public void setNetworker(Networker networker) {
        this.networker = networker;
    }

    private LiveData<PagedList<Photo>> photoPages = new LiveData<PagedList<Photo>>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<PagedList<Photo>> observer) {
            super.observe(owner, observer);
        }
    };

    public LiveData<PagedList<Photo>> getPhotoPages() {
        return photoPages;
    }

    public void updatePhotoPages() {
        DataSource.Factory<Integer, Photo> factory = new DataSource.Factory<Integer, Photo>() {
            @Override
            public DataSource<Integer, Photo> create() {
                networker.setUserId(null);
                return networker;
            }
        };

            @SuppressWarnings("unchecked")
            LivePagedListBuilder<Integer, Photo> builder = new LivePagedListBuilder(factory, new PagedList.Config.Builder()
                    .setPageSize(40)
                    .setPrefetchDistance(280)
                    .setEnablePlaceholders(true)
                    .build());
            photoPages = builder.build();
    }

    //overloaded
    private void updatePhotoPages(final String userId) {
        DataSource.Factory<Integer, Photo> factory = new DataSource.Factory<Integer, Photo>() {
            @Override
            public DataSource<Integer, Photo> create() {
                networker.setUserId(userId);
                return networker;
            }
        };

        @SuppressWarnings("unchecked")
        LivePagedListBuilder<Integer, Photo> builder = new LivePagedListBuilder(factory, new PagedList.Config.Builder()
                .setPageSize(40)
                .setPrefetchDistance(280)
                .setEnablePlaceholders(true)
                .build());
        photoPages = builder.build();
    }

    public void getPhotosFromUser(String userId) {
        updatePhotoPages(userId);
    }
}
