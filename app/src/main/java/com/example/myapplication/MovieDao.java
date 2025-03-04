// app/src/main/java/com/example/myapplication/dao/MovieDao.java
package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.myapplication.entities.Movie;
import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insertAll(List<Movie> movies);

    @Query("SELECT * FROM movies LIMIT :limit OFFSET :offset")
    LiveData<List<Movie>> getMoviesByPage(int limit, int offset);
}