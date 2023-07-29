package com.example.fitnesscalculatormobapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class BMRCalculator extends AppCompatActivity {

    private EditText editTextWeight, editTextHeight, editTextAge;
    private RadioButton radioButtonMale, radioButtonFemale;
    private EditText etId;
    private Button buttonCalculate, btnSave, btnUpdate;
    private TextView textViewBMR, textViewBMR2, TextViewSuggest1;

    public static final String URL_UPDATE = "http://192.168.148.118/student/updatebmr.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr_calculator);

        //Toolbar
        TextView title = findViewById(R.id.title);
        //For toolbar back
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        /* Menu Icon */
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        //BMR Variables
        // Initialize views
        etId = findViewById(R.id.id);
        editTextWeight = findViewById(R.id.weight);
        editTextHeight = findViewById(R.id.height);
        editTextAge = findViewById(R.id.age);
        radioButtonMale = findViewById(R.id.radioBtnMale);
        radioButtonFemale = findViewById(R.id.radioBtnFemale);
        buttonCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.BtnSave);
        btnUpdate = findViewById(R.id.BtnSave);
        textViewBMR = findViewById(R.id.tvBMR);
        textViewBMR2 = findViewById(R.id.tvBMR2);
        TextViewSuggest1 = findViewById(R.id.tvBMISuggest1);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMR();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        //Toolbar event to back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalculatorMenu.class);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert the BMI value into the database
                update();
            }
        });

    }

    private void calculateBMR() {
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        int age = Integer.parseInt(editTextAge.getText().toString());

        // Calculate BMR based on gender
        // Using Revised Harris-Benedict Equation
        double bmr;
        if (radioButtonMale.isChecked()) {
            // BMR calculation for males: BMR = 13.397W + 4.799H - 5.677A + 88.362
            bmr = (13.379 * weight) + (4.799 * height) - (5.677 * age) + 88.362;
        } else {
            // BMR calculation for females: BMR = 9.247W + 3.098H - 4.330A + 447.593
            bmr = (9.247 * weight) + (3.098 * height) - (4.33 * age) + 447.593;
            //bmr = 655 + (4.35 * weight) + (4.7 * height) - (4.7 * age);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        // Format the number with two decimal places
        String formattedResult = decimalFormat.format(bmr);

        // Display the calculated BMR
        textViewBMR.setText(formattedResult + " Calories/day");

        btnUpdate.setVisibility(View.VISIBLE);

        if (bmr <= 1618){
            textViewBMR2.setText("Activity Level: Sedentary, little or no exercise");
            TextViewSuggest1.setText("Here are some exercises and recommended durations to help you burn calories:\n" +
                    "\n" +
                    "1. Brisk Walking: Walking is a low-impact exercise that can still be effective in burning calories. Aim for at least 30 minutes of brisk walking every day. \n" +
                    "\n" +
                    "2. Cycling: Cycling is a great cardiovascular exercise that can help burn calories. If you're outdoors, aim for 30-60 minutes of cycling at a moderate pace. \n" +
                    "\n" +
                    "3. Jumping Rope: Jumping rope is a high-intensity exercise that can quickly burn calories. Start with shorter intervals, such as 1-2 minutes of jumping, followed by a short rest period. Repeat this cycle for 15-30 minutes.");
        }
        else if(bmr > 1618 && bmr <= 1854){
            textViewBMR2.setText("Activity Level: Exercise 1-3 times/week");
            TextViewSuggest1.setText("here are some recommended exercises and durations:\n" +
                    "\n" +
                    "1. Cardiovascular Exercise: Engage in moderate-intensity cardiovascular activities to burn calories. Aim for 30 to 60 minutes of activities such as brisk walking, cycling, swimming, aerobic classes, or dancing. \n" +
                    "\n" +
                    "2. High-Intensity Interval Training (HIIT): HIIT workouts are efficient for calorie burning. A typical HIIT session could last for 20 to 30 minutes, including exercises like burpees, high knees, jumping jacks, or squat jumps.\n" +
                    "\n" +
                    "3. Circuit Training: Circuit training combines strength exercises with cardio intervals. Create a circuit with exercises like lunges, push-ups, squats, planks, mountain climbers, and jumping rope. Perform each exercise for a specific duration (e.g., 30 seconds to 1 minute) before moving on to the next one. Complete multiple rounds of the circuit for a total workout duration of 20 to 45 minutes.");
        } else if (bmr > 1854 && bmr <= 1976) {
            textViewBMR2.setText("Activity Level: Exercise 4-5 times/week");
            TextViewSuggest1.setText("Here are some exercises and their approximate minutes that you can consider:\n" +
                    "\n" +
                    "Jumping Rope: 30-40 minutes\n" +
                    "Jumping rope is a high-intensity cardio exercise that can burn a significant amount of calories. Aim for 30-40 minutes of continuous jumping rope to burn approximately 400-500 calories.\n" +
                    "\n" +
                    "Circuit Training: 40-50 minutes\n" +
                    "Zumba is a fun and energetic dance workout that incorporates various dance styles and movements. Join a Zumba class or follow online Zumba sessions for 60 minutes to burn around 400-500 calories.\n" +
                    "\n" +
                    "Step Aerobics: 45-60 minutes\n" +
                    "Step aerobics involves using a raised platform to perform choreographed aerobic exercises. It can be an effective way to burn calories and improve cardiovascular fitness. Join a step aerobics class or follow along with online step aerobics routines for 45-60 minutes to burn approximately 400-500 calories.\n" +
                    "\n" +
                    "Spinning/Cycling: 60-75 minutes\n" +
                    "Spinning or indoor cycling classes provide a challenging cardiovascular workout that can help burn calories. Join a spinning class or use a stationary bike for 60-75 minutes to burn around 400-600 calories.");
        } else if (bmr > 1976 && bmr <= 2090) {
            textViewBMR2.setText("Activity Level: Daily exercise or intense exercise 3-4 times/week");
            TextViewSuggest1.setText("Here are some exercises and their approximate minutes that you can consider:\n" +
                    "\n" +
                    "Running: 30-40 minutes\n" +
                    "Running is a high-impact cardiovascular exercise that can effectively burn calories. Aim for a moderate to fast-paced run for 30-40 minutes to burn approximately 400-500 calories.\n" +
                    "\n" +
                    "HIIT (High-Intensity Interval Training): 30-45 minutes\n" +
                    "HIIT workouts involve short bursts of intense exercise followed by periods of rest or low-intensity exercise. These workouts are known for their calorie-burning benefits. Perform a 30-45 minute HIIT session, alternating between exercises like jumping jacks, burpees, mountain climbers, and high knees.\n" +
                    "\n" +
                    "Swimming: 45-60 minutes\n" +
                    "Swimming is a full-body workout that can burn a significant amount of calories. Whether it's freestyle, breaststroke, or butterfly, swim continuously for 45-60 minutes to burn around 400-600 calories.\n" +
                    "\n" +
                    "Kickboxing: 60 minutes\n" +
                    "Kickboxing workouts involve a combination of punches, kicks, and cardio exercises, making them a great option for calorie burn. Join a kickboxing class or follow online kickboxing workouts for 60 minutes to burn approximately 500-600 calories.");
        } else if (bmr > 2090 && bmr <= 2326) {
            textViewBMR2.setText("Activity Level: Intense exercise 6-7 times/week");
            TextViewSuggest1.setText("Here are some exercises and their approximate minutes that you can consider:\n" +
                    "\n" +
                    "High-Intensity Interval Training (HIIT): 45-60 minutes\n" +
                    "HIIT workouts are intense and effective for burning calories. Incorporate exercises like burpees, mountain climbers, squat jumps, and high knees into your routine. Alternate between high-intensity bursts and short rest periods for 45-60 minutes to burn approximately 500-600 calories.\n" +
                    "\n" +
                    "Cycling (Outdoor or Indoor): 60-75 minutes\n" +
                    "Cycling is a great cardio exercise that can help burn a significant amount of calories. Whether you're cycling outdoors or using a stationary bike indoors, aim for 60-75 minutes of continuous cycling at a moderate to high intensity to burn around 500-700 calories.\n" +
                    "\n" +
                    "Kickboxing: 60 minutes\n" +
                    "Kickboxing workouts combine martial arts techniques with cardio exercises, providing a high-intensity workout. Punch, kick, and perform combinations for 60 minutes to burn around 500-600 calories.\n" +
                    "\n" +
                    "Jumping Rope: 45-60 minutes\n" +
                    "Jumping rope is a simple yet effective cardio exercise. Jump continuously for 45-60 minutes, varying your speed and intensity, to burn around 500-600 calories.");
        } else if (bmr > 2326 && bmr <= 2562) {
            textViewBMR2.setText("Activity Level Very intense exercise daily, or physical job");
            TextViewSuggest1.setText("Here are some exercises and their approximate minutes that you can consider:\n" +
                    "\n" +
                    "Running: 60-75 minutes\n" +
                    "Running is a high-impact cardiovascular exercise that can help burn a significant amount of calories. Aim for a moderate to fast-paced run for 60-75 minutes to burn approximately 700-800 calories.\n" +
                    "\n" +
                    "Cycling (Outdoor or Indoor): 75-90 minutes\n" +
                    "Cycling is a great calorie-burning exercise. Whether you're cycling outdoors or using a stationary bike indoors, aim for 75-90 minutes of continuous cycling at a moderate to high intensity to burn around 700-900 calories.\n" +
                    "\n" +
                    "HIIT (High-Intensity Interval Training): 60 minutes\n" +
                    "HIIT workouts involve short bursts of intense exercise followed by periods of rest or low-intensity exercise. Incorporate exercises like burpees, squat jumps, high knees, and mountain climbers into your routine. Perform a 60-minute HIIT session to burn approximately 700-800 calories.\n" +
                    "\n" +
                    "Swimming: 75-90 minutes\n" +
                    "Swimming is a full-body workout that can burn a significant amount of calories. Whether it's freestyle, breaststroke, or butterfly, swim continuously for 75-90 minutes to burn around 700-900 calories.\n" +
                    "\n" +
                    "Kickboxing: 75-90 minutes\n" +
                    "Kickboxing workouts combine martial arts techniques with cardio exercises, providing a high-intensity workout. Punch, kick, and perform combinations for 75-90 minutes to burn approximately 700-800 calories.");
        } else {
            TextViewSuggest1.setText("To burn calories greater than 2526, you can incorporate high-intensity exercises that engage multiple muscle groups and elevate your heart rate. Here are some exercises and their approximate durations to help you achieve that calorie burn:\n" +
                    "\n" +
                    "Running: 75-90 minutes\n" +
                    "Running is an excellent calorie-burning exercise. Aim for a sustained, moderate to fast-paced run for 75-90 minutes to burn over 900 calories.\n" +
                    "\n" +
                    "Cycling (Outdoor or Indoor): 90-120 minutes\n" +
                    "Cycling is a great cardiovascular workout. Whether you're cycling outdoors or using a stationary bike indoors, aim for 90-120 minutes of continuous cycling at a moderate to high intensity to burn more than 900 calories.\n" +
                    "\n" +
                    "HIIT (High-Intensity Interval Training): 60-75 minutes\n" +
                    "HIIT workouts involve intense bursts of exercise followed by short rest periods. Include exercises like burpees, squat jumps, mountain climbers, and high knees in your routine. Perform a 60-75 minute HIIT session to burn more than 900 calories.\n" +
                    "\n" +
                    "Swimming: 90-120 minutes\n" +
                    "Swimming is a low-impact, full-body exercise that can help burn a significant number of calories. Swim continuously for 90-120 minutes, varying your strokes and intensities, to burn more than 900 calories.");
        }

    }

    public void update() {
        final String id = etId.getText().toString();
        final String bmr = textViewBMR.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(BMRCalculator.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pdLoading.setMessage("\tUpdating...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Nullable
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(URL_UPDATE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                    writer.write(getPostDataString(id, bmr));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        return response.toString();
                    } else {
                        return "Error: " + responseCode;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        // Update the TextViews with the new data
                    /*tvName.setText("Age: " + name);
                    tvYear.setText("Weight: " + year);
                    tvNum.setText("Height: " + number);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BMRCalculator.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private String getPostDataString(String id, String bmr) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(URLEncoder.encode("id", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(id, "UTF-8"));
        result.append("&");
        result.append(URLEncoder.encode("bmr", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(bmr, "UTF-8"));
        return result.toString();
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(BMRCalculator.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(BMRCalculator.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(BMRCalculator.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(BMRCalculator.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}