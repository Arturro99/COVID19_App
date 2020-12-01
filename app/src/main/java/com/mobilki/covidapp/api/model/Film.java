package com.mobilki.covidapp.api.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Film implements Serializable {
    private String id;
    private String title;
    private Date dateOfRelease;
    private int duration;
    private String longDescription;
    private String shortDescription;
    private double ratings;
    private int ratingsCount;
    private List<String> genres;
    private String imageUrl;

    private HashMap<Integer, String> actors = new HashMap<>();
    private HashMap<Integer, String> directors = new HashMap<>();

    public void addActor(int id, String name) {
        actors.put(id, name);
    }
    public void addDirector(int id, String name) { directors.put(id, name); }

    public String getActor(int id) {
        return actors.get(id);
    }
    public String getDirector(int id) { return directors.get(id); }

    public HashMap<Integer, String> getActors() { return this.actors; }
    public HashMap<Integer, String> getDirectors() { return this.directors; }

    public Film(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateOfRelease() {
        return dateOfRelease;
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


    public int getDuration() { return duration; }

    public int getRatingsCount() { return ratingsCount; }


    public void setId(String id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateOfRelease(Date dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public void setLongDescription(String description) { this.longDescription = description; }

    public void setShortDescription(String description) { this.shortDescription = description; }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setDuration(int duration) { this.duration = duration; }

    public void setRatingsCount(int ratingsCount) { this.ratingsCount = ratingsCount; }
}
