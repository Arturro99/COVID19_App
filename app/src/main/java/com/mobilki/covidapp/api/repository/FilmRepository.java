package com.mobilki.covidapp.api.repository;

import com.mobilki.covidapp.api.model.Film;

import java.util.ArrayList;
import java.util.List;

public class FilmRepository {
    private List<Film> films = new ArrayList<>();

    public Film getFilm(String id) {
        for (Film f : films) {
            if (f.getId().equals(id))
                return f;
        }
        return null;
    }

    public void addFilm(Film film) {
        this.films.add(film);
    }

    public List<Film> getFilms() {
        return films;
    }
}
