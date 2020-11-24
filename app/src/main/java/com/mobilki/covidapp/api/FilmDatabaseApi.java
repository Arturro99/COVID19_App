package com.mobilki.covidapp.api;

import org.json.JSONException;

import java.util.List;

import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.exceptions.*;

public interface FilmDatabaseApi {

    void fetchOverviewData(List<Film> films);


    void getMostPopularFilms();

    List<Film>getFilms();
}
