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

public class IdealWeightCalculator extends AppCompatActivity {

    private EditText etId, etAge, etHeight;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button btnCalculate, btnSave;
    private TextView tvIdeal, tvIntro, tvSuggest1, tvReference, tvLink;
    public static final String URL_UPDATE = "http://192.168.148.118/student/updateidealweight.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideal_weight_calculator);

        //For Toolbar
        TextView title = findViewById(R.id.title);
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        etId = findViewById(R.id.id);
        etAge = findViewById(R.id.age);
        etHeight = findViewById(R.id.height);
        radioButtonMale = findViewById(R.id.radioBtnMale);
        radioButtonFemale = findViewById(R.id.radioBtnFemale);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.BtnSave);
        tvIdeal = findViewById(R.id.tvIdeal);
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

        /* Menu Icon */
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
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

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float age = Float.parseFloat((etAge.getText().toString()));
                float height = Float.parseFloat(etHeight.getText().toString());

                double idealWeight;

                if (radioButtonMale.isChecked()) {
                    // For males
                    idealWeight = 48.0 + 2.7 * ((height - 152.4) / 2.54);

                } else if (radioButtonFemale.isChecked()){
                    // For females
                    idealWeight = 45.5 + 2.2 * ((height - 152.4) / 2.54);
                } else{
                    // Handle invalid input for sex
                    // You can display an error message or provide a default value
                    idealWeight = 0.0;
                    tvIdeal.setText("Something went wrong");
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String result = decimalFormat.format(idealWeight);

                btnSave.setVisibility(View.VISIBLE);
                tvIdeal.setText(result + "kg");
                }
        });

        tvIntro.setText("How Much Should I Weigh?");
        tvSuggest1.setText("Many individuals have attempted weight loss at some point in their lives or have known someone who has. This desire to shed pounds is often influenced by the idea of an \"ideal\" body weight, which is frequently shaped by societal standards portrayed in media outlets such as social media, TV, movies, and magazines. Although the concept of ideal body weight (IBW) is sometimes associated with visual attractiveness today, its original purpose was to determine appropriate medication dosages. The formulas used to calculate IBW have no direct connection to a person's physical appearance at a specific weight. Moreover, it has been discovered that the metabolism of certain drugs is more influenced by IBW than by overall body weight. Presently, IBW is widely used in sports to categorize individuals based on their weight.\n"+
                "\n" +
                "However, it is important to note that IBW is not a flawless measurement as it disregards body fat and muscle percentages. Consequently, highly fit and healthy athletes may be classified as overweight based on their IBW. Therefore, it is crucial to view IBW with the understanding that it is an imperfect measure and not necessarily indicative of one's health or a weight that individuals should strive to achieve. It is possible to be above or below your \"IBW\" and still maintain excellent health.");
        tvReference.setText("Reference: \nCalculator.net - Ideal Weight Calculator");
        tvLink.setText("https://www.calculator.net/ideal-weight-calculator.html?ctype=metric&cage=21&csex=f&cheightfeet=5&cheightinch=10&cheightmeter=158&printit=0&x=73&y=31");

    }

    public void update() {
        final String id = etId.getText().toString();
        final String idealweight = tvIdeal.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(IdealWeightCalculator.this);

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
                    writer.write(getPostDataString(id, idealweight));
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
                    Toast.makeText(IdealWeightCalculator.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private String getPostDataString(String id, String idealweight) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(URLEncoder.encode("id", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(id, "UTF-8"));
        result.append("&");
        result.append(URLEncoder.encode("idealweight", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(idealweight, "UTF-8"));
        return result.toString();
    }


    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(IdealWeightCalculator.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(IdealWeightCalculator.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(IdealWeightCalculator.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(IdealWeightCalculator.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}