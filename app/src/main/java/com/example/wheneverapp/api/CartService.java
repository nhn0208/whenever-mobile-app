package com.example.wheneverapp.api;

import com.example.wheneverapp.Model.Cart;
import com.example.wheneverapp.Model.CartRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartService {
    // get all cart by customerId
    @GET("/cart/v2/{customerId}")
    Call<List<Cart>> getAllCartByCustomerId(@Path("customerId") String customerId);

    // post: add new product into cart
    @POST("/cart/add")
    Call<Cart> addProductToCart(@Body CartRequest cartRequest);

    // increase
    @PATCH("/cart/increase/{id}")
    Call<Cart> increaseProductInCart(@Path("id") String productId);

    // decrease
    @PATCH("/cart/decrease/{id}")
    Call<Cart> decreaseProductInCart(@Path("id") String productId);

    // delete
    @DELETE("/cart/delete/{id}")
    Call<Cart> deleteProductInCart(@Path("id") String productId);
}
