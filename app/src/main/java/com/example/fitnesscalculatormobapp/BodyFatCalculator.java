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

public class BodyFatCalculator extends AppCompatActivity {

    private EditText etWaist, etHeight, etWeight, etNeck, etHip;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button btnCalculate, btnSave;
    private EditText etId;
    private TextView tvBodyFat, tvIntro, tvSuggest1, tvReference, tvLink;
    public static final String URL_UPDATE = "http://192.168.148.118/student/updatebodyfat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_army_body_fat);

        //For Toolbar
        TextView title = findViewById(R.id.title);
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        //For Computing
        etId = findViewById(R.id.id);
        radioButtonMale = findViewById(R.id.radioBtnMale);
        radioButtonFemale = findViewById(R.id.radioBtnFemale);
        etWeight = findViewById(R.id.weight);
        etHeight = findViewById(R.id.height);
        etNeck = findViewById(R.id.neck);
        etHip = findViewById(R.id.hip);
        etWaist = findViewById(R.id.waist);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave =findViewById(R.id.BtnSave);
        tvBodyFat = findViewById(R.id.tvBodyFat);
        tvIntro = findViewById(R.id.tvIntro);
        tvSuggest1 = findViewById(R.id.tvSuggest1);
        tvReference = findViewById(R.id.tvReference);
        tvLink = findViewById(R.id.tvLink);

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
        /* Menu Icon */
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        radioButtonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonFemale.isChecked()) {
                    etHip.setVisibility(View.VISIBLE);
                } else {
                    etHip.setVisibility(View.GONE);
                }
            }
        });

        radioButtonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonMale.isChecked()) {
                    etHip.setVisibility(View.GONE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert the BMI value into the database
                update();
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //float hip = Float.parseFloat(etHip.getText().toString());
                float neck = Float.parseFloat(etNeck.getText().toString());
                float waist = Float.parseFloat(etWaist.getText().toString());
                float height = Float.parseFloat(etHeight.getText().toString());

                double bodyFatPercentage;
                //double leanBodyMass;

                if (radioButtonMale.isChecked()) {
                    bodyFatPercentage = (495 / (1.0324 - 0.19077 * Math.log10(waist - neck) + 0.15456 * Math.log10(height))) - 450;

                    if (bodyFatPercentage < 6) {
                        tvIntro.setText("Your Body Fat Category is Essential Fat");
                        tvSuggest1.setText("For individuals in the essential fat category, which represents the minimal amount of body fat required for healthy bodily functions, here are some recommendations:\n" +
                                "\n" +
                                "1.Maintain a balanced and nutritious diet.\n" +
                                "2.Engage in regular exercise to maintain overall health and fitness\n" +
                                "3.Stay hydrated.\n" +
                                "4.Prioritize quality sleep.\n" +
                                "5.Manage stress levels.\n" +
                                "\n" +
                                "It is important to emphasize the significance of maintaining the necessary levels of essential body fat for proper physiological functioning. However, it is equally important to prioritize overall health and well-being instead of solely fixating on body fat percentages. Seeking personalized advice and guidance from healthcare professionals or registered dietitians based on your specific needs and goals is highly recommended.");
                    } else if (bodyFatPercentage >= 6 && bodyFatPercentage <= 13) {
                        tvIntro.setText("Your Body Fat Category is Athlete");
                        tvSuggest1.setText("For individuals in the athlete category of body fat, it is important to maintain a balance between body composition and performance. Here are some recommendations:\n" +
                                "\n" +
                                "1.Consume a well-balanced diet that provides the necessary nutrients to support athletic performance.\n" +
                                "2.Stay hydrated.\n" +
                                "3.Strength and resistance training.\n" +
                                "4.Cardiovascular exercise.\n" +
                                "5.Monitor body composition.\n" +
                                "\n" +
                                "Remember, individualized recommendations may vary based on specific sports, training goals, and individual needs. Consulting with professionals who specialize in sports nutrition and training can provide valuable insights and help you optimize your performance.");

                    } else if (bodyFatPercentage >= 14 && bodyFatPercentage <= 17) {
                        tvIntro.setText("Your Body Fat Category is Fitness");
                        tvSuggest1.setText("For individuals in the fitness category of body fat, the focus is on maintaining a healthy body composition and overall fitness level. Here are some recommendations:\n" +
                                "\n" +
                                "1.Balanced nutrition.\n" +
                                "2.Engage in a combination of cardiovascular exercises, strength training, and flexibility exercises.\n" +
                                "3.Be mindful of portion sizes to avoid overeating.\n" +
                                "4.Stay properly hydrated.\n" +
                                "5.Monitor body composition.\n" +
                                "\n" +
                                "Remember, the fitness category of body fat is about maintaining a healthy and balanced lifestyle rather than focusing solely on weight or body fat percentages. Prioritize overall well-being and make sustainable lifestyle choices that promote long-term health.");

                    }else if (bodyFatPercentage >= 18 && bodyFatPercentage <= 24) {
                        tvIntro.setText("Your Body Fat Category is Average");
                        tvSuggest1.setText("For individuals in the average category of body fat, the goal is to maintain a healthy body composition and overall well-being. Here are some recommendations:\n" +
                                "\n" +
                                "1.Follow a balanced and nutritious diet.\n" +
                                "2.Engage in regular physical activity to maintain overall fitness and health.\n" +
                                "3.Pay attention to portion sizes and practice mindful eating.\n" +
                                "4.Stay hydrated.\n" +
                                "5. Practice stress management techniques such as meditation, yoga, or deep breathing exercises.\n" +
                                "\n" +
                                "It is important to note that being in the average category of body fat is considered healthy. However, instead of fixating solely on body fat percentages, it is crucial to prioritize your overall well-being and prioritize a healthy lifestyle. Emphasize sustainable habits that contribute to long-term health and happiness.");

                    }else{
                        tvIntro.setText("Your Body Fat Category is Obese");
                        tvSuggest1.setText("For individuals in the average category of body fat, the goal is to maintain a healthy body composition and overall well-being. Here are some recommendations:\n" +
                                "\n" +
                                "1.Consult with a healthcare professional or registered dietitian.\n" +
                                "2. Focus on consuming fewer calories than you burn through a combination of diet and exercise.\n" +
                                "3.Limit your intake of processed foods, sugary snacks, and drinks.\n" +
                                "4.Engage in regular aerobic exercises such as walking, jogging, cycling, or swimming.\n" +
                                "5.Be mindful of the amount of food you consume in each meal.\n" +
                                "6.Changing your lifestyle habits takes time and dedication. \n" +
                                "\n" +
                                "Remember, it is advisable to seek professional advice and support when dealing with obesity to ensure safe and effective progress towards a healthier body composition.");

                    }

                } else {
                    float hip = Float.parseFloat(etHip.getText().toString());
                    bodyFatPercentage = (495 / (1.29579 - 0.35004 * Math.log10(waist + hip - neck) + 0.22100 * Math.log10(height))) - 450;

                    if (bodyFatPercentage < 13) {
                        tvIntro.setText("Your Body Fat Category is Essential Fat");
                        tvSuggest1.setText("For individuals in the essential fat category, which represents the minimal amount of body fat required for healthy bodily functions, here are some recommendations:\n" +
                                "\n" +
                                "1.Maintain a balanced and nutritious diet.\n" +
                                "2.Engage in regular exercise to maintain overall health and fitness\n" +
                                "3.Stay hydrated.\n" +
                                "4.Prioritize quality sleep.\n" +
                                "5.Manage stress levels.\n" +
                                "\n" +
                                "It is important to emphasize the significance of maintaining the necessary levels of essential body fat for proper physiological functioning. However, it is equally important to prioritize overall health and well-being instead of solely fixating on body fat percentages. Seeking personalized advice and guidance from healthcare professionals or registered dietitians based on your specific needs and goals is highly recommended.");
                    } else if (bodyFatPercentage >= 14 && bodyFatPercentage <= 20) {
                        tvIntro.setText("Your Body Fat Category is Athlete");
                        tvSuggest1.setText("For individuals in the athlete category of body fat, it is important to maintain a balance between body composition and performance. Here are some recommendations:\n" +
                                "\n" +
                                "1.Consume a well-balanced diet that provides the necessary nutrients to support athletic performance.\n" +
                                "2.Stay hydrated.\n" +
                                "3.Strength and resistance training.\n" +
                                "4.Cardiovascular exercise.\n" +
                                "5.Monitor body composition.\n" +
                                "\n" +
                                "Remember, individualized recommendations may vary based on specific sports, training goals, and individual needs. Consulting with professionals who specialize in sports nutrition and training can provide valuable insights and help you optimize your performance.");

                    } else if (bodyFatPercentage >= 21 && bodyFatPercentage <= 24) {
                        tvIntro.setText("Your Body Fat Category is Fitness");
                        tvSuggest1.setText("For individuals in the fitness category of body fat, the focus is on maintaining a healthy body composition and overall fitness level. Here are some recommendations:\n" +
                                "\n" +
                                "1.Balanced nutrition.\n" +
                                "2.Engage in a combination of cardiovascular exercises, strength training, and flexibility exercises.\n" +
                                "3.Be mindful of portion sizes to avoid overeating.\n" +
                                "4.Stay properly hydrated.\n" +
                                "5.Monitor body composition.\n" +
                                "\n" +
                                "Remember, the fitness category of body fat is about maintaining a healthy and balanced lifestyle rather than focusing solely on weight or body fat percentages. Prioritize overall well-being and make sustainable lifestyle choices that promote long-term health.");

                    }else if (bodyFatPercentage >= 25 && bodyFatPercentage <= 31) {
                        tvIntro.setText("Your Body Fat Category is Average");
                        tvSuggest1.setText("For individuals in the average category of body fat, the goal is to maintain a healthy body composition and overall well-being. Here are some recommendations:\n" +
                                "\n" +
                                "1.Follow a balanced and nutritious diet.\n" +
                                "2.Engage in regular physical activity to maintain overall fitness and health.\n" +
                                "3.Pay attention to portion sizes and practice mindful eating.\n" +
                                "4.Stay hydrated.\n" +
                                "5. Practice stress management techniques such as meditation, yoga, or deep breathing exercises.\n" +
                                "\n" +
                                "It is important to note that being in the average category of body fat is considered healthy. However, instead of fixating solely on body fat percentages, it is crucial to prioritize your overall well-being and prioritize a healthy lifestyle. Emphasize sustainable habits that contribute to long-term health and happiness.");

                    }else{
                        tvIntro.setText("Your Body Fat Category is Obese");
                        tvSuggest1.setText("For individuals in the average category of body fat, the goal is to maintain a healthy body composition and overall well-being. Here are some recommendations:\n" +
                                "\n" +
                                "1.Consult with a healthcare professional or registered dietitian.\n" +
                                "2. Focus on consuming fewer calories than you burn through a combination of diet and exercise.\n" +
                                "3.Limit your intake of processed foods, sugary snacks, and drinks.\n" +
                                "4.Engage in regular aerobic exercises such as walking, jogging, cycling, or swimming.\n" +
                                "5.Be mindful of the amount of food you consume in each meal.\n" +
                                "6.Changing your lifestyle habits takes time and dedication. \n" +
                                "\n" +
                                "Remember, it is advisable to seek professional advice and support when dealing with obesity to ensure safe and effective progress towards a healthier body composition.");

                    }

                }

                // Round the result to two decimal places
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String result = decimalFormat.format(bodyFatPercentage);

                btnSave.setVisibility(View.VISIBLE);
                tvBodyFat.setText(result + "%");
                tvReference.setText("Calculator.net - Body Fat Calculator");
                tvLink.setText("https://www.calculator.net/body-fat-calculator.html?ctype=metric&csex=f&cage=25&cweightlbs=152&cheightfeet=5&cheightinch=10.5&cneckfeet=1&cneckinch=7.5&cwaistfeet=3&cwaistinch=1.5&chipfeet=2&chipinch=10.5&cweightkgs=70&cheightmeter=178&cneckmeter=50&cwaistmeter=96&chipmeter=92&x=48&y=22");
            }
        });

    }

    public void update() {
        final String id = etId.getText().toString();
        final String bodyfat = tvBodyFat.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(BodyFatCalculator.this);

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
                    writer.write(getPostDataString(id, bodyfat));
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
                    Toast.makeText(BodyFatCalculator.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private String getPostDataString(String id, String bodyfat) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(URLEncoder.encode("id", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(id, "UTF-8"));
        result.append("&");
        result.append(URLEncoder.encode("bodyfat", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(bodyfat, "UTF-8"));
        return result.toString();
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(BodyFatCalculator.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(BodyFatCalculator.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(BodyFatCalculator.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(BodyFatCalculator.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}