package com.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity{

    private View group;
    private View loading;
    private UserViewModel userViewmodel;
    private User user;
    private TextView name;
    private TextView address;
    private TextView email;
    private TextView phone;
    private String mailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Initialize();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mailAddress = fbUser.getEmail();

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

        name = findViewById(R.id.profile_tv_name);
        address  = findViewById(R.id.profile_tv_address);
        email = findViewById(R.id.profile_tv_mailAddress);
        phone = findViewById(R.id.profile_tv_phone);

        group = findViewById(R.id.groupProfileInfos);
        group.setVisibility(View.INVISIBLE);
        loading = findViewById(R.id.profile_loading);
    }

    private void UpdateContent()
    {
        loading.setVisibility(View.INVISIBLE);

        name.setText(user.getName());
        address.setText(user.getIdAddress());
        email.setText(mailAddress);
        phone.setText(user.getPhoneNumber());

        group.setVisibility(View.VISIBLE);
    }

    public void user_button_monthlyReport(View view)
    {
        Intent intent = new Intent(this, MonthlyReportActivity.class);
        startActivity(intent);
    }
}
