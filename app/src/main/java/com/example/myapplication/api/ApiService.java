package com.example.myapplication.api;

import com.example.myapplication.Token;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.entities.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // User Routes
    // User Routes
    @Multipart
    @POST("/api/users")
    Call<ResponseBody> registerUser(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("name") RequestBody displayName,
            @Part MultipartBody.Part picture
    );

    @POST("/api/login")
    Call<Token> loginUser(@Body User user);

    @GET("/api/users/{id}")
    Call<User> getUserDetails(@Path("id") String userId, @Header("Authorization") String token);

    @POST("/api/movies/{userId}/{movieId}/recommend")
    Call<Void> addMovieToWatched(@Path("userId") String userId, @Path("movieId") String movieId);
    @GET("/api/movies/{userId}/{id}/recommend")
    Call<List<Movie>> getRecommendedMovies(@Path("id") String movieId,@Path("userId") String userId);
    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") String userId, @Header("Authorization") String token);

    // Movie Routes
    @GET("/api/movies")
    Call<List<Movie>> getMovies();

    @POST("/api/movies")
    @Multipart
    Call<Movie> createMovie(
            @Part("title") RequestBody title,
            @Part("director") RequestBody director,
            @Part("category") RequestBody categories,  // Ensure this is a valid ObjectId string
            @Part MultipartBody.Part videoFile,     // Correct field name
            @Part MultipartBody.Part posterFile     // Correct field name
    );


    @GET("/api/movies/{id}")
    Call<Movie> getMovie(@Path("id") String movieId);

    @PUT("/api/movies/{id}") // Change from PATCH to PUT
    @Multipart
    Call<Movie> updateMovie(
            @Path("id") String movieId,
            @Part("title") RequestBody title,
            @Part("director") RequestBody director,
            @Part("category") RequestBody category,
            @Part MultipartBody.Part videoFile,
            @Part MultipartBody.Part posterFile
    );
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

    @PATCH("/api/categories/{id}")
    Call<Void> updateCategory(@Path("id") String categoryId, @Body Category updatedCategory);

    @DELETE("/api/categories/{id}")
    Call<Void> deleteCategory(@Path("id") String categoryId);

}
