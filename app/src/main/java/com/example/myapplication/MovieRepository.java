// app/src/main/java/com/example/myapplication/MovieRepository.java
package com.example.myapplication;

import android.content.Context;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.MovieDao;
import com.example.myapplication.entities.Movie;
import androidx.lifecycle.LiveData;
import java.util.List;
import android.util.Log; // Added import statement

public class MovieRepository {
    private MovieDao movieDao;
    private ApiService apiService;

    public MovieRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        movieDao = db.movieDao();
        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService.class);
    }

   // Insert movies into the database
   public void insertAll(List<Movie> movies) {
    new Thread(() -> {
        movieDao.insertAll(movies);
        // Log the insertion
        for (Movie movie : movies) {
            Log.d("MovieRepository", "Inserted movie: " + movie.getTitle());
        }
    }).start();
}


    // Fetch movies with pagination from the database
    public LiveData<List<Movie>> getMoviesByPage(int limit, int offset) {
        return movieDao.getMoviesByPage(limit, offset);
    }
}