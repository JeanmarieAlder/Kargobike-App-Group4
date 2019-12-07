package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.kargobikeappg4.R;

public class ClientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void onClientSelected(View view) {
        Intent intent = new Intent();
        intent.putExtra("clientSelected", "The selected Client");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
