package com.mobilki.covidapp.api.repository;

import java.util.HashMap;

public class FilmGenresRepository {
    private HashMap<Integer, String> genres = new HashMap<>();

    public void addGenre(int id, String name) {
        genres.put(id, name);
    }

    public String getGenre(int id) {
        return genres.get(id);
    }

    public HashMap<Integer, String> getGenres() { return this.genres; }
}
