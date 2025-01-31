package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMoviesActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movies = new ArrayList<>(); // Initialize the list
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_movies);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with the initialized list
        moviesAdapter = new MoviesAdapter(movies, this); // Pass the initialized list
        recyclerView.setAdapter(moviesAdapter);

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Load movies from the API
        loadMovies();
    }

    private void loadMovies() {
        Call<List<Movie>> call = apiService.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the adapter with the fetched movies
                    movies.clear();
                    movies.addAll(response.body());
                    moviesAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
                } else {
                    // Handle API error
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle network failure
                showError(t);
            }
        });
    }

    private void handleError(Response<List<Movie>> response) {
        try {
            String errorBody = response.errorBody().string();
            Toast.makeText(this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing error response!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(Throwable t) {
        t.printStackTrace();
        Toast.makeText(this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Handle movie item clicks
        deleteMovie(movie.getId());    }
    private void deleteMovie(String movieId) {
        Call<Void> call = apiService.deleteMovie(movieId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageMoviesActivity.this, "Movie deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadMovies(); // Refresh the list
                } else {
                    Toast.makeText(ManageMoviesActivity.this, "Failed to delete movie!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageMoviesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
}
}