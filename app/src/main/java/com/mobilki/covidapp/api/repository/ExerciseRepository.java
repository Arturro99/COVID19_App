package com.mobilki.covidapp.api.repository;

import android.util.Pair;

import com.mobilki.covidapp.api.model.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExerciseRepository {
    public static List<Exercise> exercisesShouldersNormal = new ArrayList<>();
    public static List<Exercise> exercisesShouldersStretching = new ArrayList<>();
    public static List<Exercise> exercisesWristNormal = new ArrayList<>();
    public static List<Exercise> exercisesWristStretching = new ArrayList<>();


    public static void add(String id, String normal, Exercise exercises) {
        if (normal.equals("normal")) {
            if (id.equals("shoulders")) {
                exercisesShouldersNormal.add(exercises);
                System.out.println("1");
            } else if (id.equals("wrist")) {
                exercisesWristNormal.add(exercises);
                System.out.println("2");
            }
        } else {
            if (id.equals("shoulders")) {
                exercisesShouldersStretching.add(exercises);
                System.out.println("3");
            } else if (id.equals("wrist")) {
                exercisesWristStretching.add(exercises);
                System.out.println("4");
            }
        }
    }

    public static Pair<String, Integer> getRandomExercise(List<Exercise> exerciseList) {
        Random random = new Random();
        int element = random.nextInt(exerciseList.size());
        int reps = random.nextInt(exerciseList.get(element).getMaxReps()-1-exerciseList.get(element).getMinReps()) + exerciseList.get(element).getMinReps();
        return new Pair<>(exerciseList.get(element).getName(), reps);
    }
}
