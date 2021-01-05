package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.repository.ExerciseRepository;

public class ExerciseSet extends Activity {
    TextView ex1;
    TextView ex2;
    TextView ex3;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_set);
        ex1 = findViewById(R.id.exer1);
        ex2 = findViewById(R.id.exer2);
        ex3 = findViewById(R.id.exer3);

        Pair<String, Integer> pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesShouldersNormal);
        ex1.setText(pair.first + " " + pair.second);
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesShouldersStretching);
//        ex1.setText(pair.first + " " + pair.second);
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesWristNormal);
//        ex1.setText(pair.first + " " + pair.second);
    }



}
