package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Movie implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("category")
    private List<String> categoryIds; // List of category IDs

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("posterUrl")
    private String posterUrl;

    @SerializedName("director")
    private String director;

    @SerializedName("watchedBy")
    private List<String> watchedBy; // List of user IDs

    @SerializedName("Comments")
    private List<String> comments; // List of comments

    @SerializedName("promoted")
    private boolean promoted;

    @SerializedName("watchedAt")
    private String watchedAt;

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getCategories() {
        return categoryIds != null ? categoryIds : new ArrayList<>();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getDirector() {
        return director;
    }

    public List<String> getWatchedBy() {
        return watchedBy != null ? watchedBy : new ArrayList<>();
    }

    public List<String> getComments() {
        return comments != null ? comments : new ArrayList<>();
    }

    public boolean isPromoted() {
        return promoted;
    }

    public String getWatchedAt() {
        return watchedAt;
    }

    // Optional: Override toString() for debugging
    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", categoryIds=" + categoryIds +
                ", videoUrl='" + videoUrl + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", director='" + director + '\'' +
                ", watchedBy=" + watchedBy +
                ", comments=" + comments +
                ", promoted=" + promoted +
                ", watchedAt='" + watchedAt + '\'' +
                '}';
    }

}