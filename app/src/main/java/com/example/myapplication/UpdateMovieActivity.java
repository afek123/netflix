package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;

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
    private Spinner categorySpinner;
    private Button selectVideoButton, selectPosterButton, updateMovieButton;
    private TextView errorTextView;

    private Uri videoFileUri, posterFileUri;
    private List<Category> categories = new ArrayList<>();
    private Movie movieToUpdate;

    private static final int PICK_VIDEO_FILE = 1;
    private static final int PICK_POSTER_FILE = 2;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);  // Reuse the same layout

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        directorEditText = findViewById(R.id.directorEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        selectPosterButton = findViewById(R.id.selectPosterButton);
        updateMovieButton = findViewById(R.id.addMovieButton); // Same button, but updating now
        errorTextView = findViewById(R.id.errorTextView);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Get the movie to update from the Intent
        movieToUpdate = (Movie) getIntent().getSerializableExtra("movie");
        if (movieToUpdate != null) {
            // Pre-fill fields with the current movie data
            titleEditText.setText(movieToUpdate.getTitle());
            directorEditText.setText(movieToUpdate.getDirector());
            // Assuming `getCategoryId()` gives you the category ID
            int categoryIndex = getCategoryIndexById(movieToUpdate.getCategory().getId());
            categorySpinner.setSelection(categoryIndex);
        }

        // Load categories for the spinner
        loadCategories();

        // Set click listeners
        selectVideoButton.setOnClickListener(v -> openFilePicker(PICK_VIDEO_FILE));
        selectPosterButton.setOnClickListener(v -> openFilePicker(PICK_POSTER_FILE));
        updateMovieButton.setOnClickListener(v -> handleUpdateMovie());
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateMovieActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
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
        String categoryId = categories.get(categorySpinner.getSelectedItemPosition()).getId();

        if (title.isEmpty() || director.isEmpty() || categoryId == null) {
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
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), categoryId);

        // Prepare the files
        File videoFile = getFileFromUri(videoFileUri);
        File posterFile = getFileFromUri(posterFileUri);

        if (videoFile == null || posterFile == null) {
            showError("Failed to process files!");
            return;
        }

        // Create MultipartBody.Part for the video file
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("videoUrl", videoFile.getName(), RequestBody.create(MediaType.parse("video/*"), videoFile));

        // Create MultipartBody.Part for the poster file
        MultipartBody.Part posterPart = MultipartBody.Part.createFormData("posterUrl", posterFile.getName(), RequestBody.create(MediaType.parse("image/*"), posterFile));

        // Make the API call to update the movie
        Call<Movie> call = apiService.updateMovie(movieToUpdate.getId(), titleBody, directorBody, categoryBody, videoPart, posterPart);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateMovieActivity.this, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Close the activity and return to the previous screen
                } else {
                    showError("Failed to update movie");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                showError("Error: " + t.getMessage());
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), "temp_file");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getCategoryIndexById(String categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(categoryId)) {
                return i;
            }
        }
        return 0; // Default to the first category if not found
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
