package com.example.twomack.flickrapp;


import android.arch.lifecycle.ViewModel;
import com.example.twomack.flickrapp.data.Photo;


import java.util.List;

public class PhotoClickedViewModel extends ViewModel {

    private List<Photo> photos;

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos(){
        return photos;
    }

}
