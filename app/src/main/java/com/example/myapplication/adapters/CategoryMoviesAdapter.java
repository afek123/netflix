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
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.mainPage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryMoviesAdapter extends RecyclerView.Adapter<CategoryMoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;
    private MoviesAdapter.OnMovieClickListener listener;

    public CategoryMoviesAdapter(Context context, List<Movie> movies, MoviesAdapter.OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;
        private TextView movieTitle, movieDirector, movieCategory;

        public MovieViewHolder(@NonNull View itemView, MoviesAdapter.OnMovieClickListener listener) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDirector = itemView.findViewById(R.id.movieDirector);
            movieCategory = itemView.findViewById(R.id.movieCategory);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onMovieClick((Movie) itemView.getTag());
                }
            });
        }

        public void bind(Movie movie) {
            itemView.setTag(movie);
            String fullImageUrl = "http://10.0.2.2:5000" + movie.getPosterUrl();
            Glide.with(itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(movieImage);

            movieTitle.setText(movie.getTitle());
            movieDirector.setText("Director: " + movie.getDirector());
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
}