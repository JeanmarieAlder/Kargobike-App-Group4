package com.example.kargobikeappg4.ui.nav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.ui.product.ProductListActivity;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.ui.user.ProfileActivity;
import com.example.kargobikeappg4.ui.user.UserListActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void transportButton(View view)
    {
        Intent intent = new Intent(this, TransportListActivity.class);
        startActivity(intent);
    }

    public void productButton(View view)
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void profileButton(View view)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void userList(View view)
    {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }
}
