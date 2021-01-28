package com.mobilki.covidapp.api;

import java.util.List;

import com.mobilki.covidapp.api.model.Film;

public interface EntertainmentDatabaseApi<T, S>{
    void getSortedByValues(FilmSortingType type, int number, String locale) throws InterruptedException;
    void getSortedByGenres(String genre, int number, String locale) throws InterruptedException;
    void getGenres() throws InterruptedException;

    List<Film>getAll();
}
