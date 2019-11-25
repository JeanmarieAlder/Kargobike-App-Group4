package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;

public class TransportDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_detail);
    }

    public void Transport_button_clientList(View view)
    {
        Intent intent = new Intent(this, ClientListActivity.class);
        startActivity(intent);
    }

    public void Transport_button_photoScreen(View view)
    {
        Intent intent = new Intent(this, PhotoScreenActivity.class);
        startActivity(intent);
    }

    public void Transport_button_signScreen(View view)
    {
        Intent intent = new Intent(this, SignScreenActivity.class);
        startActivity(intent);
    }
}
