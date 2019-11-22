package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;

public class TransportListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);
    }

    public void transportButton(View view)
    {
        Intent intent = new Intent(this, TransportDetailActivity.class);
        startActivity(intent);
    }
}
