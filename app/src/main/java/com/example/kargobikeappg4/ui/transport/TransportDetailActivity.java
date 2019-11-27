package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;

public class TransportDetailActivity extends AppCompatActivity {

    //Variable instanciations
    private String orderId;
    private boolean editMode;
    private Order order;
    private OrderViewModel viewModel;

    private EditText eProduct;
    private EditText eQuantity;
    private EditText eDelivDate;
    private EditText eDelivTime;
    private EditText eClient;
    private EditText ePickupAddress;
    private EditText eDeliveryAddress;
    private EditText eResponsibleRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_detail);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        //Create viewmodel
        OrderViewModel.Factory factory = new OrderViewModel.Factory(
                getApplication(), orderId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(OrderViewModel.class);
        viewModel.getOrder().observe(this, orderEntity -> {
            if (orderEntity != null) {
                order = orderEntity;
                updateContent();
            }
        });

    }

    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        eProduct = findViewById(R.id.td_input_product);
        eQuantity = findViewById(R.id.td_input_quantity);
        eDelivDate = findViewById(R.id.td_input_deliveryDate);
        eDelivTime = findViewById(R.id.td_input_deliveryTime);
        eClient = findViewById(R.id.td_input_client);
        ePickupAddress = findViewById(R.id.td_input_pickupAddress);
        eDeliveryAddress = findViewById(R.id.td_input_deliveryAddress);
        eResponsibleRider = findViewById(R.id.td_input_responsibleRider);

        //get order ID from intent and set edit mode to false if new order
        orderId = getIntent().getStringExtra("orderId");
        editMode = getIntent().getBooleanExtra("isEdit", true);
    }

    private void updateContent() {
        if (order != null) {
            eProduct.setText(order.getIdProduct());
            eQuantity.setText(Float.toString(order.getQuantity()));
            eDelivDate.setText(order.getDateDelivery());
            eDelivTime.setText(order.getTimeDelivery());
            eClient.setText(order.getIdCustomer());
            ePickupAddress.setText(order.getIdPickupCheckpoint());
            eDeliveryAddress.setText(order.getIdDeliveryCheckpoint());
            eResponsibleRider.setText(order.getIdResponsibleRider());
        }
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
