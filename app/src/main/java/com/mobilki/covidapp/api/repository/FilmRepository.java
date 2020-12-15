package com.mobilki.covidapp.api.repository;

import com.mobilki.covidapp.api.model.Film;

import java.util.ArrayList;
import java.util.List;

public class FilmRepository implements Repository<Film> {
    private List<Film> films = new ArrayList<>();

    @Override
    public synchronized Film get(String id) {
        for (Film f : films) {
            if (f.getId().equals(id))
                return f;
        }
        return null;
    }

    @Override
    public void add(Film film) {
        this.films.add(film);
    }

    @Override
    public List<Film> getAll() {
        return films;
    }
}
