package com.example.cardquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewWelcomeMessage;
    private Button buttonStartNewGame;
    private Button buttonShowRules;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserDataPrefs";
    private static final String KEY_USER_NAME = "loggedInUserEmail";

    private Button logoutButton;
    View popupView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        textViewWelcomeMessage = findViewById(R.id.text_view_welcome_message);
        buttonStartNewGame = findViewById(R.id.button_start_new_game);
        buttonShowRules = findViewById(R.id.button_game_rules);
        popupView = findViewById(R.id.text_view_game_history_popup);

        logoutButton = findViewById(R.id.button_logout);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve user's name from SharedPreferences
        String loggedInUser = sharedPreferences.getString(KEY_USER_NAME, "");
        String loggedInUserName = sharedPreferences.getString(loggedInUser+"_first_name","");
        // Display welcome message with user's name
        displayWelcomeMessage(loggedInUserName);

        // Set OnClickListener for the "Start New Game" button
        buttonStartNewGame.setOnClickListener(view -> startNewGame());
        buttonShowRules.setOnClickListener(view->showRules());

        popupView.setOnClickListener(view->showGameStatsDialog(loggedInUser));

        logoutButton.setOnClickListener(view->logout());
    }

    private void logout() {
        // Clear the session data (loggedInUserEmail)
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_NAME);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent the user from navigating back to it
    }
    private void showGameStatsDialog(String userEmail) {
        // Retrieve game stats JSONArray from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("GameStats_" + userEmail, Context.MODE_PRIVATE);
        String gameStatsJson = sharedPreferences.getString("GameStatsList", "[]");

        try {
            // Convert game stats JSONArray to List of strings
            JSONArray gameStatsArray = new JSONArray(gameStatsJson);
            List<String> gameStatsList = new ArrayList<>();
            for (int i = 0; i < gameStatsArray.length(); i++) {
                gameStatsList.add(gameStatsArray.getString(i));
            }

            // Create a StringBuilder to build the game stats text
            StringBuilder gameStatsText = new StringBuilder();
            for (String stats : gameStatsList) {
                gameStatsText.append(stats).append("\n\n");
            }

            // Create a TextView to display the game stats
            TextView textView = new TextView(this);
            textView.setText(gameStatsText.toString());
            textView.setTextColor(Color.BLACK); // Set text color to black
            textView.setPadding(20, 20, 20, 20);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.popup_window_background)); // Set custom background drawable

            // Create a PopupWindow to show the game stats
            PopupWindow popupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(textView, Gravity.CENTER, 0, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void displayWelcomeMessage(String userName) {
        // Construct welcome message with user's name
        String welcome = "Welcome ";
        textViewWelcomeMessage.setText(welcome.concat(userName));
    }

    private void showRules()
    {
        Intent intent = new Intent(HomeActivity.this, GameRulesActivity.class);
        startActivity(intent);

    }
    private void startNewGame() {

        Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
        startActivity(intent);
    }
}

