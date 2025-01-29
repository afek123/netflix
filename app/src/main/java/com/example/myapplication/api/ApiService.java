package com.example.myapplication.api;

import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.entities.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // User Routes
    @POST("/api/users")
    Call<User> registerUser(@Body User user);

    @POST("/api/tokens")
    Call<User> loginUser(@Body User user);

    @GET("/api/users/{id}")
    Call<User> getUserDetails(@Path("id") String userId);

    @POST("/api/add-movie-to-watched")
    Call<Void> addMovieToWatched(@Body Movie movie);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") String userId, @Header("Authorization") String token);

    // Movie Routes
    @GET("/api/movies")
    Call<List<Movie>> getMovies();

    @POST("/api/movies")
    @Multipart
    Call<Movie> createMovie(
            @Part MultipartBody.Part movieFile,
            @Part MultipartBody.Part thumbnailFile,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("publisher") RequestBody publisher
    );

    @GET("/api/movies/{id}")
    Call<Movie> getMovie(@Path("id") String movieId);

    @PUT("/api/movies/{id}")
    Call<Movie> updateMovie(@Path("id") String movieId, @Body Movie movie);

    @DELETE("/api/movies/{id}")
    Call<Void> deleteMovie(@Path("id") String movieId);

    @GET("/api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Path("query") String query);

    // Category Routes
    @GET("/api/categories")
    Call<List<Category>> getCategories();

    @POST("/api/categories")
    Call<Void> createCategory(@Body Category category);

    @GET("/api/categories/{id}")
    Call<Category> getCategory(@Path("id") String categoryId);

    @PUT("/api/categories/{id}")
    Call<Category> updateCategory(@Path("id") String categoryId, @Body Category category);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Path("id") String categoryId);

}
