package com.mobilki.covidapp.api;

import org.json.JSONException;

import java.util.List;

import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.exceptions.*;

public interface EntertainmentDatabaseApi<T, S>{
    void getSorted(FilmSortingType type, int number);
    void getGenres();

    List<Film>getAll();
}
