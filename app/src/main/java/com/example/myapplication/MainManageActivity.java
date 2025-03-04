package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainManageActivity extends AppCompatActivity {
    private Button manageCategoriesButton;
    private Button manageMoviesButton;
    private FloatingActionButton toggleThemeButton;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String THEME_KEY = "isDarkMode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the theme based on user preference
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        setAppTheme(isDarkMode);

        setContentView(R.layout.activity_main_manage);

        // Initialize buttons
        manageCategoriesButton = findViewById(R.id.manageCategoriesButton);
        manageMoviesButton = findViewById(R.id.manageMoviesButton);
        toggleThemeButton = findViewById(R.id.toggleThemeButton);

        // Set the initial icon for the toggle theme button
        updateToggleThemeButtonIcon(isDarkMode);
        toggleThemeButton.performClick(); // Perform a click to update the icon
        // On click listener for "Manage Categories" button
        manageCategoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainManageActivity.this, ManageCategoriesActivity.class);
            startActivity(intent);
        });

        // On click listener for "Manage Movies" button
        manageMoviesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainManageActivity.this, ManageMoviesActivity.class);
            startActivity(intent);
        });

        // On click listener for "Toggle Theme" button
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            updateToggleThemeButtonIcon(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });
    }

    private void setAppTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void updateToggleThemeButtonIcon(boolean isDarkMode) {
        if (isDarkMode) {
            toggleThemeButton.setImageResource(R.drawable.ic_baseline_light_mode_24); // Light mode icon
        } else {
            toggleThemeButton.setImageResource(R.drawable.ic_baseline_dark_mode_24); // Dark mode icon
        }
    }
}