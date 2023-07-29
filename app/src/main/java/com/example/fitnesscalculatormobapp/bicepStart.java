package com.example.fitnesscalculatormobapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class bicepStart extends AppCompatActivity {

    TextView intropage, subintropage, fitonetitle, fitonedesc, timerValue, btnexercise;
    View divpage, bgprogress;
    LinearLayout fitone;
    ImageView imgTimer;

    private GifImageView gifImageView;

    private static final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    private String[] exerciseTitles = {"Push-ups Shoulder Taps", "Diamond Push-ups", "Reverse Hand Push-ups", "Plank Up-down"};
    private int exerciseCounter = 0;

    private static final long WORKOUT_TIME = 30000; // 30 seconds

    private Button btnStart;
    private Button btnReset;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bicep_start);

        subintropage = (TextView) findViewById(R.id.subintropage);
        fitonetitle = (TextView) findViewById(R.id.fitonetitle);
        fitonedesc = (TextView) findViewById(R.id.fitonedesc);
        timerValue = (TextView) findViewById(R.id.timerValue);
        btnexercise = (TextView) findViewById(R.id.btnexercise);

        divpage = (View) findViewById(R.id.divpage);
        bgprogress = (View) findViewById(R.id.bgprogress);

        fitone = (LinearLayout) findViewById(R.id.fitone);

        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);

        fitonetitle.setText(exerciseTitles[exerciseCounter]);

        gifImageView = findViewById(R.id.gifImageView);

        btnexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the counter
                exerciseCounter++;

                // Check if the counter is within the bounds of the exercise titles array
                if (exerciseCounter < exerciseTitles.length) {
                    // Set the new title
                    fitonetitle.setText(exerciseTitles[exerciseCounter]);

                    // Set the duration for the next exercise
                    switch (exerciseCounter) {
                        case 0: // shoulder taps
                            mTimeLeftInMillis = WORKOUT_TIME;
                            gifImageView.setImageResource(R.drawable.shoulder_taps);
                            break;
                        case 1: // Diamond
                            mTimeLeftInMillis = WORKOUT_TIME;
                            gifImageView.setImageResource(R.drawable.diamond_pushups);
                            break;
                        case 2: // Reverse hand push ups
                            mTimeLeftInMillis = WORKOUT_TIME;
                            gifImageView.setImageResource(R.drawable.reverse_hand_pushups);
                            break;
                        case 3: // Plank up down
                            mTimeLeftInMillis = WORKOUT_TIME;
                            gifImageView.setImageResource(R.drawable.plank_up_down);
                            break;
                    }

                    countDownTimer.cancel();
                    mTimerRunning = false;
                    updateCountDownText();
                    startTimer();

                    btnStart.setEnabled(true);
                } else {
                    // Handle the case when all exercises are completed
                    Toast.makeText(getApplicationContext(), "Workout completed!", Toast.LENGTH_SHORT).show();
                    // You can add any additional logic or UI changes here
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
                case 0: // Regular Squat
                    gifImageView.setImageResource(R.drawable.shoulder_taps);
                    mTimeLeftInMillis = WORKOUT_TIME;
                    break;
                case 1: // High Knee Run
                    gifImageView.setImageResource(R.drawable.diamond_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
                case 2: // Reverse Lunge
                    gifImageView.setImageResource(R.drawable.reverse_hand_pushups);
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                    break;
                case 3: // Curtsey Squat
                    gifImageView.setImageResource(R.drawable.plank_up_down);
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