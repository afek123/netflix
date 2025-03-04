package com.example.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "movies") // Define the table name
public class Movie implements Serializable {

    @PrimaryKey // Mark the ID as the primary key
    @SerializedName("_id")
    @NonNull
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("director")
    private String director;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("posterUrl")
    private String posterUrl;

    @SerializedName("promoted")
    private boolean promoted;

    // Use TypeConverters to handle complex types like List<List<String>>
    @SerializedName("watchedBy")
    private List<List<String>> watchedBy;

    // Use TypeConverters to handle List<String>
    @SerializedName("category")
    private List<String> categoryIds;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public List<List<String>> getWatchedBy() {
        return watchedBy != null ? watchedBy : new ArrayList<>();
    }

    public void setWatchedBy(List<List<String>> watchedBy) {
        this.watchedBy = watchedBy;
    }

    public List<String> getCategoryIds() {
        return categoryIds != null ? categoryIds : new ArrayList<>();
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    // Flatten the watchedBy list
    public List<String> getFlattenedWatchedBy() {
        List<String> flattenedList = new ArrayList<>();
        if (watchedBy != null) {
            for (List<String> innerList : watchedBy) {
                if (innerList != null) {
                    flattenedList.addAll(innerList);
                }
            }
        }
        return flattenedList;
    }

    // toString() for logging
    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publisher='" + publisher + '\'' +
                ", director='" + director + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", promoted=" + promoted +
                ", watchedBy=" + watchedBy +
                ", categoryIds=" + categoryIds +
                '}';
    }
}