package com.example.myapplication.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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
    private List<Category> categories;  // List of Category objects (or Category IDs)

    // Constructor
    public Movie(String id, String title, String description, String publisher, String director, String videoUrl, String posterUrl, boolean promoted, List<String> watchedBy, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.director = director;
        this.videoUrl = videoUrl;
        this.posterUrl = posterUrl;
        this.promoted = promoted;
        this.watchedBy = watchedBy;
        this.categories = categories;  // Initialize the categories field
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

    public List<Category> getCategories() {
        return categories;  // Getter for categories field
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;  // Setter for categories field
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
                ", categories=" + categories +  // Log the categories field
                '}';
    }

    public Category getCategory() {
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0);  // Return the first category in the list
        }
        return null;  // Return null if no categories are assigned
    }

}
