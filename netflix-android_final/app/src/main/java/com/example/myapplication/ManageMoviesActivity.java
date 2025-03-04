package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.entities.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMoviesActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private ActivityResultLauncher<Intent> addMovieLauncher;
    private ActivityResultLauncher<Intent> updateMovieLauncher;
    private RecyclerView recyclerView;
    private MovieAdapter moviesAdapter;
    private List<Movie> movies = new ArrayList<>();
    private ApiService apiService;
    private FloatingActionButton btnAddMovie;
    private FloatingActionButton toggleThemeButton; // Toggle Theme button
    private Map<String, String> categoryMap = new HashMap<>();
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

        setContentView(R.layout.activity_manage_movies);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        btnAddMovie = findViewById(R.id.btnAddMovie);
        toggleThemeButton = findViewById(R.id.toggleThemeButton); // Initialize Toggle Theme button

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MovieAdapter(this,movies, this);
        recyclerView.setAdapter(moviesAdapter);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        updateToggleThemeButtonIcon(isDarkMode);

        // Set click listeners
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoviesActivity.this, AddMovieActivity.class);
            addMovieLauncher.launch(intent);
        });

        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            updateToggleThemeButtonIcon(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });

        // Initialize ActivityResultLaunchers
        addMovieLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadMovies(); // Refresh movie list after adding a new movie
                    }
                });

        updateMovieLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Movie updatedMovie = (Movie) result.getData().getSerializableExtra("updatedMovie");
                        if (updatedMovie != null) {
                            updateMovieInList(updatedMovie); // Update the movie in the list
                        }
                    }
                });

        loadMovies(); // Load movies when the activity is created
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

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Refresh movie list when the activity resumes
    }

    private void loadMovies() {
        apiService.getMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movies.clear(); // Clear existing movies list
                    movies.addAll(response.body());
                    moviesAdapter.notifyDataSetChanged(); // Notify the adapter that the movie data has changed
                } else {
                    Log.e("ManageMoviesActivity", "Failed to load movies");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("ManageMoviesActivity", "Failed to load movies", t);
            }
        });
    }

    @Override
    public void onUpdateClick(Movie movie) {
        Log.d("ManageMoviesActivity", "Navigating to UpdateMovieActivity with movie: " + movie.getTitle());

        // Use the ActivityResultLauncher to start the activity
        Intent intent = new Intent(ManageMoviesActivity.this, UpdateMovieActivity.class);
        intent.putExtra("movie", movie); // Pass the movie object
        updateMovieLauncher.launch(intent); // Launch the intent using the new API
    }

    @Override
    public void onDeleteClick(Movie movie) {
        deleteMovie(movie.getId());
    }

    private void deleteMovie(String movieId) {
        Call<Void> call = apiService.deleteMovie(movieId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageMoviesActivity.this, "Movie deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadMovies(); // Refresh the movie list after deletion
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

    private void updateMovieInList(Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(updatedMovie.getId())) {
                movies.set(i, updatedMovie); // Replace the old movie with the updated one
                moviesAdapter.notifyItemChanged(i); // Notify the adapter of the change
                break;
            }
        }
    }
}