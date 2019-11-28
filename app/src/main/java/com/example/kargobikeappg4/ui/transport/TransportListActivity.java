package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.adapter.TransportListAdapter;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.util.RecyclerViewItemClickListener;
import com.example.kargobikeappg4.viewmodel.order.OrderListViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransportListActivity extends AppCompatActivity {
    //RecyclerView recyclerView;
    //TransportListAdapter adapter;
    //ArrayList<String> items;

    private RecyclerAdapter<Order> adapter;
    private List<Order> orders;
    private OrderListViewModel listViewModel;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);


        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_storage);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        orders = new ArrayList<>();

        //Add click listener, opens details of the selected act
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(TransportListActivity.this,
                    TransportDetailActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            intent.putExtra("orderId", orders.get(position).getIdOrder());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        OrderListViewModel.Factory factory = new OrderListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(OrderListViewModel.class);
        listViewModel.getAllOrders().observe(this, orderEntities -> {
            if (orderEntities != null) {
                orders = orderEntities;
                adapter.setData(orders);
            }
        });
        rView.setAdapter(adapter);

    }

    public void Transport_button_transportDetails(View view)
    {
        Intent intent = new Intent(this, TransportDetailActivity.class);
        startActivity(intent);
    }
}
