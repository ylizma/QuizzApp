package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    private RadioGroup choiceslist;
    private Button okButton;
    private TextView question;
    private int choosedNumber;
    private String url;
    private RequestQueue queue;
    private List<QuestionClass> questions = new ArrayList<>();
    private int score = 0;
    private int count = 0;
    private TextView questionnbr;
    private TextView scoretext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent result = this.getIntent();
        choosedNumber = result.getIntExtra("choosednumber", 10);

        choiceslist = findViewById(R.id.answerchoices);
        okButton = findViewById(R.id.buttonAnswer);
        question = findViewById(R.id.QuestionValue);
        questionnbr = findViewById(R.id.questionnumbervalue);
        scoretext = findViewById(R.id.scorevalue);
        startAll();
    }

    void startAll() {
        queue = Volley.newRequestQueue(getApplicationContext());
        url = "https://opentdb.com/api.php?amount=" + this.choosedNumber + "&type=multiple";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject allObject = new JSONObject(response);
                JSONArray objectsArray = allObject.getJSONArray("results");
                initList(objectsArray);
                Log.i("finallist", this.questions.toString());

                startQuizz();


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }, error -> {
        });
        queue.add(stringRequest);
    }

    private void initList(JSONArray objectsArray) {
        Log.i("listobj", objectsArray.toString());
        this.choosedNumber=objectsArray.length();
        for (int i = 0; i < objectsArray.length(); i++) {
            try {
                JSONObject currentobj = objectsArray.getJSONObject(i);
                String question = currentobj.getString("question");
                String correct = currentobj.getString("correct_answer").replace("&amp;","&").replace("&quot;","\"").replace("&#039;","'");
                JSONArray incorectobject = currentobj.getJSONArray("incorrect_answers");
                List<String> incorectlist = getIncorectQuestions(incorectobject);
                QuestionClass questionobject = new QuestionClass(question, correct, incorectlist);

                this.questions.add(questionobject);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    private List<String> getIncorectQuestions(JSONArray incorectobject) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < incorectobject.length(); i++) {
            try {
                String h=incorectobject.getString(i).replace("&amp;","&").replace("&quot;","\"").replace("&#039;","'");
                l.add(h);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return l;
    }

    private void startQuizz() {

        //check number of left questions
        Log.i("listsize", String.valueOf(this.questions.size()));
        QuestionClass currentquestion = nextQuestion(count);//call function that show queston and choices with index count

        this.okButton.setOnClickListener(ev -> {
            Log.i("checked", String.valueOf(this.choiceslist.getCheckedRadioButtonId()));
            if (this.choiceslist.getCheckedRadioButtonId() == -1) {//check if he doesnt choose an answer
                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("You have to choose an answer!")
                        .show();
            } else {
                int answerId = this.choiceslist.getCheckedRadioButtonId();
                RadioButton checkedradio = findViewById(answerId);
                String clientanswer = checkedradio.getText().toString();
                if (currentquestion.getCorrectAnswer().equals(clientanswer)) { //if answer is correct
                    this.score += 1;
                    this.questionnbr.setText(String.valueOf(count + 2));
                    this.scoretext.setText(String.valueOf(this.score));

                    if (count + 1 == choosedNumber) {
                        //restart or quit game
                        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Finish ")
                                .setContentText("Your Score is :" + score + " of " + this.choosedNumber)
                                .setCancelText("Exit")
                                .setConfirmText("Restart")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finish();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finishAffinity();
                                    }
                                })
                                .show();

                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Good Answer!")
                                .setContentText("Next!")
                                .show();

                        ++count;
                        startQuizz();
                    }


                } else {//if the answer is incorrect

                    if (count + 1 == choosedNumber) {

                        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Finish ")
                                .setContentText("Your Score is :" + score + " of " + this.choosedNumber)
                                .setCancelText("Exit")
                                .setConfirmText("Restart")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finish();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finishAffinity();
                                    }
                                })
                                .show();

                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops")
                                .setContentText("the right answer is : " + currentquestion.getCorrectAnswer())
                                .show();

                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Ooops")
                                .setContentText("the right answer is : " + currentquestion.getCorrectAnswer())
                                .show();
                        this.questionnbr.setText(String.valueOf(count + 2));
                        ++count;
                        startQuizz();
                    }

                }
            }
        });

    }

    QuestionClass nextQuestion(int i) { //get the next question object
        choiceslist.removeAllViews();
        choiceslist.clearCheck();
        QuestionClass currentquestion = this.questions.get(i);
        this.question.setText(currentquestion.getQuestion());
        for (String choices : currentquestion.getIncorect()) {
            RadioButton currentchoice = new RadioButton(getApplicationContext());
            currentchoice.setId(View.generateViewId());
            currentchoice.setText(choices);
            currentchoice.setTextColor(getResources().getColor(R.color.bgtextcolor));
            currentchoice.setTextColor(getResources().getColor(R.color.bgtextcolor));
            currentchoice.setHighlightColor(getResources().getColor(R.color.bgtextcolor));

            choiceslist.addView(currentchoice);
        }
        return currentquestion;
    }


}
