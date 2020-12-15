package com.mobilki.covidapp.api.customThreads;

import com.mobilki.covidapp.api.ImdbApi;

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
