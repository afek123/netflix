package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.entities.User;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SignIn extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnSignIn;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton toggleThemeButton;

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String THEME_KEY = "isDarkMode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        setAppTheme(isDarkMode);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignInTo);
        toggleThemeButton = findViewById(R.id.toggleThemeButton);
        // Set the initial icon for the toggle theme button
        updateToggleThemeButtonIcon(isDarkMode);
        // Set click listener for the Toggle Theme button
        toggleThemeButton.setOnClickListener(v -> {
            boolean newDarkMode = !isDarkMode;
            sharedPreferences.edit().putBoolean(THEME_KEY, newDarkMode).apply();
            setAppTheme(newDarkMode);
            recreate(); // Recreate the activity to apply the new theme
        });
        View btnSignUpTo = findViewById(R.id.btnSignUpTo);
        btnSignUpTo.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUp.class);
            startActivity(signUpIntent);
        });

        btnSignIn.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            // Create User object with entered username and password
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
            // Make the login request
            ApiService apiService = RetrofitClient.getRetrofitInstance(SignIn.this).create(ApiService.class);
            apiService.loginUser(user).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful() && response.body() != null ) {
                        // Get the JWT token from the response
                        Token token = response.body();
                        // Save the JWT token in SharedPreferences
                        saveToken(token.getToken(), token.getUserId(),token.getRole());
                        Log.d("SignIn", "Received token: " + token.getRole());
                        // Proceed to the next activity (e.g., home page)
                        Intent intent = new Intent(SignIn.this, mainPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Toast.makeText(SignIn.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Function to save JWT token in SharedPreferences
    private void saveToken(String token, String userId,String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token); // Store JWT token
        editor.putString("USER_ROLE", role); // Save the role
        editor.putString("user_id", userId); // Store User ID
        editor.apply();
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