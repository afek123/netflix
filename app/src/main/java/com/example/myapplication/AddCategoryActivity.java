package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {
    private EditText newCategoryNameEditText;
    private CheckBox newCategoryPromotedCheckBox;
    private Button addCategoryButton;
    private boolean isLoading = false;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        newCategoryNameEditText = findViewById(R.id.newCategoryName);
        newCategoryPromotedCheckBox = findViewById(R.id.newCategoryPromoted);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        addCategoryButton.setOnClickListener(v -> handleAddCategory());
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
                    resetForm();
                } else {
                    // Handle error response
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error: " + errorBody);
                        Toast.makeText(AddCategoryActivity.this, "Error adding category: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddCategoryActivity.this, "Error parsing error response!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading = false;
                addCategoryButton.setText("Add Category");
                t.printStackTrace(); // Log the exception
                Toast.makeText(AddCategoryActivity.this, "Failed to add category: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        newCategoryNameEditText.setText("");
        newCategoryPromotedCheckBox.setChecked(false);
    }
}