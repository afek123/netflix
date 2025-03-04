package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private OnMovieClickListener listener;
    private ApiService apiService;

    // Constructor
    public MovieAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
        this.apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService.class); // Pass context
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);

        // Set click listener for the item view (for updating)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.d("MoviesAdapter", "Update clicked for movie: " + movie.getTitle());
                listener.onUpdateClick(movie); // Trigger update on normal click
            }
        });

        // Set long click listener for deletion
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                Log.d("MoviesAdapter", "Delete clicked for movie: " + movie.getTitle());
                listener.onDeleteClick(movie); // Trigger delete on long click
            }
            return true; // Consume the long-click event
        });
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    // ViewHolder class
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;
        private TextView movieTitle, movieDirector, movieCategory;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDirector = itemView.findViewById(R.id.movieDirector);
            movieCategory = itemView.findViewById(R.id.movieCategory);
        }

        public void bind(Movie movie) {
            // Load movie image using Glide
            String fullImageUrl = "http://10.0.2.2:5000" + movie.getPosterUrl();
            Glide.with(itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(movieImage);

            // Set movie details
            movieTitle.setText(movie.getTitle());
            movieDirector.setText("Director: " + movie.getDirector());

            // Fetch categories and map category IDs to names
            fetchCategoriesAndMapToMovie(movie);
        }

        private void fetchCategoriesAndMapToMovie(Movie movie) {
            ApiService apiService = RetrofitClient.getRetrofitInstance(itemView.getContext()).create(ApiService.class);
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
                        movieCategory.setText("Categories: " + categoryNames.toString());
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

    // Listener interface for movie update and delete
    public interface OnMovieClickListener {
        void onUpdateClick(Movie movie); // For updating the movie
        void onDeleteClick(Movie movie); // For deleting the movie
    }
}