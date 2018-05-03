package com.example.twomack.flickrapp.networking;

import com.example.twomack.flickrapp.data.FlickrResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {


    @GET("rest/")
    Call<FlickrResponse> getInterestingPhotosByDateAndPage(@Query("method") String method, @Query("date") String date,
                                                    @Query("api_key") String apiKey, @Query("format") String format,
                                                    @Query("nojsoncallback") String jsonCallback, @Query("per_page") String perPage, @Query("page") String page);

    @GET("rest/")
    Call<FlickrResponse> getPhotosFromUser(@Query("method") String method, @Query("user_id") String user_id,
                                                          @Query("api_key") String apiKey, @Query("format") String format,
                                                          @Query("nojsoncallback") String jsonCallback, @Query("per_page") String perPage, @Query("page") String page);

}
