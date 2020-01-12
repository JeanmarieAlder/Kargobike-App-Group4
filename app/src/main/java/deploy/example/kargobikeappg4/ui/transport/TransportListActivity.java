package deploy.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import deploy.example.kargobikeappg4.db.entities.Order;
import deploy.example.kargobikeappg4.db.entities.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.RecyclerAdapter;
import deploy.example.kargobikeappg4.viewmodel.order.OrderListViewModel;
import deploy.example.kargobikeappg4.viewmodel.user.UserViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransportListActivity extends AppCompatActivity {

    //Attributes
    private RecyclerAdapter<Order> adapter;
    private List<Order> orders;
    private List<Order> filteredorders;
    private OrderListViewModel listViewModel;
    private UserViewModel userViewmodel;
    private RecyclerView rView;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private User user;

    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);

        //Initialize User
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        //getting all users from the DB
        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(), fUser.getUid());
        userViewmodel = ViewModelProviders.of(this, factory)
                .get(UserViewModel.class);
        userViewmodel.getUser().observe(this, userEntitie -> {
            if (userEntitie != null) {
                user = userEntitie;
            }
        });

        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_storage);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        orders = new ArrayList<>();

        //Add click listener, opens details of the selected order
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(TransportListActivity.this,
                    TransportDetailActivity.class);
            intent.putExtra("orderId", filteredorders.get(position).getIdOrder());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        OrderListViewModel.Factory factory2 = new OrderListViewModel.Factory(
                getApplication()
        );

        //set the right Dateformat
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        //get all transports
        listViewModel = ViewModelProviders.of(this, factory2)
                .get(OrderListViewModel.class);
        listViewModel.getAllOrders().observe(this, orderEntities -> {
            if (orderEntities != null) {
                orders = orderEntities;
                filteredorders = new ArrayList<>();

                //look for all orders of  date
                for (Order o : orders) {
                    if (o.getDateDelivery().equals(date)) {

                        //it is a Rider, only his orders will be displayed
                        if (user.getIdFunction().equals("Rider")) {
                            if (o.getIdResponsibleRider().equals(user.getName()))
                                filteredorders.add(o);
                        } else {
                            filteredorders.add(o);
                        }
                    }
                }
                adapter.setData(filteredorders);
            }
        });
        rView.setAdapter(adapter);

    }

    //Button to add a new Transport
    public void Transport_button_transportAdd(View view) {
        Intent intent = new Intent(this, TransportDetailActivity.class);
        intent.putExtra("isEdit", false);

        startActivity(intent);
    }
}
