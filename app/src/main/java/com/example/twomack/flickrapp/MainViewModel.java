package com.example.twomack.flickrapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;

import java.util.List;

public class MainViewModel extends ViewModel {

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
}
