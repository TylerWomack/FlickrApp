package com.example.twomack.flickrapp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.twomack.flickrapp.data.FlickrResponse;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {


    LiveData<PagedList<Photo>> photoPages = new LiveData<PagedList<Photo>>() {
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
                return new PagedDataSource();
            }
        };

        /* //other possible configuration
        @SuppressWarnings("unchecked")
        LivePagedListBuilder<Integer, Photo> builder2 = new LivePagedListBuilder(factory, 60);
        */

        @SuppressWarnings("unchecked")
        LivePagedListBuilder<Integer, Photo> builder = new LivePagedListBuilder(factory, new PagedList.Config.Builder()
                .setPageSize(20)
                .setPrefetchDistance(100)
                .setEnablePlaceholders(false)
                .build());


        photoPages = builder.build();
    }

    //old code that works below
    /*
    Networker networker;

    public void setNetworker(Networker networker) {
        this.networker = networker;
    }

    public LiveData<List<Photo>> getPhotos(){
        return networker.getPhotoObservable();
    }

    public void getPopularPhotos(){
        networker.updateTodaysPhotos(); }

    public void getPhotosFromUser(String userId){
        networker.findPhotosFromUser(userId);
        }
        */
}
