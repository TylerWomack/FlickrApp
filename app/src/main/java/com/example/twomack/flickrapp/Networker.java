package com.example.twomack.flickrapp;

import java.util.ArrayList;
import java.util.List;

public class Networker {

    public Networker(){}

    //todo: return something besides dummy data.
    public List<Photo> getNewPhotos(String searchquery){
        ArrayList<Photo> photos = new ArrayList<>();
        for (int i = 0; i < 200; i++){
            Photo photo = new Photo();
            photo.setId(i);
            photos.add(photo);
        }
        return photos;
    }

}
