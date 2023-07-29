package com.example.fitnesscalculatormobapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class WeeklyRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_record);

        //For Toolbar
        TextView title = findViewById(R.id.title);
        ImageView back = findViewById(R.id.back);
        /* Menu Icon */
        ImageView menu = findViewById(R.id.menuicon);

        /* Title Home */
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

        /* Toolbar event to back */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(WeeklyRecord.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.measurements) {
                    Intent intent = new Intent(WeeklyRecord.this, MyMeasurements.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.weekly) {
                    Intent intent = new Intent(WeeklyRecord.this, WeeklyRecord.class);
                    startActivity(intent);
                    finish();
                }
                if (menuItem.getItemId() == R.id.monthly) {
                    Intent intent = new Intent(WeeklyRecord.this, MonthlyRecord.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        popupMenu.show();
    }
}