package com.mobilki.covidapp.authentication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.MainActivity;
import com.mobilki.covidapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Register extends AppCompatActivity {

    EditText mName, mEmail, mPassword, mPasswordRepeated;
    Button mRegisterBtn;
    ProgressBar mProgressBar;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirestore;
    String userId;
    TextView mLogIn;
    SharedPreferences settings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.clientName);
        mEmail = findViewById(R.id.clientEmail);
        mPassword = findViewById(R.id.clientPassword);
        mPasswordRepeated = findViewById(R.id.clientPasswordRepeated);
        mRegisterBtn = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.progressBarRegister);
        mLogIn = findViewById(R.id.logIn);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {

        mRegisterBtn.setOnClickListener(view -> register());
        mLogIn.setOnClickListener(view -> startActivity(new Intent(this, Login.class)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean register() {

        String eMail = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String passwordRepeated = mPasswordRepeated.getText().toString().trim();
        String name = mName.getText().toString();

        if (TextUtils.isEmpty(eMail)) {
            mEmail.setError(getResources().getString(R.string.email_absent));
            return false;
        }


        if (!isValid(password)) {
            mPassword.setError(getResources().getString(R.string.criterias));
            return false;
        }

        if (!password.equals(passwordRepeated)) {
            mPasswordRepeated.setError(getResources().getString(R.string.password_match));
            return false;
        }

            mProgressBar.setVisibility(View.VISIBLE);

            mFirebaseAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUser fUser = mFirebaseAuth.getCurrentUser();
                    assert fUser != null;
                    fUser.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(Register.this, "E-mail sent successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(aVoid -> Toast.makeText(Register.this, "E-mail not sent (" + aVoid.getMessage() +")", Toast.LENGTH_SHORT).show());

                    userId = fUser.getUid();
                    DocumentReference documentReference = mFirestore.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", eMail);
                    documentReference.set(user).addOnSuccessListener(aVoid -> Log.d("SS", "Profile updated for" + userId));

                    Toast.makeText(Register.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    Toast.makeText(Register.this, "An error occurred: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValid(String pwd) {
        Predicate<String> rule1 = s -> pwd.length() >= 8;
        Predicate<String> rule2a = s -> !s.equals(s.toLowerCase());
        Predicate<String> rule2b = s -> !s.equals(s.toUpperCase());
        Predicate<String> rule2c = s -> s.codePoints().anyMatch(Character::isDigit);
        Predicate<String> rule2d = s -> s.matches(".*^[A-Za-z0-9 ].*");
        Predicate<String> rule2 = s -> Stream.of(rule2a, rule2b, rule2c, rule2d)
                                                .filter(x -> x.test(s))
                                                .count() >= 4;

        return rule1.and(rule2).test(pwd);
    }
}