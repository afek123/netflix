package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private OnMovieClickListener listener;

    // Constructor
    public MoviesAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
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

        // Set click listener for the item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
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
            String fullImageUrl = "http://10.0.2.2:5000" + movie.getPosterUrl(); // Replace with your server's IP or domain
            Glide.with(itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
                    .error(R.drawable.error) // Error image if loading fails
                    .into(movieImage);

            // Set movie details
            movieTitle.setText(movie.getTitle());
            movieDirector.setText("Director: " + movie.getDirector());
            movieCategory.setText("Category: " + movie.getCategory());
        }
    }

    // Listener interface
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}