package com.mobilki.covidapp.health;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HealthForm extends AppCompatActivity {
    Map<String, Object> functional = new HashMap<>();

    Integer targetWeight = 0;
    Integer targetSteps = 0;
    Integer targetWater = 0;

    Button shoulderYes;
    Button shoulderNo;
    Button backYes;
    Button backNo;
    Button WristsYes;
    Button WristsNo;
    Button KneesYes;
    Button KneesNo;
    Button ElbowsYes;
    Button ElbowsNo;
    Button HipsYes;
    Button HipsNo;

    EditText targetWeightText;
    EditText targetStepsText;
    EditText targetWaterText;

    LinearLayout backgroundShoulder;
    LinearLayout backgroundBack;
    LinearLayout backgroundWrists;
    LinearLayout backgroundKnees;
    LinearLayout backgroundElbows;
    LinearLayout backgroundHips;

    Button saveBtn;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CollectionReference collectionReference;
    SharedPreferences settings;
    int chooseYes;
    int res;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences), 0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);
        setContentView(R.layout.activity_health_form);

        settings.edit().putBoolean("first_time_health", false).apply();
        res = !settings.getBoolean("darkModeOn", false) ? R.style.Widget_MaterialComponents_Button_OutlinedButton : R.style.Widget_MaterialComponents_Button;

        initMap();
        initBtnsAndText();
        initAllOnClick();
        initFireBase();
        getDataFromDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromDB() {
        collectionReference.document("healthSettings").get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalShoulder")).orElse(false))) {
                    changeStyle(shoulderYes, shoulderNo, backgroundShoulder, "functionalShoulder");
                    chooseYes++;
                }
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalBack")).orElse(false))) {
                    changeStyle(backYes, backNo, backgroundBack, "functionalBack");
                    chooseYes++;
                }
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalWrists")).orElse(false))) {
                    changeStyle(WristsYes, WristsNo, backgroundWrists, "functionalWrists");
                    chooseYes++;
                }
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalKnees")).orElse(false))) {
                    changeStyle(KneesYes, KneesNo, backgroundKnees, "functionalKnees");
                    chooseYes++;
                }
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalElbows")).orElse(false))) {
                    changeStyle(ElbowsYes, ElbowsNo, backgroundElbows, "functionalElbows");
                    chooseYes++;
                }
                if ((Optional.ofNullable(documentSnapshot.getBoolean("functionalHips")).orElse(false))) {
                    changeStyle(HipsYes, HipsNo, backgroundHips, "functionalHips");
                    chooseYes++;
                }
                targetWeightText.setText((Optional.ofNullable(documentSnapshot.getLong("targetWeight")).orElse(19L).toString()));
                targetStepsText.setText((Optional.ofNullable(documentSnapshot.getLong("targetSteps")).orElse(19L).toString()));
                targetWaterText.setText((Optional.ofNullable(documentSnapshot.getLong("targetWater")).orElse(19L).toString()));
            }
        });
    }

    private void changeStyle(Button yes, Button no, LinearLayout layout, String functionalString) {

        no.setTextAppearance(res);
        yes.setTextAppearance(R.style.Widget_MaterialComponents_Button);
        layout.setBackgroundResource(R.drawable.outline_button_yes);
        functional.put(functionalString, true);
    }


    private void initFireBase() {
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        collectionReference = mFirestore
                .collection("users").document(mUser.getUid())
                .collection("settings");
    }

    private void initMap() {
        functional.put("functionalShoulder", false);
        functional.put("functionalBack", false);
        functional.put("functionalWrists", false);
        functional.put("functionalKnees", false);
        functional.put("functionalElbows", false);
        functional.put("functionalHips", false);
    }

    private void initAllOnClick() {
        setOnClickBtn(shoulderYes, shoulderNo, backgroundShoulder, "functionalShoulder");
        setOnClickBtn(backYes, backNo, backgroundBack, "functionalBack");
        setOnClickBtn(WristsYes, WristsNo, backgroundWrists, "functionalWrists");
        setOnClickBtn(KneesYes, KneesNo, backgroundKnees, "functionalKnees");
        setOnClickBtn(ElbowsYes, ElbowsNo, backgroundElbows, "functionalElbows");
        setOnClickBtn(HipsYes, HipsNo, backgroundHips, "functionalHips");
        saveBtn.setOnClickListener(view -> {
            saveData();
        });
    }

    private void saveData() {
        try {
            targetWeight = Integer.parseInt(targetWeightText.getText().toString());
        } catch (NumberFormatException e) {
            targetWeight = 0;
        }
        try {
            targetSteps = Integer.parseInt(targetStepsText.getText().toString());
        } catch (NumberFormatException e) {
            targetSteps = 0;
        }
        try {
            targetWater = Integer.parseInt(targetWaterText.getText().toString());
        } catch (NumberFormatException e) {
            targetWater = 0;
        }

        DocumentReference documentReference = collectionReference.document("healthSettings");
        Map<String, Object> settings = functional;
        settings.put("targetWeight", targetWeight);
        settings.put("targetSteps", targetSteps);
        settings.put("targetWater", targetWater);

        documentReference.set(settings).addOnSuccessListener(x -> Toast.makeText(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).show());
        HealthForm.this.finish();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setOnClickBtn(Button yes, Button no, LinearLayout layout, String functionalString) {
        yes.setOnClickListener(view -> {
            if (chooseYes >= 2) {
                Toast.makeText(getApplicationContext(), getString(R.string.cannot_add_more_than_2),Toast.LENGTH_SHORT).show();
                return;
            }
            no.setTextAppearance(res);
            yes.setTextAppearance(R.style.Widget_MaterialComponents_Button);
            layout.setBackgroundResource(R.drawable.outline_button_yes);
            functional.put(functionalString, true);
            chooseYes++;
        });
        no.setOnClickListener(view -> {
            //if (layout.getBackground() == this.getApplicationContext().getDrawable(R.drawable.outline_button_yes)) {
            if ((boolean) functional.get(functionalString)) {
                chooseYes--;
            }
            yes.setTextAppearance(res);
            no.setTextAppearance(R.style.Widget_MaterialComponents_Button);
            layout.setBackgroundResource(R.drawable.outline_button_no);
            functional.put(functionalString, false);
        });
        no.setTextAppearance(R.style.Widget_MaterialComponents_Button);
        yes.setTextAppearance(res);
    }

    private void initBtnsAndText() {
        shoulderYes = findViewById(R.id.buttonFormShouldersYes);
        shoulderNo = findViewById(R.id.buttonFormShouldersNo);
        backYes = findViewById(R.id.buttonFormBackYes);
        backNo = findViewById(R.id.buttonFormBackNo);
        WristsYes = findViewById(R.id.buttonFormWristsYes);
        WristsNo = findViewById(R.id.buttonFormWristsNo);
        KneesYes = findViewById(R.id.buttonFormKneesYes);
        KneesNo = findViewById(R.id.buttonFormKneesNo);
        ElbowsYes = findViewById(R.id.buttonFormElbowYes);
        ElbowsNo = findViewById(R.id.buttonFormElbowNo);
        HipsYes = findViewById(R.id.buttonFormHipYes);
        HipsNo = findViewById(R.id.buttonFormHipNo);

        targetWeightText = findViewById(R.id.targetWeightText);
        targetStepsText = findViewById(R.id.targetStepsText);
        targetWaterText = findViewById(R.id.targetWaterText);


        backgroundShoulder = findViewById(R.id.backgroundShoulder);
        backgroundBack = findViewById(R.id.backgroundBack);
        backgroundWrists = findViewById(R.id.backgroundWrist);
        backgroundKnees = findViewById(R.id.backgroundKnees);
        backgroundElbows = findViewById(R.id.backgroundElbows);
        backgroundHips = findViewById(R.id.backgroundHips);

        saveBtn = findViewById(R.id.saveBtnFormHealth);
    }
}