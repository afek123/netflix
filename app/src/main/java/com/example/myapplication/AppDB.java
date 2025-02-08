package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.entities.Category;

@Database(entities = {Post.class, Category.class}, version = 2, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract CategoryDao categoryDao();
}

