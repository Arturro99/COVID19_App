package com.mobilki.covidapp.api.customThreads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.FilmSortingType;
import com.mobilki.covidapp.api.ImdbApi;

import lombok.SneakyThrows;

public class FilmByValuesSorter extends Thread{
    private final ImdbApi imdbApi;
    private final FilmSortingType type;
    private final int numberOfFilms;
    private final String locale;

    public FilmByValuesSorter(ImdbApi imdbApi, FilmSortingType type, int numberOfFilms, String locale) {
        this.imdbApi = imdbApi;
        this.type = type;
        this.numberOfFilms = numberOfFilms;
        this.locale = locale;
    }

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        imdbApi.getSortedByValues(type, numberOfFilms, locale);
    }
}
