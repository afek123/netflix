package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCategoriesActivity extends AppCompatActivity {
    private ListView lvCategories;
    private ArrayAdapter<String> adapter;
    private List<String> categoryNames;
    private List<Category> categories;
    private ApiService apiService;
    private FloatingActionButton btnAddCategory;
    private FloatingActionButton toggleThemeButton; // Toggle Theme button

    private static final int UPDATE_CATEGORY_REQUEST_CODE = 1;
    private static final int ADD_CATEGORY_REQUEST_CODE = 2;

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

        setContentView(R.layout.activity_manage_categories);

        // Initialize views
        lvCategories = findViewById(R.id.lvCategories);
        btnAddCategory = findViewById(R.id.btnAdd);
        toggleThemeButton = findViewById(R.id.toggleThemeButton); // Initialize Toggle Theme button

        // Initialize lists and adapter
        categoryNames = new ArrayList<>();
        categories = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
        lvCategories.setAdapter(adapter);

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Load categories
        loadCategories();

        // Handle long click to delete a category
        lvCategories.setOnItemLongClickListener((adapterView, view, position, id) -> {
            Category category = categories.get(position);
            deleteCategory(category.getId());
            return true;
        });

        // Handle click to edit a category
        lvCategories.setOnItemClickListener((parent, view, position, id) -> {
            Category category = categories.get(position);
            Intent intent = new Intent(ManageCategoriesActivity.this, EditCategoryActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getName());
            intent.putExtra("category_promoted", category.isPromoted());
            startActivityForResult(intent, UPDATE_CATEGORY_REQUEST_CODE);
        });

        // Floating Action Button click listener for adding a category
        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCategoriesActivity.this, AddCategoryActivity.class);
            startActivityForResult(intent, ADD_CATEGORY_REQUEST_CODE);
        });

        // Floating Action Button click listener for toggling theme
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the category addition or update
        if (resultCode == RESULT_OK) {
            // Refresh categories list when either category is added or updated
            loadCategories();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh categories list when the activity resumes
        loadCategories();
    }

    private void loadCategories() {
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categoryNames.clear();

                    for (Category category : response.body()) {
                        // Log received category details
                        Log.d("API_RESPONSE", "Category received - ID: " + category.getId() + ", Name: " + category.getName());

                        categories.add(category);
                        categoryNames.add(category.getName());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageCategoriesActivity.this, "Failed to load categories!", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response unsuccessful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Network request failed", t);
            }
        });
    }

    private void deleteCategory(String categoryId) {
        Call<Void> call = apiService.deleteCategory(categoryId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageCategoriesActivity.this, "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Refresh the list
                } else {
                    // Handle error response
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error: " + errorBody);
                        Toast.makeText(ManageCategoriesActivity.this, "Failed to delete category: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ManageCategoriesActivity.this, "Error parsing error response!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}