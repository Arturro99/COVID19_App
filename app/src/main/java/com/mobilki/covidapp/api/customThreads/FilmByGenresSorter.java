package com.mobilki.covidapp.api.customThreads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.ImdbApi;

import lombok.SneakyThrows;

public class FilmByGenresSorter extends Thread{
    private ImdbApi imdbApi;
    private String genre;
    private int numberOfFilms;
    private String locale;

    public FilmByGenresSorter(ImdbApi imdbApi, String genre, int numberOfFilms, String locale) {
        this.imdbApi = imdbApi;
        this.genre = genre;
        this.numberOfFilms = numberOfFilms;
        this.locale = locale;
    }

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        imdbApi.getSortedByGenres(genre, numberOfFilms, locale);
    }
}
