package com.example.fitnesscalculatormobapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class BMICalculator extends AppCompatActivity {


    private Button btn, btnSave, btnUpdate;

    private TextView storeBMIResult;
    private TextView textViewResult;
    private TextView textViewIntro;
    private TextView textViewSuggest1;
    private TextView textViewSuggest2;
    private TextView textViewSuggest3;
    private TextView textViewSuggest4;
    private TextView textViewReference;
    private TextView textViewLink;
    private EditText etId;
    private EditText editTextWeight;
    private EditText editTextFeet;
    private EditText editTextInch;

    private float bmiIndex;

    public static final String URL_UPDATE = "http://192.168.148.118/student/updatebmi.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        //Toolbar
        TextView title = findViewById(R.id.title);
        //For toolbar back
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        //BMI Need
        etId = findViewById(R.id.id);
        editTextWeight = findViewById(R.id.weight);
        editTextFeet = findViewById(R.id.heightft);
        editTextInch = findViewById(R.id.heightIn);
        btn = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.BtnSave);
        btnUpdate = findViewById(R.id.BtnSave);
        storeBMIResult = findViewById(R.id.storeBMI);
        textViewResult = findViewById(R.id.tvBMI);
        textViewIntro = findViewById(R.id.tvBMIIntro);
        textViewSuggest1 = findViewById(R.id.tvBMISuggest1);
        textViewSuggest2 = findViewById(R.id.tvBMISuggest2);
        textViewSuggest3 = findViewById(R.id.tvBMISuggest3);
        textViewSuggest4 = findViewById(R.id.tvBMISuggest4);
        textViewReference = findViewById(R.id.tvBMIReference);
        textViewLink = findViewById(R.id.tvBMILink);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float weight = Float.parseFloat(String.valueOf(editTextWeight.getText()));
                float Feet = Float.parseFloat(String.valueOf(editTextFeet.getText()));
                float Inch = Float.parseFloat(String.valueOf(editTextInch.getText()));

                /*Conversion height(m2)
                 * 1ft = 12 inch
                 * 1inch = 0.0254 meters
                 * */
                float height = (float) ((Feet * 12 + Inch) * 0.0254);

                DecimalFormat dec_format = new DecimalFormat("00.0");

                bmiIndex = Float.parseFloat(dec_format.format(weight / (height * height)));

                btnUpdate.setVisibility(View.VISIBLE);

                // Condition
                if (bmiIndex < 18.5){
                    //Underweight BMI
                    textViewResult.setText(bmiIndex + " (Underweight)");
                    textViewIntro.setText("These are the good ways to gain weight if you're underweight based on Mayo Clinic");
                    textViewSuggest1.setText("1. Discuss your BMI category with your healthcare provider as BMI may relate to your overall health and well-being. Your healthcare provider might determine possible reasons for underweight and recommend support or treatment.");
                    textViewSuggest2.setText("2. Eating more frequently. Slowly begin to eat 5 to 6 smaller meals during the day. Try to tune into your body to recognize when you might be hungry. But you may need to plan times to eat even if you aren't that hungry.");
                    textViewSuggest3.setText("3. Top it off. Add extras to your dishes for more calories, such as cheese in casseroles or nut butter on whole-grain toast. You also can add dry milk or liquid milk to foods for extra protein and calories. Some examples are mashed potatoes or soups.");
                    textViewSuggest4.setText("4. Exercise. Exercise, especially strength training, can help you gain weight by building up your muscles. Exercise also may stimulate your appetite.");
                    textViewReference.setText("To know more about Mayo Clinic, just click the link below:");
                    textViewLink.setText("https://www.mayoclinic.org/");
                } else if (bmiIndex >= 18.5 && bmiIndex <=24.9) {
                    //Normal BMI
                    textViewResult.setText(bmiIndex + " (Normal)");
                    textViewIntro.setText("Physical Activity for a Healthy Weight based on CDC (Centers for Disease Control and Prevention)");
                    textViewSuggest1.setText("Regular physical activity provides immediate and long-term health benefits. Being physically active can improve your brain health, reduce the risk of disease, strengthen bones and muscles, and improve your ability to do everyday activities.\n" +
                            "\n" +
                            "Physical activity also helps:\n" +
                            "\n" +
                            "Improve sleep quality.\n\n" +
                            "Reduce high blood pressure.\n\n" +
                            "Reduce risk for type 2 diabetes, heart attack, stroke, and several forms of cancer.\n\n" +
                            "Reduce arthritis pain and associated disability.\n\n" +
                            "Reduce risk for osteoporosis and falls.\n\n" +
                            "Reduce symptoms of depression and anxiety.");
                    textViewSuggest2.setText("\nIn addition, physical activity is important if you are trying to lose weight or maintain a healthy weight.\n" +
                            "\n" +
                            "When losing weight, more physical activity increases the number of calories your body uses for energy. Using calories through physical activity, combined with reducing the calories you eat, creates a calorie deficit that results in weight loss.\n\n" +
                            "Most weight loss occurs from decreasing caloric intake. However, evidence shows the only way to maintain weight loss is to be engaged in regular physical activity.\n\n" +
                            "Most importantly, physical activity reduces risks of cardiovascular disease and diabetes beyond that produced by weight reduction alone.");
                    textViewSuggest3.setText("\nHow much physical activity do I need?\n");
                    textViewSuggest4.setText("To maintain your weight: Work your way up to 150 minutes of moderate-intensity aerobic activity each week. This could be brisk walking 30 minutes a day, 5 days a week. Or you could do 75 minutes of vigorous-intensity aerobic activity each week, such as swimming laps.\n\n" +
                            "\n" +
                            "The exact amount of physical activity needed to maintain a healthy weight varies greatly from person to person. You may need more than the equivalent of 150 minutes of moderate-intensity activity a week to maintain your weight.\n\n" +
                            "\n" +
                            "To lose weight and keep it off: You will need a high amount of physical activity unless you also adjust your diet to reduce the number of calories you eat and drink. Getting to and staying at a healthy weight requires both regular physical activity and a healthy eating plan.");
                    textViewReference.setText("To know more this just click the link below:");
                    textViewLink.setText("https://www.cdc.gov/healthyweight/physical_activity/index.html");
                } else if (bmiIndex >= 25 && bmiIndex <=29.9) {
                    //Overweight BMI
                    textViewResult.setText(bmiIndex + " (Overweight)");
                    textViewIntro.setText("If your BMI indicates you are overweight, it is ideal for you to lose weight");
                    textViewSuggest1.setText("That said, weight-loss treatment is particularly important and recommended when you have two or more health risk-factors.");
                    textViewSuggest2.setText("These include being a smoker, being inactive (I would define inactivity as fewer than 10,000 pedometer steps daily, averaged over a week), or having any of the following:");
                    textViewSuggest3.setText("high blood pressure, low HDL (good) cholesterol, high LDL (bad) cholesterol, high triglyceride (blood fat) levels, impaired fasting-glucose, a family history of premature heart disease, or a high waist-circumference (measured at the belly button).");
                    textViewSuggest4.setText("Note: Highly muscular people, usually professional athletes or body builders, may register as overweight using the BMI scale, but this may be because of high muscle mass instead of fat. More muscle is not generally thought of as unhealthy, so weight loss would not be necessary in these cases.");
                    textViewReference.setText("The notes above are based on Katherine Tallmadge, M.A., R.D., to know more click the link below:");
                    textViewLink.setText("https://www.livescience.com/38868-eight-steps-to-healthiest-weight.html");
                } else {
                    //Obesity BMI
                    textViewResult.setText( bmiIndex + " (Obesity)");
                    textViewIntro.setText("This is how can overweight and obesity be reduced based on WHO(World Health Organization)");
                    textViewSuggest1.setText("Overweight and obesity, as well as their related noncommunicable diseases, are largely preventable. Supportive environments and communities are fundamental in shaping people’s choices, by making the choice of healthier foods and regular physical activity the easiest choice (the choice that is the most accessible, available and affordable), and therefore preventing overweight and obesity.");
                    textViewSuggest2.setText("\nAt the individual level, people can:\n" +
                            "\n" +
                            "limit energy intake from total fats and sugars;\n\n" +
                            "increase consumption of fruit and vegetables, as well as legumes, whole grains and nuts; and\n\n" +
                            "engage in regular physical activity (60 minutes a day for children and 150 minutes spread through the week for adults).");
                    textViewSuggest3.setText("Individual responsibility can only have its full effect where people have access to a healthy lifestyle. Therefore, at the societal level it is important to support individuals in following the recommendations above, through sustained implementation of evidence based and population based policies that make regular physical activity and healthier dietary choices available, affordable and easily accessible to everyone, particularly to the poorest individuals. An example of such a policy is a tax on sugar sweetened beverages.");
                    textViewSuggest4.setText("The food industry can play a significant role in promoting healthy diets by:\n" +
                            "\n" +
                            "reducing the fat, sugar and salt content of processed foods;\n\n" +
                            "ensuring that healthy and nutritious choices are available and affordable to all consumers;\n\n" +
                            "restricting marketing of foods high in sugars, salt and fats, especially those foods aimed at children and teenagers; and\n\n" +
                            "ensuring the availability of healthy food choices and supporting regular physical activity practice in the workplace.");
                    textViewReference.setText("The notes above are based on WHO(World Health Organization):");
                    textViewLink.setText("https://www.who.int/news-room/fact-sheets/detail/obesity-and-overweight");
                }

            }
        });
    }

    public void update() {
        final String id = etId.getText().toString();
        final String bmi = textViewResult.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(BMICalculator.this);

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
                    writer.write(getPostDataString(id, bmi));
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
                    Toast.makeText(BMICalculator.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private String getPostDataString(String id, String bmi) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        result.append(URLEncoder.encode("id", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(id, "UTF-8"));
        result.append("&");
        result.append(URLEncoder.encode("bmi", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(bmi, "UTF-8"));
        return result.toString();
    }


    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(BMICalculator.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(BMICalculator.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(BMICalculator.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(BMICalculator.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
