package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMovieActivity extends AppCompatActivity {
    private EditText titleEditText, directorEditText;
    private MultiAutoCompleteTextView categoryMultiAutoCompleteTextView;
    private Button selectVideoButton, selectPosterButton, updateMovieButton;
    private TextView errorTextView;
    private FloatingActionButton toggleThemeButton;

    private Uri videoFileUri, posterFileUri;
    private List<Category> categories = new ArrayList<>();
    private Movie movieToUpdate;

    private static final int PICK_VIDEO_FILE = 1;
    private static final int PICK_POSTER_FILE = 2;

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

        setContentView(R.layout.activity_update_movie);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        directorEditText = findViewById(R.id.directorEditText);
        categoryMultiAutoCompleteTextView = findViewById(R.id.categoryMultiAutoCompleteTextView);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        selectPosterButton = findViewById(R.id.selectPosterButton);
        updateMovieButton = findViewById(R.id.updateMovieButton);
        errorTextView = findViewById(R.id.errorTextView);
        toggleThemeButton = findViewById(R.id.toggleThemeButton);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        // Get the movie to update from the Intent
        movieToUpdate = (Movie) getIntent().getSerializableExtra("movie");
        if (movieToUpdate != null) {
            // Pre-fill fields with the current movie data
            titleEditText.setText(movieToUpdate.getTitle());
            directorEditText.setText(movieToUpdate.getDirector());
            loadCategories();
            StringBuilder selectedCategories = new StringBuilder();
            for (String categoryId : movieToUpdate.getCategoryIds()) {
                for (Category category : categories) {
                    if (category.getId().equals(categoryId)) {
                        selectedCategories.append(category.getName()).append(", ");
                        break;
                    }
                }
            }
            if (selectedCategories.length() > 0) {
                selectedCategories.setLength(selectedCategories.length() - 2); // Remove the last ", "
            }
            categoryMultiAutoCompleteTextView.setText(selectedCategories.toString());
        }

        // Load categories for the MultiAutoCompleteTextView

        // Set click listeners
        selectVideoButton.setOnClickListener(v -> openFilePicker(PICK_VIDEO_FILE));
        selectPosterButton.setOnClickListener(v -> openFilePicker(PICK_POSTER_FILE));
        updateMovieButton.setOnClickListener(v -> handleUpdateMovie());
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

    private void loadCategories() {
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            UpdateMovieActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            categoryNames
                    );
                    categoryMultiAutoCompleteTextView.setAdapter(adapter);
                    categoryMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                } else {
                    showError("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void openFilePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (requestCode == PICK_VIDEO_FILE) {
                videoFileUri = fileUri;
                Toast.makeText(this, "Video file selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_POSTER_FILE) {
                posterFileUri = fileUri;
                Toast.makeText(this, "Poster file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleUpdateMovie() {
        String title = titleEditText.getText().toString().trim();
        String director = directorEditText.getText().toString().trim();
        String[] selectedCategories = categoryMultiAutoCompleteTextView.getText().toString().split(",\\s*");

        if (title.isEmpty() || director.isEmpty() || selectedCategories.length == 0) {
            showError("All fields are required!");
            return;
        }

        if (videoFileUri == null || posterFileUri == null) {
            showError("Please select both a video file and a poster file!");
            return;
        }

        // Prepare the RequestBody objects for text fields
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody directorBody = RequestBody.create(MediaType.parse("text/plain"), director);
        StringBuilder categoryIds = new StringBuilder();
        for (String categoryName : selectedCategories) {
            for (Category category : categories) {
                if (category.getName().equals(categoryName.trim())) {
                    if (categoryIds.length() > 0) {
                        categoryIds.append(",");
                    }
                    categoryIds.append(category.getId());
                    break;
                }
            }
        }
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), categoryIds.toString());

        // Prepare the files
        File videoFile = getFileFromUri(videoFileUri);
        File posterFile = getFileFromUri(posterFileUri);

        if (videoFile == null || posterFile == null) {
            showError("Failed to process files!");
            return;
        }

        // Create MultipartBody.Part for the video file
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData(
                "videoUrl",
                videoFile.getName(),
                RequestBody.create(MediaType.parse("video/*"), videoFile)
        );

        // Create MultipartBody.Part for the poster file
        MultipartBody.Part posterPart = MultipartBody.Part.createFormData(
                "posterUrl",
                posterFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), posterFile)
        );

        // Log the request details for debugging
        Log.d("UpdateMovieActivity", "Title: " + title);
        Log.d("UpdateMovieActivity", "Director: " + director);
        Log.d("UpdateMovieActivity", "Video File: " + videoFile.getName());
        Log.d("UpdateMovieActivity", "Poster File: " + posterFile.getName());
        Log.d("UpdateMovieActivity", movieToUpdate.getId());
        // Make the API call to update the movie
        Call<Movie> call = apiService.updateMovie(movieToUpdate.getId(), titleBody, directorBody, categoryBody, videoPart, posterPart);
        Log.d("UpdateMovieActivity", "Video File: " + videoFile.getName());
        Log.d("UpdateMovieActivity", "Poster File: " + posterFile.getName());
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Log.d("UpdateMovieActivity", "Movie updated successfully!");
                    Toast.makeText(UpdateMovieActivity.this, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Close the activity and return to the previous screen
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        showError("Failed to update movie: " + errorBody);
                        Log.e("UpdateMovieActivity", "Failed to update movie: " + errorBody);
                    } catch (IOException e) {
                        showError("Failed to update movie: Cannot parse error response");
                        Log.e("UpdateMovieActivity", "Failed to parse error response", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                showError("Error: " + t.getMessage());
                Log.e("UpdateMovieActivity", "Error: " + t.getMessage(), t);
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        if (uri == null) return null;

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), getFileName(uri));
            copyInputStreamToFile(inputStream, file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}