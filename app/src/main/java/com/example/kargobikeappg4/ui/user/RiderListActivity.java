package com.example.kargobikeappg4.ui.user;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.Rider;
import com.example.kargobikeappg4.viewmodel.order.OrderListViewModel;
import com.example.kargobikeappg4.viewmodel.rider.RiderListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RiderListActivity extends AppCompatActivity {

    private RecyclerAdapter<Rider> adapter;
    private List<Rider> riders;
    private RiderListViewModel listViewModel;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_userlist);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        riders = new ArrayList<>();

        //Add click listener, opens details of the selected user
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(RiderListActivity.this,
                    ProfileActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            intent.putExtra("userId", riders.get(position).getIdRider());
            startActivity(intent);
        });

        RiderListViewModel.Factory factory = new RiderListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(RiderListViewModel.class);
        listViewModel.getAllRiders().observe(this, riderEntities -> {
            if (riderEntities != null) {
                riders = riderEntities;
                adapter.setData(riders);
            }
        });
        rView.setAdapter(adapter);

    }
}
