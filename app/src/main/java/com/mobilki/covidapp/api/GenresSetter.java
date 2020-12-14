package com.mobilki.covidapp.api;

public class GenresSetter extends Thread {

    private ImdbApi imdbApi;

    public GenresSetter(ImdbApi imdbApi) {
        this.imdbApi = imdbApi;
    }

    @Override
    public void run() {
       imdbApi.getGenres();
    }
}
