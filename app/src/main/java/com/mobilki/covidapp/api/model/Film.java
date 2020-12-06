package com.mobilki.covidapp.api.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class Film implements Serializable {
    private String id;
    private String title;
    private Date dateOfRelease;
    private int duration;
    private String shortDescription;
    private double ratings;
    private int ratingsCount;
    private List<String> genres;
    private String imageUrl;

    private List<Actor> actors = new ArrayList<>();
    private HashMap<Integer, String> directors = new HashMap<>();

    public Film(String id) {
        this.id = id;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }
    public void addDirector(int id, String name) { directors.put(id, name); }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<Actor> getActor(int id) {
        return actors.stream()
                .filter(actor -> actor.getId() == id)
                .findFirst();
    }
}
