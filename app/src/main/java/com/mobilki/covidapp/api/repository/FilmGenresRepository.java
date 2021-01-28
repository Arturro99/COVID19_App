package com.mobilki.covidapp.api.repository;

import java.util.HashMap;

public class FilmGenresRepository {
    private static final HashMap<Integer, String> genres = new HashMap<>();

    public static void addGenre(int id, String name) {
        genres.put(id, name);
    }

    public static String getGenre(int id) {
        return genres.get(id);
    }

    public static HashMap<Integer, String> getGenres() { return genres; }
}
