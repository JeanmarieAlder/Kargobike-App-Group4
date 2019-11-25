package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.TransportListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransportListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TransportListAdapter adapter;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);

        items = new ArrayList<>();
        items.add("First Transport Item");
        items.add("Second Transport Item");
        items.add("Third Transport Item");

        recyclerView = findViewById(R.id.recycler_view_storage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransportListAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    public void Transport_button_transportDetails(View view)
    {
        Intent intent = new Intent(this, TransportDetailActivity.class);
        startActivity(intent);
    }
}
