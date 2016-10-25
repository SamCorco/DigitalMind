package com.jemsam.digitalmind.service;

import com.jemsam.digitalmind.model.LoginResponse;
import com.jemsam.digitalmind.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jeremy.toussaint on 25/10/16.
 */

public interface LoginAPiInterface {

    @POST("register")
    Call<LoginResponse> createUser(@Body User user);

    @GET("connect")
    Call<LoginResponse> connectUser();


}
