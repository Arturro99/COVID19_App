package com.mobilki.covidapp.api.customThreads;

import com.mobilki.covidapp.api.ImdbApi;

import lombok.SneakyThrows;

public class GenresSetter extends Thread {

    private final ImdbApi imdbApi;

    public GenresSetter(ImdbApi imdbApi) {
        this.imdbApi = imdbApi;
    }

    @SneakyThrows
    @Override
    public void run() {
       imdbApi.getGenres();
    }
}
