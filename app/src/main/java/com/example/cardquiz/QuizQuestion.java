package com.example.cardquiz;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestion {
    private String questionText;
    private List<String> options;
    private List<String> correctAnswers;
    private List<String> userAnswers;

    public QuizQuestion(String questionText, List<String> options, List<String> correctAnswers) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswers = correctAnswers;
        this.userAnswers = new ArrayList<>();
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getUserAnswers() {
        return userAnswers;
    }

    public void addUserAnswer(String answer) {
        userAnswers.add(answer);
    }

    public void removeUserAnswer(String answer) {
        userAnswers.remove(answer);
    }

    public boolean isQuestionAnswered() {
        return !userAnswers.isEmpty();
    }

    public boolean isQuestionCorrect() {
        return correctAnswers.containsAll(userAnswers) && userAnswers.containsAll(correctAnswers);
    }

    public boolean hasMultipleCorrectAnswers() {
        return correctAnswers.size() > 1;
    }
}
