package com.example.fitnesscalculatormobapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CalculatorMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_menu);

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

        //Toolbar event to back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        // Calculator Card Menu
        CardView BMICard = findViewById(R.id.BMICal);
        CardView BMRCard = findViewById(R.id.BMRCal);
        CardView BodyTypeCard = findViewById(R.id.BodyTypeCal);
        CardView BodyFatCard = findViewById(R.id.BodyFatCal);
        CardView IdealWeightCard = findViewById(R.id.IdealWeightCal);

        //Toolbar event to back
        BMICard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BMICalculator.class);
                startActivity(intent);
                finish();
            }
        });

        BMRCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BMRCalculator.class);
                startActivity(intent);
                finish();
            }
        });

        BodyFatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BodyFatCalculator.class);
                startActivity(intent);
                finish();
            }
        });

        BodyTypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BodyTypeCalculator.class);
                startActivity(intent);
                finish();
            }
        });

        IdealWeightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IdealWeightCalculator.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(CalculatorMenu.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(CalculatorMenu.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(CalculatorMenu.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(CalculatorMenu.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}