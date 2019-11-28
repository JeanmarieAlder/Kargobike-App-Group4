package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.product.ProductListActivity;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.ui.user.ProfileActivity;
import com.example.kargobikeappg4.ui.user.RiderListActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void Welcome_button_transportList(View view)
    {
        Intent intent = new Intent(this, TransportListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_productList(View view)
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_profile(View view)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void Welcome_button_userList(View view)
    {
        Intent intent = new Intent(this, RiderListActivity.class);
        startActivity(intent);
    }
}
