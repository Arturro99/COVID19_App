package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.R;

public class ExerciseSet extends Activity {
    TextView ex1;
    TextView ex2;
    TextView ex3;
    TextView ex4;
    TextView ex5;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_set);
//        ex1 = findViewById(R.id.exer1);
//        ex2 = findViewById(R.id.exer2);
//        ex3 = findViewById(R.id.exer3);
//        ex4 = findViewById(R.id.exer4);
//        ex5 = findViewById(R.id.exer5);
//
//        Pair<String, Integer> pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesShouldersNormal);
//        ex1.setText(pair.second + " x " + pair.first);
//
//
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesBackStretching);
//        ex2.setText(pair.second + " x " + pair.first);
//
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesKneesNormal);
//        ex3.setText(pair.second + " x " + pair.first);
//
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesWristNormal);
//        ex4.setText(pair.second + " x " + pair.first);
//
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesStomachStretching);
//        ex5.setText(pair.second + " x " + pair.first);

//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesShouldersStretching);
//        ex1.setText(pair.first + " " + pair.second);
//        pair = ExerciseRepository.getRandomExercise(ExerciseRepository.exercisesWristNormal);
//        ex1.setText(pair.first + " " + pair.second);
//        System.out.println(ExerciseRepository.exercisesBackNormal);
//        System.out.println(ExerciseRepository.exercisesBackStretching);
//        System.out.println(ExerciseRepository.exercisesShouldersNormal);
//        System.out.println(ExerciseRepository.exercisesShouldersStretching);
//        System.out.println(ExerciseRepository.exercisesKneesNormal);
//        System.out.println(ExerciseRepository.exercisesKneesStretching);
//        System.out.println(ExerciseRepository.exercisesWristNormal);
//        System.out.println(ExerciseRepository.exercisesWristStretching);
//        System.out.println(ExerciseRepository.exercisesStomachNormal);
//        System.out.println(ExerciseRepository.exercisesStomachStretching);


    }



}



//    Map<String, Object> data = new HashMap<>();
//        data.put("name ", "Back exercise ");
//        data.put("minReps", 10);
//        data.put("maxReps", 20);
//
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String forString = "wrist";
//        Random rand = new Random();
//        for (int i = 0; i< 5; i++) {
//            String name = forString +  " normal " + i;
//            String finalName = name;
//            db.collection("exercises").document(forString).collection("normal").document("test"+ i).set(new HashMap<String, Object>()
//            {
//                {
//                    put("name", finalName);
//                    put("minReps", rand.nextInt(10));
//                    put("maxReps", rand.nextInt(20)+10);
//                }
//            });
//            name = forString +  "  stretching " + i;
//            String finalName1 = name;
//            db.collection("exercises").document(forString).collection("stretching").document("test"+ i).set(new HashMap<String, Object>()
//            {
//                {
//                    put("name", finalName1);
//                    put("minReps", rand.nextInt(10));
//                    put("maxReps", rand.nextInt(20)+10);
//                }
//            });
//        }