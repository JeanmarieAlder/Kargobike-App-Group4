package com.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.Address;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.db.repository.UserRepository;
import com.example.kargobikeappg4.viewmodel.address.AddressViewModel;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity{

    private View group;
    private View loading;
    private UserViewModel userViewmodel;
    private String userId;
    private User user;
    private AddressViewModel addressViewModel;
    private Address userAddress;
    private TextView name;
    private TextView address;
    private TextView city;
    private TextView email;
    private TextView phone;
    private Boolean currentUser;
    private Intent currentIntent;
    private UserRepository uRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Initialize();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = currentIntent.getBooleanExtra("user", true);

        if(!currentUser)
            userId = currentIntent.getStringExtra("userId");
        else
            userId = fbUser.getUid();

        //Create viewmodel for user
        UserViewModel.Factory userFactory = new UserViewModel.Factory(
                getApplication(), userId);
        userViewmodel = ViewModelProviders.of(this, userFactory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntity ->{
            if(userEntity != null)
            {
                user = userEntity;
                //Create viewmodel for address
                AddressViewModel.Factory addressFactory = new AddressViewModel.Factory(
                        getApplication(), user.getIdAddress());
                addressViewModel = ViewModelProviders.of(this, addressFactory)
                        .get(AddressViewModel.class);
                addressViewModel.getAddress().observe(this, addressEntity ->{
                    if(addressEntity != null)
                    {
                        userAddress = addressEntity;
                        UpdateContent();
                    }
                });
            }
        });


    }

    private void Initialize(){

        name = findViewById(R.id.profile_tv_name);
        address  = findViewById(R.id.profile_tv_address);
        city  = findViewById(R.id.profile_tv_city);
        email = findViewById(R.id.profile_tv_mailAddress);
        phone = findViewById(R.id.profile_tv_phone);

        group = findViewById(R.id.groupProfileInfos);
        group.setVisibility(View.INVISIBLE);
        loading = findViewById(R.id.profile_loading);


        currentIntent = getIntent();
    }

    private void UpdateContent()
    {
        loading.setVisibility(View.INVISIBLE);

        name.setText(user.getName());
        address.setText(userAddress.getStreet());
        String pcCity = userAddress.getPostcode() + " " + userAddress.getCity();
        city.setText(pcCity);
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());

        group.setVisibility(View.VISIBLE);
    }

    public void user_button_monthlyReport(View view)
    {
        Intent intent = new Intent(this, MonthlyReportActivity.class);
        intent.putExtra("userId", user.getIdUser());

        startActivity(intent);
    }
}
