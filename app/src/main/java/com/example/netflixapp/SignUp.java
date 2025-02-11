package com.example.netflixapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.netflixapp.api.ApiService;
import com.example.netflixapp.api.RetrofitClient;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword, etDisplayName;
    private Button btnSignUp, btnUploadImage, btngoToSignIn, btnChangeMode;
    private ImageView ivProfilePreview;

    private Uri selectedImageUri;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etDisplayName = findViewById(R.id.etDisplayName);
        btnSignUp = findViewById(R.id.btnSignUpToSI);
        btnUploadImage = findViewById(R.id.btnChooseImage);
        ivProfilePreview = findViewById(R.id.imageView);
        btngoToSignIn = findViewById(R.id.btnMoveToSI);
        btnChangeMode = findViewById(R.id.btnChangeMode1);

        // Set listeners
        btnUploadImage.setOnClickListener(v -> selectImage());
        btnSignUp.setOnClickListener(v -> handleSignUp());
        btngoToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        });
        btnChangeMode.setOnClickListener(v -> {
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }

    // Open file picker to choose an image
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // Convert the selected URI into a File and display preview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivProfilePreview.setImageURI(selectedImageUri);

            // Convert the Uri to a File
            imageFile = getFileFromUri(selectedImageUri);
            if (imageFile == null) {
                Toast.makeText(this, "Failed to process image file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Validate inputs and send user data and image in a single request
    private void handleSignUp() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String displayName = etDisplayName.getText().toString().trim();

        // Input validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
            Toast.makeText(this, "Password must contain at least one letter and one number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageFile == null) {
            Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create RequestBody objects for text fields
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody displayNameBody = RequestBody.create(MediaType.parse("text/plain"), displayName);

        // Prepare the image file as a MultipartBody.Part
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("picture", imageFile.getName(), imageRequestBody);

        // Make the API call to register the user with all data in one request
        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.registerUser(usernameBody, passwordBody, displayNameBody, imagePart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    // Redirect to login screen (or another activity)
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignUp.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SignUp", "Error: " + t.getMessage());
            }
        });
    }

    // Convert a URI to a File by copying its data into a temporary file in the cache directory
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

    // Retrieve the file name from the Uri
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

    // Copy data from an InputStream to a File
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
