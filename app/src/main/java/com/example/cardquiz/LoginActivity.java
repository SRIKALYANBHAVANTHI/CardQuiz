package com.example.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserDataPrefs";
    private static final String KEY_EMAIL = "loggedInUserEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonLogin = findViewById(R.id.button_login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Set OnClickListener for the "Login" button
        buttonLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        // Retrieve user input from EditText fields
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user exists and password matches
        if (isUserExists(email)) {
            String savedPassword = sharedPreferences.getString(email + "_password", "");
            if (password.equals(savedPassword)) {
                // Password matches, login successful
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Save user's email in SharedPreferences as session
                saveSession(email);

                // Proceed to HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

                // Finish the current activity (LoginActivity)
                finish();
            } else {
                // Password does not match
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // User does not exist
            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserExists(String email) {
        // Check if user exists in SharedPreferences
        return sharedPreferences.contains(email + "_email");
    }

    private void saveSession(String email) {
        // Save user's email in SharedPreferences as session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }
}
