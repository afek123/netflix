package com.example.myapplication.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("_id")
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

    @SerializedName("watchedBy")
    private List<String> watchedBy; // List of User IDs

    @SerializedName("categories")
    private List<String> categoryIds; // Stores category IDs from API

    // Constructor
    public Movie(String id, String title, String description, String publisher, String director, String videoUrl, String posterUrl, boolean promoted, List<String> watchedBy, List<String> categoryIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.director = director;
        this.videoUrl = videoUrl;
        this.posterUrl = posterUrl;
        this.promoted = promoted;
        this.watchedBy = watchedBy;
        this.categoryIds = categoryIds;
    }

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

    public List<String> getWatchedBy() {
        return watchedBy;
    }

    public void setWatchedBy(List<String> watchedBy) {
        this.watchedBy = watchedBy;
    }

    public List<String> getCategoryIds() {
        return categoryIds != null ? categoryIds : new ArrayList<>();
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    // New method to flatten the watchedBy list (if it's a list of lists)
    public List<String> getFlattenedWatchedBy() {
        List<String> flattenedList = new ArrayList<>();
        if (watchedBy != null) {
            for (String userId : watchedBy) {
                flattenedList.add(userId);
            }
        }
        return flattenedList;
    }

    // Optional: Override toString() for better logging
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
