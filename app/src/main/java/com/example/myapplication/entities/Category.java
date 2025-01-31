package com.example.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int room_id; // Local primary key for Room

    @NonNull
    @SerializedName("_id")  // If your API uses `_id`, change here
    private String id; // MongoDB ID

    private String name;
    private boolean promoted;

    public Category(@NonNull String id, String name, boolean promoted) {
        this.id = id;
        this.name = name;
        this.promoted = promoted;
    }

    @Ignore
    public Category(String name, boolean promoted) {
        this.id = ""; // You can update this later when fetching from MongoDB
        this.name = name;
        this.promoted = promoted;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
}
