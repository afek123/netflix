package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button manageCategoriesButton;
    private Button manageMoviesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        manageCategoriesButton = findViewById(R.id.manageCategoriesButton);
        manageMoviesButton = findViewById(R.id.manageMoviesButton);

        // On click listener for "Manage Categories" button
        manageCategoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
        });

        // On click listener for "Manage Movies" button
        manageMoviesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManageMoviesActivity.class);
            startActivity(intent);
        });
    }
}
