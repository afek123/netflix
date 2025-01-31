package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.entities.Movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovieActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, publisherEditText;
    private CheckBox promotedCheckBox;
    private Button saveMovieButton, selectMovieButton, selectPosterButton;

    private ApiService apiService;
    private Uri movieFileUri, posterFileUri;

    private static final int PICK_MOVIE_FILE = 1;
    private static final int PICK_POSTER_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        publisherEditText = findViewById(R.id.publisherEditText);
        promotedCheckBox = findViewById(R.id.promotedCheckBox);
        saveMovieButton = findViewById(R.id.saveMovieButton);
        selectMovieButton = findViewById(R.id.selectMovieButton);
        selectPosterButton = findViewById(R.id.selectPosterButton);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Set click listeners
        saveMovieButton.setOnClickListener(v -> handleAddMovie());
        selectMovieButton.setOnClickListener(v -> openFilePicker(PICK_MOVIE_FILE));
        selectPosterButton.setOnClickListener(v -> openFilePicker(PICK_POSTER_FILE));
    }

    private void handleAddMovie() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String publisher = publisherEditText.getText().toString().trim();
        boolean promoted = promotedCheckBox.isChecked();

        if (title.isEmpty() || description.isEmpty() || publisher.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the RequestBody objects for text fields
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody publisherBody = RequestBody.create(MediaType.parse("text/plain"), publisher);
        RequestBody promotedBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(promoted));

        // Prepare the files (if uploading)
        File movieFile = getFileFromUri(movieFileUri); // Replace with actual file URI
        File posterFile = getFileFromUri(posterFileUri); // Replace with actual file URI

        if (movieFile == null || posterFile == null) {
            Toast.makeText(this, "Please select both a movie file and a poster file!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create MultipartBody.Part for the movie file
        MultipartBody.Part moviePart = MultipartBody.Part.createFormData(
                "movieFile",
                movieFile.getName(),
                RequestBody.create(MediaType.parse("video/*"), movieFile)
        );

        // Create MultipartBody.Part for the poster file
        MultipartBody.Part posterPart = MultipartBody.Part.createFormData(
                "posterFile",
                posterFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), posterFile)
        );

        // Make the API call to create the movie
        Call<Movie> call = apiService.createMovie(moviePart, posterPart, titleBody, descriptionBody, publisherBody, promotedBody);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddMovieActivity.this, "Movie added successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Go back to the ManageMoviesActivity
                } else {
                    Toast.makeText(AddMovieActivity.this, "Failed to add movie", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(AddMovieActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (requestCode == PICK_MOVIE_FILE) {
                movieFileUri = fileUri;
                Toast.makeText(this, "Movie file selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_POSTER_FILE) {
                posterFileUri = fileUri;
                Toast.makeText(this, "Poster file selected", Toast.LENGTH_SHORT).show();
            }
        }
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
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
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
}