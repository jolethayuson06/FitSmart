package com.example.fitnesscalculatormobapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class StartWorkAct extends AppCompatActivity {

    TextView intropage, subintropage, fitonetitle, fitonedesc, timerValue, btnexercise;
    View divpage, bgprogress;
    Button btnStart, btnReset;
    GifImageView gifImageView;

    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private String[] exerciseTitles = {"Alternating Push-ups", "Diamond Push-ups", "Incline Push-ups", "Wide Push-ups"};
    private int exerciseCounter = 0;

    private static final long WORKOUT_TIME = 30000; // 30 seconds

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        /*intropage = findViewById(R.id.intropage);*/
        subintropage = findViewById(R.id.subintropage);
        fitonetitle = findViewById(R.id.fitonetitle);
        fitonedesc = findViewById(R.id.fitonedesc);
        timerValue = findViewById(R.id.timerValue);
        btnexercise = findViewById(R.id.btnexercise);

        divpage = findViewById(R.id.divpage);
        bgprogress = findViewById(R.id.bgprogress);

        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);

        fitonetitle.setText(exerciseTitles[exerciseCounter]);

        gifImageView = findViewById(R.id.gifImageView);

        btnexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseCounter++;

                if (exerciseCounter < exerciseTitles.length) {
                    fitonetitle.setText(exerciseTitles[exerciseCounter]);

                    switch (exerciseCounter) {
                        case 1: // Incline Push-ups
                            mTimeLeftInMillis = START_TIME_IN_MILLIS;
                            gifImageView.setImageResource(R.drawable.diamod_pushups);
                            break;
                        case 2: // Decline Push-ups
                            mTimeLeftInMillis = START_TIME_IN_MILLIS;
                            gifImageView.setImageResource(R.drawable.incline_pushups);
                            break;
                        case 3: // Forearm Push-ups
                            mTimeLeftInMillis = START_TIME_IN_MILLIS;
                            gifImageView.setImageResource(R.drawable.wide_pushups);
                            break;
                        default: // Regular Push-ups (first exercise)
                            mTimeLeftInMillis = START_TIME_IN_MILLIS;
                            gifImageView.setImageResource(R.drawable.alternating_shuffle_pushups);
                            break;
                    }

                    resetTimer();
                    updateCountDownText();

                    btnStart.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Workout completed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                btnStart.setEnabled(true);
                btnStart.setText("Start");
            }
        });
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mTimerRunning = false;
        mTimeLeftInMillis = WORKOUT_TIME; // Set the duration to 30 seconds
        updateCountDownText();
    }


    private void startTimer() {
        if (mTimerRunning) {
            pauseTimer();
        } else {
            resumeTimer();
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        mTimerRunning = false;
        btnStart.setText("Start");
    }

    private void resumeTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                moveToNextExercise();
                startTimer();
            }
        }.start();
        mTimerRunning = true;
        btnStart.setText("Pause");
    }

    private void moveToNextExercise() {
        exerciseCounter++;
        if (exerciseCounter < exerciseTitles.length) {
            fitonetitle.setText(exerciseTitles[exerciseCounter]);
            switch (exerciseCounter) {
                case 1: // Incline Push-ups
                    gifImageView.setImageResource(R.drawable.diamod_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
                case 2: // Decline Push-ups
                    gifImageView.setImageResource(R.drawable.incline_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
                case 3: // Forearm Push-ups
                    gifImageView.setImageResource(R.drawable.wide_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
                default: // Regular Push-ups (first exercise)
                    gifImageView.setImageResource(R.drawable.alternating_shuffle_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
            }
            resetTimer();
            updateCountDownText();
        } else {
            Toast.makeText(getApplicationContext(), "Workout completed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerValue.setText(timeLeft);
    }
}
