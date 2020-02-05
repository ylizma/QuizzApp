package com.example.quizzapp;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionClass {
    private String question;
    private String correctAnswer;
    private String clientAnswer;
    private List<String> incorect;

    public QuestionClass(String question, String correctAnswer, List<String> incorect) {
        this.question = question.replace("&amp;","&").replace("&quot;","\"");
        this.correctAnswer = correctAnswer;
        this.incorect = incorect;
        this.incorect.add(correctAnswer);
        Collections.shuffle(this.incorect);
    }

    @NonNull
    @Override
    public String toString() {
        return this.question;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getClientAnswer() {
        return clientAnswer;
    }

    public List<String> getIncorect() {
        return incorect;
    }
}
