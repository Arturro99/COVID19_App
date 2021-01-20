package com.mobilki.covidapp.health;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ExercisesRepository {
    private List<Exercise> exercises = new ArrayList<>();

    public void add(Exercise element) {
        exercises.add(element);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Exercise get(String type) {
        return exercises.stream()
                .filter(b -> b.getType().equals(type))
                .findAny()
                .orElse(null);
    }


    public List<Exercise> getAll() {
        return exercises;
    }
}
