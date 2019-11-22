package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends LoggingActivity {

    private static final String KEY_CURRENT_INDEX = "key_current_index";
    private static final String KEY_ARRAY_ANSWERS = "key_array_answers";
    private static final String KEY_NUMBER_OF_ANSWERS = "key_number_of_answers";
    private static final String KEY_NUMBER_OF_CORRECT_ANSWERS = "key_number_of_correct_answers";
    private static final String NO_ANSWER = "no_answer";
    private static final String CORRECT_ANSWER = "correct_answer";
    private static final String INCORRECT_ANSWER = "incorrect_answer";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int numberOfAnswers = 0;
    private int numberOfCorrectAnswers = 0;
    private int numberOfQuestions = mQuestionBank.length;

    private String[] answers = new String[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int pos = 0; pos < mQuestionBank.length; pos++ ){
            answers[pos] = NO_ANSWER;
        }


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            answers = savedInstanceState.getStringArray(KEY_ARRAY_ANSWERS);
            numberOfAnswers = savedInstanceState.getInt(KEY_NUMBER_OF_ANSWERS);
            numberOfCorrectAnswers = savedInstanceState.getInt(KEY_NUMBER_OF_CORRECT_ANSWERS);
        }

        final TextView questionString = findViewById(R.id.question_string);
        final Question currentQuestion = mQuestionBank[mCurrentIndex];
        questionString.setText(currentQuestion.getQuestionResId());

        Button trueButton = findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(true);
            }
        });

        Button falseButton = findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(false);
            }
        });

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                final Question currentQuestion = mQuestionBank[mCurrentIndex];
                questionString.setText(currentQuestion.getQuestionResId());
            }
        });

        Button checkButton = findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toastMessage = String.format("Answered %d/%d questions\n " +
                                      "Correct answers: %d", numberOfAnswers, numberOfQuestions, numberOfCorrectAnswers);

                Toast.makeText(
                        getApplicationContext(),
                        toastMessage,
                        Toast.LENGTH_LONG
                ).show();
            }
        });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putStringArray(KEY_ARRAY_ANSWERS,answers);
        outState.putInt(KEY_NUMBER_OF_ANSWERS, numberOfAnswers);
        outState.putInt(KEY_NUMBER_OF_CORRECT_ANSWERS, numberOfCorrectAnswers);
    }

    private void onButtonClicked(boolean answer) {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        int toastMessage = (currentQuestion.isCorrectAnswer() == answer) ?
                R.string.correct_toast :
                R.string.incorrect_toast;

        Toast.makeText(
                MainActivity.this,
                toastMessage,
                Toast.LENGTH_LONG
        ).show();

        if (answers[mCurrentIndex] == NO_ANSWER){
            if (currentQuestion.isCorrectAnswer() == answer){
                numberOfCorrectAnswers++;
                numberOfAnswers++;
                answers[mCurrentIndex] = CORRECT_ANSWER;
            } else {
                numberOfAnswers++;
                answers[mCurrentIndex] = INCORRECT_ANSWER;
            }
        }

        if (answers[mCurrentIndex] == CORRECT_ANSWER){
            if (currentQuestion.isCorrectAnswer() != answer){
                numberOfCorrectAnswers--;
                answers[mCurrentIndex] = INCORRECT_ANSWER;
            }
        }

        if (answers[mCurrentIndex] == INCORRECT_ANSWER){
            if (currentQuestion.isCorrectAnswer() == answer){
                numberOfCorrectAnswers++;
                answers[mCurrentIndex] = CORRECT_ANSWER;
            }
        }
    }
}
