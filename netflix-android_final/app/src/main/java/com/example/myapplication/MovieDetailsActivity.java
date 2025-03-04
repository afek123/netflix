package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.adapters.CategoryMoviesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FloatingActionButton toggleThemeButton;

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String THEME_KEY = "isDarkMode";
    private VideoView movieVideoView;
    private Movie movie;
    private Button playMovieButton;
    private ApiService apiService;
    private FrameLayout fullScreenContainer;
    private RecyclerView recommendedMoviesRecyclerView;
    private CategoryMoviesAdapter recommendedMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        setAppTheme(isDarkMode);
        movieVideoView = new VideoView(this);
        movieVideoView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        fullScreenContainer = new FrameLayout(this);
        fullScreenContainer.addView(movieVideoView);
        fullScreenContainer.setVisibility(View.GONE);

        addContentView(fullScreenContainer, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        playMovieButton = findViewById(R.id.playMovieButton);
        recommendedMoviesRecyclerView = findViewById(R.id.recommendedMoviesRecyclerView);
        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        apiService = RetrofitClient.getClient().create(ApiService.class);
        toggleThemeButton = findViewById(R.id.toggleThemeButton);

        // Set the initial icon for the toggle theme button
        updateToggleThemeButtonIcon(isDarkMode);
        // Set click listener for the Toggle Theme button
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });
        movie = (Movie) getIntent().getSerializableExtra("movie");
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        if (movie != null) {
            fetchMovieDetails(movie.getId());
            fetchRecommendedMovies(movie.getId(),userId);
            playMovieButton.setOnClickListener(v -> {
                addToWatchlist(movie.getId(),userId);
                playMovie(movie);
            });
        } else {
            Log.e("MovieDetailsActivity", "Movie ID is missing");
            Toast.makeText(this, "Movie data is not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateUI() {
        if (movie != null) {
            TextView movieNameTextView = findViewById(R.id.movieName);
            TextView directorTextView = findViewById(R.id.director);
            TextView categoryTextView = findViewById(R.id.category);
            ImageView moviePosterImageView = findViewById(R.id.moviePoster);

            movieNameTextView.setText(movie.getTitle());
            directorTextView.setText(movie.getDirector());

            // Load movie poster
            Glide.with(this)
                    .load(getString(R.string.ip_address) + movie.getPosterUrl())
                    .into(moviePosterImageView);
            ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
            apiService.getCategories().enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Category> categories = response.body();
                        // Map category IDs to category names
                        StringBuilder categoryNames = new StringBuilder();
                        for (String categoryId : movie.getCategoryIds()) {
                            for (Category category : categories) {
                                if (category.getId().equals(categoryId)) {
                                    categoryNames.append(category.getName()).append(", ");
                                    break;
                                }
                            }
                        }
                        if (categoryNames.length() > 0) {
                            categoryNames.setLength(categoryNames.length() - 2); // Remove the last ", "
                        }
                        categoryTextView.setText("Categories: " + categoryNames.toString());
                    } else {
                        Log.e("MoviesAdapter", "Failed to fetch categories");
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    Log.e("MoviesAdapter", "Failed to fetch categories", t);
                }
            });
        }


    }


    private void fetchMovieDetails(String movieId) {
        apiService.getMovie(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie = response.body();
                    Log.d("MovieDetailsActivity", "Movie details loaded successfully");
                    updateUI();
                } else {
                    Log.e("MovieDetailsActivity", "Failed to load movie details, Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MovieDetailsActivity", "Error loading movie details", t);
            }
        });
    }

    private void fetchRecommendedMovies(String movieId,String userId) {
        apiService.getRecommendedMovies(movieId,userId).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> recommendedMovies = response.body();
                    recommendedMoviesAdapter = new CategoryMoviesAdapter(MovieDetailsActivity.this, recommendedMovies, movie -> {
                        Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                        intent.putExtra("movie", movie);
                        startActivity(intent);
                    });
                    recommendedMoviesRecyclerView.setAdapter(recommendedMoviesAdapter);
                    Log.d("MovieDetailsActivity", "Recommended movies loaded successfully");
                } else {
                    Log.e("MovieDetailsActivity", "Failed to load recommended movies, Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("MovieDetailsActivity", "Error loading recommended movies", t);
            }
        });
    }

    private void playMovie(Movie movie) {
        if (movie == null) {
            Log.e("MovieDetailsActivity", "Movie data is not loaded yet");
            return;
        }

        fullScreenContainer.setVisibility(View.VISIBLE);
        playMovieButton.setVisibility(View.GONE);

        String videoUrl = getString(R.string.ip_address) + movie.getVideoUrl();
        Log.d("MovieDetailsActivity", "Playing movie from: " + videoUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoUrl), "video/*");
        intent.putExtra("force_fullscreen", true);
        startActivity(intent);
    }
    private void addToWatchlist(String movieId,String userId) {


        if (userId == null) {
            Log.e("MovieDetailsActivity", "User ID is missing!");
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.addMovieToWatched(userId, movieId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MovieDetailsActivity", "Movie added to watchlist successfully");
                } else {
                    Log.e("MovieDetailsActivity", "Failed to add movie to watchlist, Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MovieDetailsActivity", "Error adding movie to watchlist", t);
            }
        });
    }
    private void setAppTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void updateToggleThemeButtonIcon(boolean isDarkMode) {
        if (isDarkMode) {
            toggleThemeButton.setImageResource(R.drawable.ic_baseline_light_mode_24); // Light mode icon
        } else {
            toggleThemeButton.setImageResource(R.drawable.ic_baseline_dark_mode_24); // Dark mode icon
        }
    }
}


