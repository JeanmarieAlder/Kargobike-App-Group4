package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.adapter.TransportListAdapter;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.util.RecyclerViewItemClickListener;
import com.example.kargobikeappg4.viewmodel.order.OrderListViewModel;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class TransportListActivity extends AppCompatActivity {

    private RecyclerAdapter<Order> adapter;
    private List<Order> orders;
    private List<Order> filteredorders;
    private OrderListViewModel listViewModel;
    private UserViewModel userViewmodel;
    private RecyclerView rView;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);

        //Initialize User
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        Log.d("User Firebase", "User id (current)" + fUser.getUid() + fUser.getDisplayName() );

        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fUser.getUid());
        userViewmodel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntitie ->{
            if(userEntitie != null)
            {
                user = userEntitie;
            }
        });

        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_storage);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        orders = new ArrayList<>();

        //Add click listener, opens details of the selected act
                adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(TransportListActivity.this,
                    TransportDetailActivity.class);
            /*intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );*/
            intent.putExtra("orderId", filteredorders.get(position).getIdOrder());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        OrderListViewModel.Factory factory2 = new OrderListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory2)
                .get(OrderListViewModel.class);
        listViewModel.getAllOrders().observe(this, orderEntities -> {
            if (orderEntities != null) {
                orders = orderEntities;
                filteredorders = new ArrayList<>();
                for (Order o : orders) {
                    if (o.getIdResponsibleRider().equals(user.getName())) {
                        filteredorders.add(o);
                    }
                }
                adapter.setData(filteredorders);
            }
        });
        rView.setAdapter(adapter);

    }

    public void Transport_button_transportAdd(View view)
    {
        Intent intent = new Intent(this, TransportDetailActivity.class);
        intent.putExtra("isEdit", false);

        startActivity(intent);
    }
}
