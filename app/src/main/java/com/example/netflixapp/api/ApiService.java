package com.example.netflixapp.api;

import com.example.netflixapp.entities.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // User Routes
        @Multipart
        @POST("/api/users")
        Call<ResponseBody> registerUser(
                @Part("username") RequestBody username,
                @Part("password") RequestBody password,
                @Part("name") RequestBody displayName,
                @Part MultipartBody.Part picture
        );


    @POST("/api/tokens")
    Call<User> loginUser(@Body User user);

    @GET("/api/users/{id}")
    Call<User> getUserDetails(@Path("id") String userId);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") String userId, @Header("Authorization") String token);

    @DELETE("/api/movies/{id}")
    Call<Void> deleteMovie(@Path("id") String movieId);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Path("id") String categoryId);

}
