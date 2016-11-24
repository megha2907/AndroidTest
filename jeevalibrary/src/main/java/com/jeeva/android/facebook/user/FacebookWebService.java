package com.jeeva.android.facebook.user;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jeeva on 14/3/16.
 */
public interface FacebookWebService {

    @GET("me")
    Call<FacebookProfile> getProfile(
            @Query("fields") String fields,
            @Query("access_token") String accessToken
    );
}