package com.mobilki.covidapp.api.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.model.Book;
import com.mobilki.covidapp.api.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameRepository implements Repository<Game> {
    private List<Game> games = new ArrayList<>();

    @Override
    public void add(Game element) {
        games.add(element);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Game get(String titleEn) {
        return games.stream()
                .filter(b -> b.getTitleEn().equals(titleEn))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Game> getAll() {
        return games;
    }
}
