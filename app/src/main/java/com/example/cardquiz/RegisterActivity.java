package com.example.cardquiz;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextFamilyName, editTextEmail, editTextPassword, editTextDateOfBirth;
    private Button buttonRegister, buttonPickDate;

    private static final String PREF_NAME = "UserDataPrefs";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize views
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextFamilyName = findViewById(R.id.edit_text_family_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextDateOfBirth = findViewById(R.id.edit_text_date_of_birth);
        buttonRegister = findViewById(R.id.button_register);
        buttonPickDate = findViewById(R.id.button_pick_date);

        // Set OnClickListener for the Register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set OnClickListener for the Pick Date button
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Load saved registration data for the current user (if any)
        loadSavedDataForCurrentUser();
    }

    private void registerUser() {
        // Retrieve user input from EditText fields
        String firstName = editTextFirstName.getText().toString().trim();
        String familyName = editTextFamilyName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String dateOfBirth = editTextDateOfBirth.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(familyName) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(dateOfBirth)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user already exists (based on email)
        if (isUserExists(email)) {
            Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save registration data uniquely per email ID
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email + "_email",email);
        editor.putString(email + "_first_name", firstName);
        editor.putString(email + "_family_name", familyName);
        editor.putString(email + "_password", password);
        editor.putString(email + "_date_of_birth", dateOfBirth);
        editor.apply();

        // If all validations pass, proceed with registration logic
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

        // If all validations pass and registration is successful, navigate to the LoginActivity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void loadSavedDataForCurrentUser() {
        // Retrieve the logged-in user's email from SharedPreferences or session variable
        String userEmail = sharedPreferences.getString("loggedInUserEmail", "");

        // Check if the user is logged in
        if (!TextUtils.isEmpty(userEmail)) {
            // Load saved registration data for the current user (if any)
            String firstName = sharedPreferences.getString(userEmail + "_first_name", "");
            String familyName = sharedPreferences.getString(userEmail + "_family_name", "");
            String password = sharedPreferences.getString(userEmail + "_password", "");
            String dateOfBirth = sharedPreferences.getString(userEmail + "_date_of_birth", "");

            // Set loaded data to EditTexts
            editTextFirstName.setText(firstName);
            editTextFamilyName.setText(familyName);
            editTextPassword.setText(password);
            editTextDateOfBirth.setText(dateOfBirth);
        }
    }

    private boolean isUserExists(String email) {
        // Check if user already exists based on email
        return sharedPreferences.contains(email + "_first_name");
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update EditText with selected date
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year; // Month is 0-based
                        editTextDateOfBirth.setText(date);
                    }
                },
                year, month, dayOfMonth
        );

        // Set max date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // Show dialog
        datePickerDialog.show();
    }
}

