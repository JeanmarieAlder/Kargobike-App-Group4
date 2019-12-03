package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;

public class BikeConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_confirmation);
    }

    public void bikeConfirmation(View view)
    {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
