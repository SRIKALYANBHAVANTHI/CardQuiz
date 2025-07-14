package com.example.cardquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private LinearLayout optionsLayout;
    private Button buttonNext;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "UserDataPrefs";
    private static final String KEY_USER_NAME = "loggedInUserEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        textViewQuestion = findViewById(R.id.text_view_question);
        optionsLayout = findViewById(R.id.options_layout);
        buttonNext = findViewById(R.id.button_next);

        // Load quiz questions from XML file
        loadQuizQuestions();

        // Display the first question
        showQuestion();

        // Listen for next button click
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user has selected an answer
                if (quizQuestions.get(currentQuestionIndex).isQuestionAnswered()) {
                    // Show confirmation dialog
                    showConfirmationDialog();
                } else {
                    // No answer selected, show error message
                    Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Load quiz questions from XML file
    private void loadQuizQuestions() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.quiz);
            quizQuestions = QuizParser.parseQuestions(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Display the current question
    private void showQuestion() {
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        textViewQuestion.setText(currentQuestion.getQuestionText());
        optionsLayout.removeAllViews(); // Clear existing options

        if (currentQuestion.getOptions().size() == 1) {
            // Single choice question, use RadioGroup
            RadioGroup radioGroupOptions = new RadioGroup(this);
            radioGroupOptions.setOrientation(LinearLayout.VERTICAL);
            for (String option : currentQuestion.getOptions()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioGroupOptions.addView(radioButton);
            }
            optionsLayout.addView(radioGroupOptions);
        } else {
            // Multiple choice question, use CheckBoxes
            for (String option : currentQuestion.getOptions()) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setTextColor(-1);
                checkBox.setText(option);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Update user answers when CheckBox state changes
                        if (isChecked) {
                            currentQuestion.addUserAnswer(checkBox.getText().toString());
                        } else {
                            currentQuestion.removeUserAnswer(checkBox.getText().toString());
                        }
                    }
                });
                optionsLayout.addView(checkBox);
            }
        }
    }

    // Show confirmation dialog
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Answer");
        builder.setMessage("Are you sure you want to select this answer?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check if the answer is correct
                checkAnswer();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    // Check if the answer is correct
    private void checkAnswer() {
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        if (currentQuestion.isQuestionCorrect()) {
            score++; // Increment score if the answer is correct
        }

        // Move to the next question or display the result
        currentQuestionIndex++;
        if (currentQuestionIndex < quizQuestions.size()) {
            showQuestion(); // Display the next question
        } else {
            displayResult(); // Display the quiz result
        }
    }

    // Store game stats in SharedPreferences for a specific user
    private void storeGameStats(String userEmail, String scoreAndPercentage) {
        SharedPreferences sharedPreferences = getSharedPreferences("GameStats_" + userEmail, Context.MODE_PRIVATE);
        try {
            // Retrieve existing game stats from SharedPreferences
            JSONArray gameStatsArray;
            String gameStatsJson = sharedPreferences.getString("GameStatsList", "[]");
            gameStatsArray = new JSONArray(gameStatsJson);

            // Add the new game stats to the array
            gameStatsArray.put(scoreAndPercentage);

            // Store the updated game stats array back in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("GameStatsList", gameStatsArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Display the quiz result
    private void displayResult() {
        // Calculate score percentage
        int totalQuestions = quizQuestions.size();
        int percentage = (int) (((double) score / totalQuestions) * 100);

        // Build result message
        String resultMessage = "";
        resultMessage += "Score: " + score + "/" + totalQuestions + "\n";
        resultMessage += "Percentage: " + percentage + "%";

        // Display result in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Result");
        builder.setMessage(resultMessage);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve user's name from SharedPreferences
        String loggedInUser = sharedPreferences.getString(KEY_USER_NAME, "");
        storeGameStats(loggedInUser,resultMessage);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish(); // Finish the activity after displaying result
            }
        });
        builder.setCancelable(false); // Prevent dismissing the dialog by clicking outside
        builder.show();
    }
}
