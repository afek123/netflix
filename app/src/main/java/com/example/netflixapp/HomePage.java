package com.example.netflixapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
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
    }
}