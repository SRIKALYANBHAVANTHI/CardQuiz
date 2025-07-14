package com.example.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Button loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if already logged in navigate to home page
                sharedPreferences = getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE);

                if(sharedPreferences.contains("loggedInUserEmail"))
                {
                    startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
                    return;
                }

                // Handle login button click, navigate to LoginActivity
                startActivity(new Intent(AuthenticationActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register button click, navigate to RegisterActivity
                startActivity(new Intent(AuthenticationActivity.this, RegisterActivity.class));
            }
        });
    }
}
