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

public class BodyTypeCalculator extends AppCompatActivity {

    private EditText etId, editTextBust, editTextWaist, editTextHip;
    private Button btn, btnSave;
    private TextView result, intro;

    public static final String URL_UPDATE = "http://192.168.148.118/student/updatebodytype.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_type_calculator);

        //Toolbar
        TextView title = findViewById(R.id.title);
        //For toolbar back
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        //Calculate
        etId = findViewById(R.id.id);
        editTextBust = findViewById(R.id.bust);
        editTextWaist = findViewById(R.id.waist);
        editTextHip = findViewById(R.id.hip);
        btn = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.BtnSave);
        result = findViewById(R.id.tvBodyTypeResult);
        intro = findViewById(R.id.tvIntro);
        TextView suggest1 = findViewById(R.id.tvBodyTypeSuggest1);
        TextView reference = findViewById(R.id.tvReference);
        TextView link = findViewById(R.id.tvLink);

        //Toolbar
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

        //Calculate
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float bust = Float.parseFloat(String.valueOf(editTextBust.getText()));
                float waist = Float.parseFloat(String.valueOf(editTextWaist.getText()));
                float hip = Float.parseFloat(String.valueOf(editTextHip.getText()));

                float waistToHipRatio = waist / hip;

                String bodyShapeType;

                btnSave.setVisibility(View.VISIBLE);

                if (waistToHipRatio <= 0.7) {
                    if (bust >= 100) {
                        bodyShapeType = "Apple";
                        result.setText(bodyShapeType);
                        intro.setText("Understanding the Apple Body Shape");
                        suggest1.setText("The “apple” body shape is known in the scientific community as “android,” meaning that most of the fat is stored in the midsection and less fat is stored in the hips, buttocks, and thighs.\n" +
                                "\n" +
                                "People with android body types tend to have a larger waist-to-hip ratio, meaning their waist is larger or close to equivalent in circumference to their hips.\n" +
                                "\n" +
                                "Does body shape affect your health?\n" +
                                "First things first: The way a person’s body looks does not automatically tell you whether they are healthy.\n" +
                                "\n" +
                                "That said, certain body shapes may be at an increased risk of negative health outcomes, according to numerous research studies.\n" +
                                "\n" +
                                "One 2020 review of 72 studies found that people with greater fat distribution in the stomach area (an apple shape) had a significantly higher risk of death from all causes than those with pear-shaped bodies.\n"+
                                "\n" +
                                "In one 2019 study involving 2,683 postmenopausal women, those who had an apple body type — more fat in the midsection and less fat in the legs — were three times more likely to have heart disease than those with a pear body type.");
                        reference.setText("Healthline");
                        link.setText("https://www.healthline.com/health/apple-body-shape#limitations-of-fruit-metaphors");
                    } else {
                        bodyShapeType = "Pear";
                        result.setText("Body Shape = " + bodyShapeType );
                        intro.setText("Health Risks For A Pear-Shaped Person Based on The Health Site");
                        suggest1.setText("A pear-shaped individual, also referred to as a triangle-shaped or gynoid-shaped person, has a body structure where the hips are usually wider than the bust. The waistline is well-defined, while the neck, arms, and shoulders tend to be slim. However, excess body fat tends to accumulate around the hips, buttocks, and thighs. In this body type, weight gain typically begins in the lower body and progresses to the legs before accumulating around the waist and eventually the upper body. The term \"gynoid shape\" is commonly associated with women due to the prevalence of this body shape among them.\n");
                        reference.setText("The Health Site");
                        link.setText("https://www.thehealthsite.com/diseases-conditions/health-risks-for-pear-shaped-person-ag0517-488370/");
                    }
                } else if (waistToHipRatio <= 0.75) {
                    bodyShapeType = "Hourglass";
                    result.setText(bodyShapeType );
                    intro.setText("Here's How To Work Toward an Hourglass Shape or Maintain Your Hourglass Body Based on Healthline");
                    suggest1.setText("An hourglass figure typically consists of a smaller waist balanced by a larger bust and curvier hips. That means there are three target areas to work on for more of an hourglass shape:\n" +
                            "\n" +
                            "\t- your upper body\n" +
                            "\t- your waist\n" +
                            "\t- your glutes, upper thighs, and hips\n" +
                            "The emphasis of what you work on will depend on your natural shape.\n" +
                            "\n" +
                            "If you’re already slim all around, you may want to start by building muscle around your shoulders and chest area to get broader on top. If you carry weight around your midsection, you’ll likely want to prioritize whittling that away.");
                    reference.setText("Healthline");
                    link.setText("https://www.healthline.com/health/body/how-to-get-an-hourglass-figure#three-target-areas");
                } else if (waistToHipRatio <= 0.8) {
                    bodyShapeType = "Triangle";
                    result.setText(bodyShapeType );
                    intro.setText("Here are some health-related considerations and suggestions for individuals with a triangle body shape");
                    suggest1.setText("Engage in regular cardiovascular exercises such as walking, jogging, swimming, or cycling. Aim for at least 150 minutes of moderate-intensity aerobic exercise per week.\n" +
                            "\n" +
                            "Incorporate strength training exercises to tone and strengthen your upper body, including your arms, shoulders, and back. This will help balance your overall body proportions and enhance muscle definition.\n" +
                            "\n" +
                            "Strengthening your core muscles can improve your posture and provide stability for your lower body. Include exercises such as planks, bridges, and abdominal crunches in your fitness routine.\n" +
                            "\n" +
                            "Maintain a balanced and nutritious diet to support overall health and weight management. Be mindful of portion sizes and avoid excessive intake of processed foods, sugary snacks, and drinks.\n" +
                            "\n"+
                            "Pay attention to your posture, particularly when sitting or standing. Practice good posture habits to prevent any strain or imbalances in your body.\n");
                    reference.setText("Physical Activity Guidelines for Americans. U.S. Department of Health and Human Services.");
                    link.setText("https://health.gov/our-work/physical-activity/current-guidelines");
                } else if (waistToHipRatio <= 0.85) {
                    bodyShapeType = "Inverted Triangle";
                    result.setText(bodyShapeType );
                    intro.setText("How to Exercise With an Inverted-Triangle Body Based on LiveStrong");
                    suggest1.setText("Regarding exercise, individuals with an inverted triangle body shape should adhere to the Physical Activity Guidelines for Americans. These guidelines recommend that adults engage in moderate-intensity aerobic exercise for a minimum of 150 to 300 minutes per week.\n" +
                            "\n" +
                            "If you don't have that much time to spare, you can bump up the level of your workouts to vigorous-intensity and decrease the total time to 75 to 150 minutes per week. Further, they recommend two days of muscle-building exercises that target the entire body.\n" +
                            "\n" +
                            "In addition, the best cardio for inverted triangle body shape will also focus on the muscles in the lower body, including the glutes, hamstring and quadricep  muscles.");
                    reference.setText("LiveStrong");
                    link.setText("https://www.livestrong.com/article/476738-how-to-exercise-with-an-inverted-triangle-body/");
                } else if (waistToHipRatio <= 0.9) {
                    bodyShapeType = "Rectangle";
                    result.setText(bodyShapeType );
                    intro.setText("");
                    suggest1.setText("Individuals with a rectangular body shape, also referred to as the straight or athletic body type, have a well-proportioned figure with minimal differences in body measurements. Here are some health recommendations specifically tailored for those with a rectangular body shape:\n" +
                            "\n" +
                            "Focus on consuming a variety of whole foods, including fruits, vegetables, whole grains, lean proteins, and healthy fats.\n" +
                            "\n" +
                            "Incorporate a combination of cardiovascular exercises, strength training, and flexibility exercises into your fitness routine.\n" +
                            "\n" +
                            "Practice good posture to maintain the natural alignment of your spine.\n" +
                            "\n" +
                            "Strengthening the core muscles can improve stability and posture. Include exercises that target the abdominal, back, and pelvic muscles, such as planks and bridges.\n" +
                            "\n"+
                            "Ensure you have enough sleep to promote physical and mental well-being. Allow your body sufficient time to recover between workouts.");
                    reference.setText("American Heart Association. (2020). Recommendations for Physical Activity in Adults and Kids.");
                    link.setText("https://www.heart.org/en/healthy-living/fitness/fitness-basics/aha-recs-for-physical-activity-in-adults");
                } else {
                    bodyShapeType = "Oval";
                    result.setText(bodyShapeType );
                    intro.setText("For individuals with an oval body shape, also known as the apple or round body type, there are some health-related suggestions to consider.");
                    suggest1.setText("Focus on consuming a variety of whole foods, including fruits, vegetables, lean proteins, and whole grains. Limit the intake of processed foods, sugary snacks, and beverages high in added sugars.\n" +
                            "\n" +
                            "Consider using smaller plates and bowls to help control food portions.\n" +
                            "\n" +
                            "Incorporate both cardiovascular exercises, such as walking or cycling, and strength training exercises to improve muscle tone and body composition.\n" +
                            "\n" +
                            "Drink an adequate amount of water throughout the day to stay hydrated.\n" +
                            "\n" +
                            "Practice stress-reducing techniques, such as meditation, deep breathing exercises, or engaging in hobbies, to help manage stress levels.");
                    reference.setText("American Heart Association. (2021). Healthy Eating.");
                    link.setText("https://www.heart.org/en/healthy-living/healthy-eating");
                }

            }
        });
    }

    public void update() {
        final String id = etId.getText().toString();
        final String bodytype = result.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(BodyTypeCalculator.this);

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
                    writer.write(getPostDataString(id, bodytype));
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
                    Toast.makeText(BodyTypeCalculator.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private String getPostDataString(String id, String bodytype) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(URLEncoder.encode("id", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(id, "UTF-8"));
        result.append("&");
        result.append(URLEncoder.encode("bodytype", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(bodytype, "UTF-8"));
        return result.toString();
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(BodyTypeCalculator.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(BodyTypeCalculator.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(BodyTypeCalculator.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(BodyTypeCalculator.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}