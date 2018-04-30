package com.example.twomack.flickrapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;

import java.security.Key;
import java.util.List;

public class PagedDataSource extends PageKeyedDataSource<Integer, Photo> {

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Photo> callback) {
        Networker networker = new Networker();

        List<Photo> photos = networker.getPhotosSynchronously(1, params.requestedLoadSize);
        callback.onResult(photos, 1, 2);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

        //loadAfter is never triggered.
        int key = params.key;
        Networker networker = new Networker();
        List<Photo> photos = networker.getPhotosSynchronously(key, params.requestedLoadSize);
        //callback.onResult(photos, key++);
    }
}
