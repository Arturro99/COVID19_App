package com.mobilki.covidapp.api.model;

import java.util.List;

public class Film {
    private String id;
    private String title;
    private int yearOfRelease;
    private int duration;
    private String longDescription;
    private String shortDescription;
    private double ratings;
    private int ratingsCount;
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

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() { return shortDescription; }

    public double getRatings() {
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

    public int getDuration() { return duration; }

    public int getRatingsCount() { return ratingsCount; }


    public void setId(String id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public void setLongDescription(String description) { this.longDescription = description; }

    public void setShortDescription(String description) { this.shortDescription = description; }

    public void setRatings(double ratings) {
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

    public void setDuration(int duration) { this.duration = duration; }

    public void setRatingsCount(int ratingsCount) { this.ratingsCount = ratingsCount; }
}
