package com.mobilki.covidapp.api;

import org.json.JSONException;

import java.util.List;

import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.exceptions.*;

public interface EntertainmentDatabaseApi<T, S>{
    void getSortedByValues(FilmSortingType type, int number);
    void getSortedByGenres(String genre, int number);
    void getGenres() throws InterruptedException;

    List<Film>getAll();
}
