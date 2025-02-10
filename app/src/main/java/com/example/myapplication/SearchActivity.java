package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {
    private EditText searchQueryEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> searchResults = new ArrayList<>();
    private ApiService apiService;
    private FloatingActionButton toggleThemeButton; // Toggle Theme button

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String THEME_KEY = "isDarkMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the theme based on user preference
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        setAppTheme(isDarkMode);

        setContentView(R.layout.activity_search);

        // Initialize views
        searchQueryEditText = findViewById(R.id.searchQueryEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        toggleThemeButton = findViewById(R.id.toggleThemeButton); // Initialize Toggle Theme button

        // Set up RecyclerView
        moviesAdapter = new MoviesAdapter(searchResults, this);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(moviesAdapter);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Set click listener for the search button
        searchButton.setOnClickListener(v -> handleSearch());

        // Set click listener for the Toggle Theme button
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });
    }

    private void setAppTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void handleSearch() {
        String query = searchQueryEditText.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the API to search for movies
        Call<List<Movie>> call = apiService.searchMovies(query);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.clear();
                    searchResults.addAll(response.body());
                    moviesAdapter.notifyDataSetChanged(); // Update the RecyclerView
                } else {
                    Toast.makeText(SearchActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    Log.e("SearchActivity", "Failed to search movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SearchActivity", "Failed to search movies", t);
            }
        });
    }

    @Override
    public void onUpdateClick(Movie movie) {
        // Handle update click (e.g., navigate to UpdateMovieActivity)
        Log.d("SearchActivity", "Update clicked for movie: " + movie.getTitle());
    }

    @Override
    public void onDeleteClick(Movie movie) {
        // Handle delete click (e.g., delete the movie)
        Log.d("SearchActivity", "Delete clicked for movie: " + movie.getTitle());
    }
}