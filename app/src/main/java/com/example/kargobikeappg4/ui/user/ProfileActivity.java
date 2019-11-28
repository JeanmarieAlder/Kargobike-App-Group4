package com.example.kargobikeappg4.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void user_button_monthlyReport(View view)
    {
        Intent intent = new Intent(this, MonthlyReportActivity.class);
        startActivity(intent);
    }
}
