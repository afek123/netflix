package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView lvCategories;
    private ArrayAdapter<String> adapter;
    private List<String> categoryNames;
    private List<Category> categories;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCategories = findViewById(R.id.lvCategories);
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
                        categories.add(category);
                        categoryNames.add(category.getName());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load categories!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory(String categoryId) {
        Call<Void> call = apiService.deleteCategory(categoryId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Refresh the list
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete category!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}