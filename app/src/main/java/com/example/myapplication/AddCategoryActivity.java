package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {
    private EditText newCategoryNameEditText;
    private CheckBox newCategoryPromotedCheckBox;
    private Button addCategoryButton;
    private FloatingActionButton toggleThemeButton; // Toggle Theme button
    private boolean isLoading = false;

    private ApiService apiService;
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

        setContentView(R.layout.activity_add_category);

        // Initialize views
        newCategoryNameEditText = findViewById(R.id.newCategoryName);
        newCategoryPromotedCheckBox = findViewById(R.id.newCategoryPromoted);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        toggleThemeButton = findViewById(R.id.toggleThemeButton); // Initialize Toggle Theme button

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Set click listeners
        addCategoryButton.setOnClickListener(v -> handleAddCategory());
        updateToggleThemeButtonIcon(isDarkMode);

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


    private void handleAddCategory() {
        if (isLoading) return; // Prevent multiple clicks

        String newCategoryName = newCategoryNameEditText.getText().toString().trim();
        boolean newCategoryPromoted = newCategoryPromotedCheckBox.isChecked();

        if (newCategoryName.isEmpty()) {
            Toast.makeText(this, "Category name is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        isLoading = true;
        addCategoryButton.setText("Adding...");

        // Create category object
        Category category = new Category(newCategoryName, newCategoryPromoted);

        // Make Retrofit API call
        Call<Void> call = apiService.createCategory(category);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading = false;
                addCategoryButton.setText("Add Category");

                if (response.isSuccessful()) {
                    // Success: Category added
                    Toast.makeText(AddCategoryActivity.this, "Category added successfully!", Toast.LENGTH_SHORT).show();

                    // Return to ManageCategoriesActivity with a result
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish(); // Close the activity and return to ManageCategoriesActivity
                } else {
                    // Handle error response
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showError(t);
            }
        });
    }

    private void handleError(Response<Void> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("API_ERROR", "Error: " + errorBody);
            Toast.makeText(this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing error response!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(Throwable t) {
        isLoading = false;
        t.printStackTrace();
        Toast.makeText(this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}