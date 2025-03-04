package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entities.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category... categories);

    @Query("SELECT * FROM categories")  // ✅ Use lowercase table name
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id = :id")
    Category getById(String id);


    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}