package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.entities.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMoviesActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {
    private ActivityResultLauncher<Intent> addMovieLauncher; // Declare ActivityResultLauncher
    private ActivityResultLauncher<Intent> updateMovieLauncher; // Declare ActivityResultLauncher for updating movies

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movies = new ArrayList<>();
    private ApiService apiService;
    private FloatingActionButton btnAddMovie;
    private Map<String, String> categoryMap = new HashMap<>();
    private static final int ADD_MOVIE_REQUEST_CODE = 1; // Request code for adding a movie
    private static final int UPDATE_MOVIE_REQUEST_CODE = 2; // Request code for updating a movie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_movies);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// Define the list of categories (it can be empty or populated as needed)
        List<Category> categories = new ArrayList<>(); // or populate it based on the loaded categories

// Update the MoviesAdapter constructor call
        moviesAdapter = new MoviesAdapter(movies, categories, this);  // Pass the necessary arguments

        recyclerView.setAdapter(moviesAdapter);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoviesActivity.this, AddMovieActivity.class);
            addMovieLauncher.launch(intent); // Launch using the ActivityResultLauncher
        });

        // Initialize the ActivityResultLauncher for adding a movie
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
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Movie updatedMovie = (Movie) result.getData().getSerializableExtra("updatedMovie");
                            if (updatedMovie != null) {
                                updateMovieInList(updatedMovie); // Update the movie in the list
                            }
                        }
                    }
                });

        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();  // Refresh movie list when the activity resumes
    }

    private void loadMovies() {
        apiService.getMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movies.clear(); // Clear existing movies list
                    loadCategories(); // Load categories before loading movies
                    for (Movie movie : response.body()) {
                        // Map category IDs to category names
                        List<String> categoryIds = movie.getCategoryIds();
                        List<Category> categoryObjects = new ArrayList<>();
                        if(categoryIds.isEmpty()){
                            Log.d("MainActivitys", "null");
                        }
                        for (String categoryId : categoryIds) {
                            Log.d("MainActivitys", "in");
                            String categoryName = categoryMap.get(categoryId); // Get category name from the map
                            boolean isPromoted = false;  // Default to false, or use actual value if available
                            Log.d("MainActivitys", categoryName);
                            if (categoryName != null) {
                                Log.d("MainActivitys", "hell");
                                categoryObjects.add(new Category(categoryId, categoryName, isPromoted)); // Create a Category object
                            } else {
                                categoryObjects.add(new Category(categoryId, "Unknown", isPromoted)); // If category is not found
                            }
                        }

                        // Extract category names as a list of strings
                        List<String> categoryNames = new ArrayList<>();
                        for (Category category : categoryObjects) {
                            categoryNames.add(category.getName());
                        }

                        // Set the movie categories (list of category names as strings)
                        movie.setCategoryIds(categoryNames.isEmpty() ? List.of("Unknown") : categoryNames);

                        // Handle watchedBy nested list
                        List<String> flattenedWatchedBy = movie.getFlattenedWatchedBy();
                        Log.d("MovieLoader", "Movie: " + movie.getTitle() + " Watched by: " + flattenedWatchedBy);

                        movies.add(movie);
                    }

                    moviesAdapter.notifyDataSetChanged();  // Notify the adapter that the movie data has changed
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




    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Create a map of category IDs to category names
                    for (Category category : response.body()) {
                        categoryMap.put(category.getId(), category.getName());
                    }
                } else {
                    Log.e("ManageMoviesActivity", "Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("ManageMoviesActivity", "Failed to load categories", t);
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
                    loadMovies();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            loadMovies();  // Refresh the movie list after adding a new movie
        } else if (requestCode == UPDATE_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Movie updatedMovie = (Movie) data.getSerializableExtra("updatedMovie");
                if (updatedMovie != null) {
                    updateMovieInList(updatedMovie); // Update the movie in the list
                }
            }
        }
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
