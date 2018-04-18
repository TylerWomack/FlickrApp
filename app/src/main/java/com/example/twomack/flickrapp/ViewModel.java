package com.example.twomack.flickrapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.database.Observable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ViewModel extends AndroidViewModel {

    Networker networker;

    public ViewModel(@NonNull Application application) {
        super(application);
        networker = new Networker();
    }

    MutableLiveData<List<Photo>> photos = new MutableLiveData<>();

    //empty objects. Not needed for this iteration of the app.
    public LiveData<List<Photo>> getPhotos(){
        return photos;
    }

    public void changePhotos(String search){
        List<Photo> newPhotos = networker.getNewPhotos(search);
        photos.setValue(newPhotos);
    }
}
