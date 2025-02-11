package com.example.netflixapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.example.netflixapp.entities.User;
import com.example.netflixapp.api.ApiService;
import com.example.netflixapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignInTo);

        btnSignIn.setOnClickListener(view -> {
            if (valid()) {
                // Create a User object with the login credentials
                User user = new User();
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());

                // Call the login API
                loginUser(user);
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        View btnSignUpTo = findViewById(R.id.btnSignUpTo);
        btnSignUpTo.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(this, SignUp.class);
            startActivity(signUpIntent);
        });

        Button btnChangeMode = findViewById(R.id.btnChangeMode);
        btnChangeMode.setOnClickListener(view -> {
            int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }

    private boolean valid() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // Validate input
        return !username.isEmpty() && password.length() >= 8;
    }

    private void loginUser(User user) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<User> call = apiService.loginUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login successful
                    User loggedInUser = response.body();
                    Toast.makeText(SignIn.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to the next activity
                    Intent intent = new Intent(SignIn.this, afterActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                } else {
                    // Login failed
                    Toast.makeText(SignIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network errors
                Toast.makeText(SignIn.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}