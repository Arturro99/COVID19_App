package com.mobilki.covidapp.health;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExercisesRepository {
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;
    private List<Exercise> exercisesUpper = new ArrayList<>();
    private List<Exercise> exercisesLower = new ArrayList<>();
    private List<Exercise> exercisesCondition = new ArrayList<>();
    boolean goodShoulders;
    boolean goodBack;
    boolean goodWrists;
    boolean goodKnees;
    boolean goodElbows;
    boolean goodHip;
    String language;


    public void add(Exercise element) {
        if (element.getType().equals(Exercise.TypeExercise.UPPER)){
            exercisesUpper.add(element);
        }
        else if (element.getType().equals(Exercise.TypeExercise.LOWER)){
            exercisesLower.add(element);
        }
        else if (element.getType().equals(Exercise.TypeExercise.CONDITION)) {
            exercisesCondition.add(element);
        }
    }

    public void getSettings() {
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore.collection("users").document(mUser.getUid())
                .collection("settings").document("healthSettings")
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                goodShoulders = !documentSnapshot.getBoolean("functionalShoulder");
                goodBack = !documentSnapshot.getBoolean("functionalBack");
                goodWrists = !documentSnapshot.getBoolean("functionalWrists");
                goodKnees = !documentSnapshot.getBoolean("functionalKnees");
                goodElbows = !documentSnapshot.getBoolean("functionalKnees");
                goodHip = !documentSnapshot.getBoolean("functionalHips");
            }
        });
        mFirestore.collection("users").document(mUser.getUid())
                .collection("settings").document("language")
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                language = documentSnapshot.getString("language");
            }
        });
    }

    public ExerciseToApp get(Exercise.TypeExercise type) {
        Random random = new Random();
        List<Exercise> list;
        if (type.equals(Exercise.TypeExercise.UPPER)){
            list = exercisesUpper;
        }
        else if (type.equals(Exercise.TypeExercise.LOWER)){
            list = exercisesLower;
        }
        else {
            list = exercisesCondition;
        }
        Exercise tmp = null;
        boolean isGood = false;
        while (!isGood) {
            isGood = true;
            int rand = random.nextInt(list.size());
            tmp = list.get(rand);
            if ((!goodShoulders) && tmp.isGoodShoulders()) {
                isGood = false;
            }
            if ((!goodBack) && tmp.isGoodBack()) {
                isGood = false;
            }
            if ((!goodWrists) && tmp.isGoodWrists()) {
                isGood = false;
            }
            if ((!goodKnees) && tmp.isGoodKnees()) {
                isGood = false;
            }
            if ((!goodElbows) && tmp.isGoodElbows()) {
                isGood = false;
            }
            if ((!goodHip) && tmp.isGoodHip()) {
                isGood = false;
            }
            System.out.println(tmp.toString());
        }
        int reps = random.nextInt(tmp.getMaxReps()-tmp.getMinReps())+tmp.getMinReps();

        ExerciseToApp tmpExet;
        if (language.equals("polish")) {
            String repsAndTitle = reps + " x " + tmp.getName_pl();
            tmpExet = new ExerciseToApp(repsAndTitle, tmp.getYt(), tmp.getDescription_pl());
        } else {
            String repsAndTitle = reps + " x " + tmp.getName_en();
            tmpExet = new ExerciseToApp(repsAndTitle, tmp.getYt(), tmp.getDescription_en());
        }






        return tmpExet;
    }
}
