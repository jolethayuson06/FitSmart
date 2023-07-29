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

import java.util.HashMap;

public class MyMeasurements extends AppCompatActivity {
    private EditText etName;
    private EditText etYear;
    private EditText etNum;
    private EditText etId;
    private TextView tvName;
    private TextView tvYear;
    private TextView tvNum;
    private TextView tvBmi, tvBmr, tvBodyFat, tvBodyType, tvIdealType;
    private Button btn_save;
    private Button btn_update;

    public static final String URL_ADD = "http://192.168.148.118/student/create.php";
    public static final String URL_SHOW = "http://192.168.148.118/student/get.php";
    public static final String URL_UPDATE = "http://192.168.148.118/student/update.php";
    public static final String URL_DELETE = "http://192.168.148.118/student/delete.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_measurements);

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

        // Initialize the views
        etName = findViewById(R.id.name);
        etYear = findViewById(R.id.year);
        etNum = findViewById(R.id.number);
        etId = findViewById(R.id.id);
        tvName = findViewById(R.id.showname);
        tvYear = findViewById(R.id.showyear);
        tvNum = findViewById(R.id.shownum);
        tvBmi = findViewById(R.id.showbmi);
        tvBmr = findViewById(R.id.showbmr);
        tvBodyFat = findViewById(R.id.showbodyfat);
        tvBodyType = findViewById(R.id.showbodyshape);
        tvIdealType = findViewById(R.id.showidealweight);
        btn_save = findViewById(R.id.BtnSave);
        btn_update = findViewById(R.id.BtnUpdate);

        // Retrieve and display data
        retrieveData();

        //Toolbar event to back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        // Set click listener for the Save button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        // Set click listener for the Update button
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        // Check if the user with id=1 exists
        if (isUserExists()) {
            // Show the Update button
            btn_update.setVisibility(View.VISIBLE);
        }
    }

    private boolean isUserExists() {
        // Check if the user with id=1 exists
        // Implement your logic here
        // Return true if the user exists, false otherwise
        // Example:
        String userId = etId.getText().toString();
        return userId.equals("1");
    }

    public void add(){
        final String name = etName.getText().toString();
        final String year = etYear.getText().toString();
        final String number = etNum.getText().toString();

        class Add_stud extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(MyMeasurements.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Nullable
            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("year", year);
                params.put("number", number);

                return requestHandler.sendPostRequest(URL_ADD, params);
            }

            @Override
            protected  void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MyMeasurements.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
                }
            }

        }

        Add_stud add_stud = new Add_stud();
        add_stud.execute();
    }

    public void update() {
        final String id = etId.getText().toString();
        final String name = etName.getText().toString();
        final String year = etYear.getText().toString();
        final String number = etNum.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(MyMeasurements.this);

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
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);
                params.put("year", year);
                params.put("number", number);

                return requestHandler.sendPostRequest(URL_UPDATE, params);
            }

            @Override
            protected  void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        // Update the TextViews with the new data
                        tvName.setText("Age: " + name);
                        tvYear.setText("Weight: " + year);
                        tvNum.setText("Height: " + number);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MyMeasurements.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
                }
            }

        }

        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private void retrieveData() {
        class RetrieveData extends AsyncTask<Void, Void, String> {
            ProgressDialog pdLoading = new ProgressDialog(MyMeasurements.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\tLoading...");
                pdLoading.setCancelable(false);
                pdLoading.show();
            }

            @Nullable
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                // No need to pass the "id" parameter, assuming there is only one record
                return requestHandler.sendPostRequest(URL_SHOW, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();

                try {
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        // Data retrieval successful, update the views with the retrieved data
                        String name = obj.getString("name");
                        String year = obj.getString("year");
                        String number = obj.getString("number");
                        String bmi = obj.getString("bmi");
                        String bmr = obj.getString("bmr");
                        String bodyfat = obj.getString("bodyfat");
                        String bodytype = obj.getString("bodytype");
                        String idealweight = obj.getString("idealweight");

                        // Update the corresponding TextViews with the retrieved data
                        tvName.setText("Age: " + name);
                        tvYear.setText("Weight: " + year);
                        tvNum.setText("Height: " + number);
                        tvBmi.setText("BMI: " + bmi);
                        tvBmr.setText("BMR: " + bmr);
                        tvBodyFat.setText("Body Fat: " + bodyfat);
                        tvBodyType.setText("Body Type: " + bodytype);
                        tvIdealType.setText("Ideal Weight: " + idealweight);

                        // Make the TextViews visible
                        tvName.setVisibility(View.VISIBLE);
                        tvYear.setVisibility(View.VISIBLE);
                        tvNum.setVisibility(View.VISIBLE);
                        tvBmi.setVisibility(View.VISIBLE);
                        tvBmr.setVisibility(View.VISIBLE);
                        tvBodyFat.setVisibility(View.VISIBLE);
                        tvBodyType.setVisibility(View.VISIBLE);
                        tvIdealType.setVisibility(View.VISIBLE);

                        // Hide the Save button
                        btn_save.setVisibility(View.GONE);

                        // Show the Update button
                        btn_update.setVisibility(View.VISIBLE);
                    } else {
                        // Handle the case when an error occurs during data retrieval
                        String errorMessage = obj.getString("message");
                        Toast.makeText(MyMeasurements.this, errorMessage, Toast.LENGTH_SHORT).show();

                        // Show the Save button
                        btn_save.setVisibility(View.VISIBLE);

                        // Hide the Update button
                        btn_update.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyMeasurements.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Show the Save button
                    btn_save.setVisibility(View.VISIBLE);

                    // Hide the Update button
                    btn_update.setVisibility(View.GONE);
                }
            }
        }

        RetrieveData retrieveData = new RetrieveData();
        retrieveData.execute();
    }


    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MyMeasurements.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(MyMeasurements.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(MyMeasurements.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(MyMeasurements.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}