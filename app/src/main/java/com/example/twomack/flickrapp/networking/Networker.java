package com.example.twomack.flickrapp.networking;

import android.annotation.SuppressLint;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.twomack.flickrapp.BuildConfig;
import com.example.twomack.flickrapp.data.FlickrResponse;
import com.example.twomack.flickrapp.data.Photo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Networker extends PageKeyedDataSource<Integer, Photo> {

    private API api;
    private int pages;
    public String userId;

    public Networker(){
        Retrofit retrofit = buildRetrofit();
        api = retrofit.create(API.class);
    }

    private Retrofit retrofitInstance;
    final private static String BASEURL = "https://api.flickr.com/services/";

    @SuppressLint("SimpleDateFormat")
    private String getYesterdaysDate(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(yesterday);

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

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Photo> callback) {

        if (userId == null){
            api.getInterestingPhotosByDateAndPage("flickr.interestingness.getList", getYesterdaysDate(), "523c09e21fe0a874f8c185ff67f853d4",
                    "json", "1", String.valueOf(params.requestedLoadSize), "1").enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(@NonNull Call<FlickrResponse> call, @NonNull Response<FlickrResponse> response) {
                    if (response.isSuccessful()){
                        FlickrResponse flickrResponse = response.body();
                        if (flickrResponse != null){
                            callback.onResult(flickrResponse.getPhotos().getPhoto(), flickrResponse.getPhotos().getPage(), flickrResponse.getPhotos().getPage() + 1);
                        }
                    }else {
                        Log.e("Data retrieval failed", "networker callback failed");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FlickrResponse> call, @NonNull Throwable t) {

                }
            });
        }else {
            api.getPhotosFromUser("flickr.people.getPhotosOf", userId, "523c09e21fe0a874f8c185ff67f853d4",
                    "json", "1", String.valueOf(params.requestedLoadSize), "1").enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(@NonNull Call<FlickrResponse> call, @NonNull Response<FlickrResponse> response) {
                    if (response.isSuccessful()){
                        FlickrResponse flickrResponse = response.body();
                        if (flickrResponse != null){
                            pages = flickrResponse.getPhotos().getPages();
                            //responses.setValue(response.body().getPhotos().getPhoto());
                            callback.onResult(flickrResponse.getPhotos().getPhoto(), flickrResponse.getPhotos().getPage(), flickrResponse.getPhotos().getPage() + 1);
                        }
                    }else {
                        Log.e("Data retrieval failed", "networker callback failed");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FlickrResponse> call, @NonNull Throwable t) {

                }
            });
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {}

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        if (userId == null){
            api.getInterestingPhotosByDateAndPage("flickr.interestingness.getList", getYesterdaysDate(), "523c09e21fe0a874f8c185ff67f853d4",
                    "json", "1", String.valueOf(params.requestedLoadSize), String.valueOf(params.key)).enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(@NonNull Call<FlickrResponse> call, @NonNull Response<FlickrResponse> response) {
                    if (response.isSuccessful()) {
                        FlickrResponse flickrResponse = response.body();
                        pages = flickrResponse.getPhotos().getPages();
                            if (params.key >= pages){
                                callback.onResult(flickrResponse.getPhotos().getPhoto(), null);
                            }else {
                                callback.onResult(flickrResponse.getPhotos().getPhoto(), flickrResponse.getPhotos().getPage() + 1);
                            }
                    } else {
                        Log.e("Data retrieval failed", "networker callback failed");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FlickrResponse> call, @NonNull Throwable t) {
                }
            });
        }else {
            api.getPhotosFromUser("flickr.people.getPhotosOf", userId, "523c09e21fe0a874f8c185ff67f853d4",
                    "json", "1", String.valueOf(params.requestedLoadSize), String.valueOf(params.key)).enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(@NonNull Call<FlickrResponse> call, @NonNull Response<FlickrResponse> response) {
                    if (response.isSuccessful()) {
                        FlickrResponse flickrResponse = response.body();

                        if (flickrResponse != null){
                            if (params.key >= pages){
                                callback.onResult(flickrResponse.getPhotos().getPhoto(), null);
                            }else {
                                callback.onResult(flickrResponse.getPhotos().getPhoto(), flickrResponse.getPhotos().getPage() + 1);
                            }
                        }
                    } else {
                        Log.e("Data retrieval failed", "networker callback failed");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FlickrResponse> call, @NonNull Throwable t) {
                }
            });
        }
    }
}
