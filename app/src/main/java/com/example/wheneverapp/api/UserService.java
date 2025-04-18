package com.example.wheneverapp.api;

import com.example.wheneverapp.Model.LoginResponse;
import com.example.wheneverapp.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface UserService {
    @POST("/auth/login/v2")
    Call<LoginResponse> login(@Body User loginRequest);

    @POST("/auth/logout")
    Call<User> logout();
}
