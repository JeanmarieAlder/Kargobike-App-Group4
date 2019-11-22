package com.example.kargobikeappg4.ui.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
    }

    public void productDetailsButton(View view)
    {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        startActivity(intent);
    }
}
