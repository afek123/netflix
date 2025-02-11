package com.example.netflixapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(view -> {
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivity(signInIntent);
        });
        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUp.class);
            startActivity(signUpIntent);
        });
        Button btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(v -> {
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

    }
}