package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class BikeConfirmationActivity extends AppCompatActivity {

    private UserViewModel userViewmodel;
    private User user;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_confirmation);

        Initialize();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();


        //Create viewmodel
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fbUser.getUid());
        userViewmodel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntitie ->{
            if(userEntitie != null)
            {
                user = userEntitie;
                UpdateContent();
            }
        });
    }

    private void Initialize(){

        name = findViewById(R.id.bikeca_tv_welcome);

    }

    private void UpdateContent()
    {
        name.setText("Hi, "+ user.getName());
    }

    public void bikeConfirmation(View view)
    {
        Intent intent = new Intent(this, WelcomeActivity.class);

        startActivity(intent);
    }
}
