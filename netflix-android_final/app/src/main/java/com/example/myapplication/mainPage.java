package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mainPage extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {

    private RecyclerView recyclerViewMovies;
    private RecyclerView recyclerViewCategories;
    private MoviesAdapter moviesAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Movie> movies = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private ApiService apiService;
    private boolean isLoading = false;
    private int page = 1;
    private SharedPreferences sharedPreferences, sharedPreferences1;
    private FloatingActionButton toggleThemeButton;


    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String THEME_KEY = "isDarkMode";
    private VideoView randomMovieVideoView;
    private ImageView profileImageView;

    private Button manageMoviesButton;
    private Button searchMoviesButton;
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        setAppTheme(isDarkMode);
        profileImageView = findViewById(R.id.profileImageView);
        manageMoviesButton=findViewById(R.id.manageMoviesButton);
        sharedPreferences1 = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences1.getString("USER_ROLE", "user"); // Default role is "user"
        logoutButton=findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(mainPage.this, MainActivity.class);
            startActivity(intent);
        });
        // Disable button if the user is not a manager
        if (!"manager".equals(userRole)) {
            manageMoviesButton.setEnabled(false);
            manageMoviesButton.setAlpha(0.5f); // Make it visually disabled
        }
        randomMovieVideoView = findViewById(R.id.randomMovieVideoView);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        searchMoviesButton = findViewById(R.id.searchMoviesButton);
        searchMoviesButton.setOnClickListener(view -> { Intent intent = new Intent(mainPage.this, SearchActivity.class);
            startActivity(intent);});
        manageMoviesButton.setOnClickListener(view -> { Intent intent = new Intent(mainPage.this, MainManageActivity.class);
            startActivity(intent);});

        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        toggleThemeButton = findViewById(R.id.toggleThemeButton);
        // On click listener for "Toggle Theme" button
        updateToggleThemeButtonIcon(isDarkMode);
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            updateToggleThemeButtonIcon(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });
        moviesAdapter = new MoviesAdapter(this, new HashMap<>(), new ArrayList<>(), this);
        categoryAdapter = new CategoryAdapter(categories);

        recyclerViewMovies.setAdapter(moviesAdapter);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        fetchUserProfile();
        loadCategories();
        loadMovies();
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainPage", "Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("MainPage", "Failed to load categories", t);
            }
        });
    }
    private void fetchUserProfile() {
        // Assuming the token is stored in SharedPreferences after login
        String token_id = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("user_id", null);
        String token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("jwt_token", null);
        if (token == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("MainPage", "Token: " + token);
        Log.d("MainPage", "Token ID: " + token_id);
        // Make API request to fetch user details
        Call<User> call = apiService.getUserDetails(token_id, token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String profileImageUrl = user.getPicture(); // Ensure this method exists in User.java

                    Log.d("MainPage", "Profile Image URL: " + profileImageUrl); // Log the URL

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        // Load image using Glide
                        Glide.with(mainPage.this)
                                .load(getString(R.string.ip_address) + profileImageUrl)
                                .into(profileImageView);
                    } else {
                        Log.e("MainPage", "Profile image URL is empty or null");
                    }
                } else {
                    Log.e("MainPage", "Failed to load profile, Response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mainPage.this, "Network error!", Toast.LENGTH_SHORT).show();
                Log.e("MainPage", "Error fetching profile: " + t.getMessage());
            }
        });
    }
    private void loadMovies() {
        apiService.getMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movies.clear();
                    movies.addAll(response.body());
                    moviesAdapter = new MoviesAdapter(mainPage.this, groupMoviesByCategories(), new ArrayList<>(groupMoviesByCategories().keySet()), mainPage.this);
                    recyclerViewMovies.setAdapter(moviesAdapter);
                    moviesAdapter.notifyDataSetChanged();
                    playRandomMovie();
                } else {
                    Log.e("MainPage", "Failed to load movies");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("MainPage", "Failed to load movies", t);
            }
        });
    }

    private Map<String, List<Movie>> groupMoviesByCategories() {
        Map<String, List<Movie>> moviesByCategory = new HashMap<>();
        for (Movie movie : movies) {
            for (String categoryId : movie.getCategoryIds()) {
                String categoryName = getCategoryNameById(categoryId);
                if (!moviesByCategory.containsKey(categoryName)) {
                    moviesByCategory.put(categoryName, new ArrayList<>());
                }
                moviesByCategory.get(categoryName).add(movie);
            }
        }
        return moviesByCategory;
    }

    private String getCategoryNameById(String categoryId) {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                return category.getName();
            }
        }
        return "Unknown";
    }



    private void playRandomMovie() {
        if (movies.isEmpty()) {
            Log.e("MainPage", "Movie list is empty, cannot select a random movie");
            return;
        }
        Random random = new Random();
        Movie randomMovie = movies.get(random.nextInt(movies.size()));
        String videoUrl = getString(R.string.ip_address) + randomMovie.getVideoUrl();
        Log.d("playRandomMovie", videoUrl);
        randomMovieVideoView.setVideoURI(Uri.parse(videoUrl));
        MediaController mediaController = new MediaController(this);
        randomMovieVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(randomMovieVideoView);
        randomMovieVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            randomMovieVideoView.start();
        });
    }
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
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
