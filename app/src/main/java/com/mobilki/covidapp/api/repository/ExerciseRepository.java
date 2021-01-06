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
    public static List<Exercise> exercisesBackNormal = new ArrayList<>();
    public static List<Exercise> exercisesBackStretching = new ArrayList<>();
    public static List<Exercise> exercisesStomachNormal = new ArrayList<>();
    public static List<Exercise> exercisesStomachStretching = new ArrayList<>();
    public static List<Exercise> exercisesKneesNormal = new ArrayList<>();
    public static List<Exercise> exercisesKneesStretching = new ArrayList<>();


    public static void add(String id, String normal, Exercise exercises) {
        if (normal.equals("normal")) {
            if (id.equals("shoulders")) {
                exercisesShouldersNormal.add(exercises);
            } else if (id.equals("wrist")) {
                exercisesWristNormal.add(exercises);
            } else if (id.equals("back")) {
                exercisesBackNormal.add(exercises);
            } else if (id.equals("stomach")) {
                exercisesStomachNormal.add(exercises);
            } else if (id.equals("knees")) {
                exercisesKneesNormal.add(exercises);
            }
        } else {
            if (id.equals("shoulders")) {
                exercisesShouldersStretching.add(exercises);
            } else if (id.equals("wrist")) {
                exercisesWristStretching.add(exercises);
            } else if (id.equals("back")) {
                exercisesBackStretching.add(exercises);
            } else if (id.equals("stomach")) {
                exercisesStomachStretching.add(exercises);
            } else if (id.equals("knees")) {
                exercisesKneesStretching.add(exercises);
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
