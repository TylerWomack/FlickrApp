package com.example.twomack.flickrapp.networking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.twomack.flickrapp.BuildConfig;
import com.example.twomack.flickrapp.data.FlickrResponse;
import com.example.twomack.flickrapp.data.Photo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Networker {

    private Retrofit retrofitInstance;
    final private static String BASEURL = "https://api.flickr.com/services/";

    MutableLiveData<List<Photo>> responses = new MutableLiveData<>();

    public LiveData<List<Photo>> getPhotoObservable(){return responses;}


    public String getYesterdaysDate(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        String yesterdaysDate = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
        return yesterdaysDate;
    }

    public void updateTodaysPhotos(){

        Retrofit retrofit = buildRetrofit();
        API api = retrofit.create(API.class);

        //you're getting 5 pages of 100 responses, but you're only using 99 of them.
        Call<FlickrResponse> flickrObservable = api.getInterestingPhotosByDate(
                "flickr.interestingness.getList", getYesterdaysDate(), "523c09e21fe0a874f8c185ff67f853d4",
                "json", "1", "300");

        flickrObservable.enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {

                if (response.body() == null || response.body().getPhotos() == null) {
                    return;
                }

                    responses.setValue(response.body().getPhotos().getPhoto());

            }

            //onFailure means the network isn't working or a serialization issue, doesn't encompass all errors.
            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {

            }
        });

    }

    public List<Photo> getPhotosSynchronously(int page, int loadCount){
        Retrofit retrofit = buildRetrofit();
        API api = retrofit.create(API.class);

        String count = String.valueOf(loadCount);

        //you're getting 5 pages of 100 responses, but you're only using 99 of them.
        Call<FlickrResponse> flickrObservable = api.getInterestingPhotosByDateAndPage(
                "flickr.interestingness.getList", getYesterdaysDate(), "523c09e21fe0a874f8c185ff67f853d4",
                "json", "1", String.valueOf(loadCount), String.valueOf(page));

        try {
            //responses.postValue(flickrObservable.execute().body().getPhotos().getPhoto());
            return flickrObservable.execute().body().getPhotos().getPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void findPhotosFromUser(String userId){

        Retrofit retrofit = buildRetrofit();
        API api = retrofit.create(API.class);
        api.getPhotosFromUser(
                "flickr.people.getPhotosOf", userId, "523c09e21fe0a874f8c185ff67f853d4",
                "json", "1").enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                FlickrResponse flickrResponse = response.body();
                if(flickrResponse.getPhotos() == null) {
                    return;
                }
                responses.setValue(flickrResponse.getPhotos().getPhoto());
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {

            }
        });
    }

    private Retrofit buildRetrofit(){
        if(retrofitInstance != null) {
            return retrofitInstance;
        }
        boolean debug = BuildConfig.DEBUG;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if(debug) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofitInstance = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitInstance;
    }
}
