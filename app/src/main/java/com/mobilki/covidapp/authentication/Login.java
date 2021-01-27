package com.mobilki.covidapp.authentication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobilki.covidapp.MainActivity;
import com.mobilki.covidapp.R;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mForgotPassword, mRegister;
    ProgressBar mProgressBar;
    FirebaseAuth mFirebaseAuth;
    SharedPreferences settings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(getResources().getString(R.string.shared_preferences),0);
        setTheme(!settings.getBoolean("darkModeOn", false) ? R.style.LightTheme : R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.clientEmail);
        mPassword = findViewById(R.id.clientPassword);
        mLoginBtn = findViewById(R.id.logIn);
        mForgotPassword = findViewById(R.id.forgotPassword);
        mRegister = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.progressBarLogin);
        mFirebaseAuth = FirebaseAuth.getInstance();

        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {
        mLoginBtn.setOnClickListener(view -> {
            login();
        });

        mRegister.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Register.class)));

        mForgotPassword.setOnClickListener(view -> {
            EditText email = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setMessage("Enter your e-mail to reset your password: ")
                    .setView(email)
                    .setTitle("Reset password")
                    .setPositiveButton("APPLY", (dialogInterface, i) -> {
                        String mail = email.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(
                                aVoid -> Toast.makeText(Login.this, "Reset link sent to the provided e-mail", Toast.LENGTH_SHORT).show()
                        ).addOnFailureListener(
                                e -> Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    })
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {})
                    .create()
                    .show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean login() {
        System.out.println("IN REGISTRATION");

        String eMail = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(eMail)) {
            mEmail.setError(getResources().getString(R.string.email_absent));
            return false;
        }

        if (!isValid(password)) {
            mPassword.setError(getResources().getString(R.string.password_match));
            return false;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        mFirebaseAuth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Login.this, getResources().getString(R.string.logged_in_success), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else {
                Toast.makeText(Login.this, "An error occurred: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        Predicate<String> rule2d = s -> s.codePoints().anyMatch(Character::isAlphabetic);
        Predicate<String> rule2 = s -> Stream.of(rule2a, rule2b, rule2c, rule2d)
                .filter(x -> x.test(s))
                .count() == 4;

        System.out.println("IN PWD VALIDATION");
        return rule1.and(rule2).test(pwd);
    }
}