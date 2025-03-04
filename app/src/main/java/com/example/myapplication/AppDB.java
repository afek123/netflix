// app/src/main/java/com/example/myapplication/AppDB.java
package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.CategoryDao;
import com.example.myapplication.MovieDao;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.Post;
import com.example.myapplication.entities.Category;
import com.example.myapplication.Converters;

@Database(entities = {Movie.class, Post.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;

    public abstract MovieDao movieDao();
    public abstract PostDao postDao();
    public abstract CategoryDao categoryDao();

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "FooDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}