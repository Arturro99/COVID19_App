package com.mobilki.covidapp.api.model;

import java.util.List;

public class Film {
    private String id;
    private String title;
    private int yearOfRelease;
    private String description;
    private float ratings;
    private List<String> genres;
    private String imageUrl;
    private List<Actor> actors;

    public Film(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public String getDescription() {
        return description;
    }

    public float getRatings() {
        return ratings;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
