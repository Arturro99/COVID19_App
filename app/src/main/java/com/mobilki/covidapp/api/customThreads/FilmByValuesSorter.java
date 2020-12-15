package com.mobilki.covidapp.api.customThreads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.FilmSortingType;
import com.mobilki.covidapp.api.ImdbApi;

public class FilmByValuesSorter extends Thread{
    private ImdbApi imdbApi;
    private FilmSortingType type;
    private int numberOfFilms;

    public FilmByValuesSorter(ImdbApi imdbApi, FilmSortingType type, int numberOfFilms) {
        this.imdbApi = imdbApi;
        this.type = type;
        this.numberOfFilms = numberOfFilms;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        imdbApi.getSortedByValues(type, numberOfFilms);
    }
}
