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

public class EditCategoryActivity extends AppCompatActivity {
    private EditText editCategoryNameEditText;
    private CheckBox editCategoryPromotedCheckBox;
    private Button updateCategoryButton;
    private FloatingActionButton toggleThemeButton; // Toggle Theme button

    private String categoryId;
    private ApiService apiService;
    private boolean isLoading = false;

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

        setContentView(R.layout.activity_edit_category);

        // Initialize views
        editCategoryNameEditText = findViewById(R.id.editCategoryName);
        editCategoryPromotedCheckBox = findViewById(R.id.editCategoryPromoted);
        updateCategoryButton = findViewById(R.id.updateCategoryButton);
        toggleThemeButton = findViewById(R.id.toggleThemeButton); // Initialize Toggle Theme button

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Get category data from Intent
        Intent intent = getIntent();
        if (intent.hasExtra("category_id")) {
            categoryId = intent.getStringExtra("category_id");
            editCategoryNameEditText.setText(intent.getStringExtra("category_name"));
            editCategoryPromotedCheckBox.setChecked(intent.getBooleanExtra("category_promoted", false));
        } else {
            Toast.makeText(this, "Error: No category data received!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no data is received
            return;
        }

        // Set click listeners
        updateCategoryButton.setOnClickListener(v -> handleUpdateCategory());
        updateToggleThemeButtonIcon(isDarkMode);

        // Set click listener for the Toggle Theme button
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

    private void handleUpdateCategory() {
        if (isLoading) return;

        String updatedCategoryName = editCategoryNameEditText.getText().toString().trim();
        boolean isPromoted = editCategoryPromotedCheckBox.isChecked();

        if (updatedCategoryName.isEmpty()) {
            Toast.makeText(this, "Category name is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        isLoading = true;
        updateCategoryButton.setText("Updating...");

        Category updatedCategory = new Category(categoryId, updatedCategoryName, isPromoted);

        Call<Void> call = apiService.updateCategory(categoryId, updatedCategory);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading = false;
                updateCategoryButton.setText("Update Category");

                if (response.isSuccessful()) {
                    Toast.makeText(EditCategoryActivity.this, "Category updated successfully!", Toast.LENGTH_SHORT).show();

                    // Send back to MainActivity
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish(); // Return to MainActivity
                } else {
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
        updateCategoryButton.setText("Update Category");
        t.printStackTrace();
        Toast.makeText(this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}