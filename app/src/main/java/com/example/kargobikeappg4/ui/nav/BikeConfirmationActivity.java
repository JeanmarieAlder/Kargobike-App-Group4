package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kargobikeappg4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BikeConfirmationActivity extends AppCompatActivity {

    TextView accountInfo;
    FirebaseUser user;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_confirmation);
        accountInfo = findViewById(R.id.accountInfo);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        String email = user.getEmail();
        Log.d("TAG", "Account name: " + email);
        accountInfo.setText(email);
    }

    public void bikeConfirmation(View view)
    {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
