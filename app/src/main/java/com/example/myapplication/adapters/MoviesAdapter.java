package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;

import java.util.List;
import java.util.Map;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CategoryViewHolder> {
    private Map<String, List<Movie>> moviesByCategory;
    private List<String> categoryNames;
    private Context context;
    private OnMovieClickListener listener;

    public MoviesAdapter(Context context, Map<String, List<Movie>> moviesByCategory, List<String> categoryNames, OnMovieClickListener listener) {
        this.context = context;
        this.moviesByCategory = moviesByCategory;
        this.categoryNames = categoryNames;
        this.listener = listener;
    }

    public void setItems(Map<String, List<Movie>> moviesByCategory, List<String> categoryNames) {
        this.moviesByCategory = moviesByCategory;
        this.categoryNames = categoryNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_with_movies, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String categoryName = categoryNames.get(position);
        List<Movie> movies = moviesByCategory.get(categoryName);
        holder.bind(categoryName, movies, listener);
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle;
        private RecyclerView recyclerViewMovies;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            recyclerViewMovies = itemView.findViewById(R.id.recyclerViewMovies);
        }

        public void bind(String categoryName, List<Movie> movies, OnMovieClickListener listener) {
            categoryTitle.setText(categoryName);
            CategoryMoviesAdapter adapter = new CategoryMoviesAdapter(itemView.getContext(), movies, listener);
            recyclerViewMovies.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewMovies.setAdapter(adapter);
        }
    }

    // Interface for handling movie click events
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}