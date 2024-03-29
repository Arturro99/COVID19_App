package com.mobilki.covidapp.api.customThreads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.ImdbApi;

import lombok.SneakyThrows;

public class FilmByGenresSorter extends Thread{
    private final ImdbApi imdbApi;
    private final String genre;
    private final int numberOfFilms;
    private final String locale;

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
