package com.example.wheneverapp.api;

import com.example.wheneverapp.Model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ModelService {
    @GET("/models")
    Call<List<Model>> getAllModels();

    @GET("/models/{id}")
    Call<Model> getModelById(@Path("id") String id);
}
