package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kargobikeappg4.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

        if(user != null){
            String email = user.getEmail();
            accountInfo.setText(email);
        }else{
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            String email = account.getEmail();
            accountInfo.setText(email);
        }

    }

    public void bikeConfirmation(View view)
    {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
