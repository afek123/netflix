package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MoviesAdapter;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMoviesActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener {

    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movies = new ArrayList<>();
    private ApiService apiService;
    private FloatingActionButton btnAddMovie;

    private static final int ADD_MOVIE_REQUEST_CODE = 1; // Request code for adding a movie
    private static final int UPDATE_MOVIE_REQUEST_CODE = 2; // Request code for updating a movie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_movies);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        moviesAdapter = new MoviesAdapter(movies, this);
        recyclerView.setAdapter(moviesAdapter);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMoviesActivity.this, AddMovieActivity.class);
            startActivityForResult(intent, ADD_MOVIE_REQUEST_CODE); // Use startActivityForResult to get result
        });

        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();  // Refresh movie list when the activity resumes
    }

    private void loadMovies() {
        Call<List<Movie>> call = apiService.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movies.clear();
                    movies.addAll(response.body());
                    moviesAdapter.notifyDataSetChanged();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
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
    public void onUpdateClick(Movie movie) {
        // Log to verify it's triggered
        Log.d("ManageMoviesActivity", "Navigating to UpdateMovieActivity with movie: " + movie.getTitle());

        // Navigate to UpdateMovieActivity with the movie data
        Intent intent = new Intent(ManageMoviesActivity.this, UpdateMovieActivity.class);
        intent.putExtra("movie", movie); // Pass the movie object to the update activity
        startActivityForResult(intent, UPDATE_MOVIE_REQUEST_CODE);
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
