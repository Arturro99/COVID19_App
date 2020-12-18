package com.mobilki.covidapp.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobilki.covidapp.MainActivity;
import com.mobilki.covidapp.R;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Register extends AppCompatActivity {

    EditText mName, mEmail, mPassword, mPasswordRepeated;
    Button mRegisterBtn;
    ProgressBar mProgressBar;
    FirebaseAuth mFirebaseAuth;
    TextView mLogIn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {

        mRegisterBtn.setOnClickListener(view -> {
            register();
        });
        mLogIn.setOnClickListener(view -> startActivity(new Intent(this, Login.class)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean register() {
        System.out.println("IN REGISTRATION");
        boolean emailFilled = false;

        String eMail = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(eMail))
            mEmail.setError("You need to specify e-mail");
        else
            emailFilled = true;


        if (!isValid(password)) {
            mPassword.setError("Your password does not match one of the password criteria:" +
                    "\nlength >=8" +
                    "\nat least one uppercase letter" +
                    "\nat least one lowercase letter" +
                    "\nat least one digit" +
                    "\nat least one non-alphabetic symbol");
        }

        if (emailFilled && isValid(password)) {
            System.out.println("IN SUBMITTING");
            mProgressBar.setVisibility(View.VISIBLE);

            mFirebaseAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        Toast.makeText(Register.this, "An error occurred: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            });
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValid(String pwd) {
        Predicate<String> rule1 = s -> pwd.length() >= 8;
        Predicate<String> rule2a = s -> !s.equals(s.toLowerCase());
        Predicate<String> rule2b = s -> !s.equals(s.toUpperCase());
        Predicate<String> rule2c = s -> s.codePoints().anyMatch(Character::isDigit);
        Predicate<String> rule2d = s -> s.codePoints().anyMatch(Character::isAlphabetic);
        Predicate<String> rule2 = s -> Stream.of(rule2a, rule2b, rule2c, rule2d)
                                                .filter(x -> x.test(s))
                                                .count() >= 3;

        System.out.println("IN PWD VALIDATION");
        return rule1.and(rule2).test(pwd);
    }
}