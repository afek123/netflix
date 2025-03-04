package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Insert
    void insert(Post... post);

    @Query("SELECT * FROM Post")
    List<Post> index();

    @Query("SELECT * FROM Post WHERE id = :id")
    Post get(int id);

    @Update
    void update(Post... post);

    @Delete
    void delete(Post... post);
}