package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.ListAdapter;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductListViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TransportDetailActivity extends AppCompatActivity {

    private static final String TAG = "TransportDetailActivity";
    //Variable instanciations
    private String orderId;
    private boolean editMode;
    private boolean isLoaded;
    private Order order;
    private OrderViewModel viewModel;
    private ProductListViewModel viewModelProducts;

    private Button btnSave;
    private Button btnChangeStatus;

    //private EditText eProduct;
    private EditText eQuantity;
    private EditText eDelivDate;
    private EditText eDelivTime;
    private EditText eClient;
    private EditText ePickupAddress;
    private EditText eDeliveryAddress;
    //private EditText eResponsibleRider;
    private TextView tvStatus;
    private DatabaseReference reff;
    private Spinner spinnerProducts;
    private ListAdapter adapterProductsList;

    private Spinner spinnerRiders;
    private ListAdapter adapterRidersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_detail);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        //Spinner for Products
        spinnerProducts = (Spinner) findViewById(R.id.spinnerProducts);
        adapterProductsList = new ListAdapter<>(TransportDetailActivity.this, R.layout.rowlist, new ArrayList<>());
        spinnerProducts.setAdapter(adapterProductsList);

        //Spinner for Riders
        spinnerRiders = (Spinner) findViewById(R.id.spinnerRiders);
        adapterRidersList = new ListAdapter<>(TransportDetailActivity.this, R.layout.rowlist, new ArrayList<>());
        spinnerRiders.setAdapter(adapterRidersList);

        //Create viewmodel
        OrderViewModel.Factory factory = new OrderViewModel.Factory(
                    getApplication(), orderId);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(OrderViewModel.class);

        //Fill the Product list
        ArrayList<String> productNames = new ArrayList<String>();
        productNames.add("Product 1");
        productNames.add("Product 2");

        updateAdapterProductsList(productNames);


        //Fill the Rider list
        ArrayList<String> riderNames = new ArrayList<String>();
        riderNames.add("Agron Asani");
        riderNames.add("David Felley");
        riderNames.add("Damian Wasmer");
        riderNames.add("Jean-Marie Alder");

        updateAdapterRiderList(riderNames);


        //Receive all product names from DB
        /*
        ProductListViewModel.Factory factory2 = new ProductListViewModel.Factory(
                getApplication());
        viewModelProducts = ViewModelProviders.of(this, factory2)
                .get(ProductListViewModel.class);

        viewModelProducts.getAllProducts().observe(this, products -> {
            if (products != null) {

                Log.d(TAG,"products Not null");
                //Array productNames
                ArrayList<String> productNames = new ArrayList<String>();
                for (Product p : products
                ) {
                    productNames.add(p.getName());
                }
            }
        });
        */

        if(editMode) {
            viewModel.getOrder().observe(this, orderEntity -> {
                if (orderEntity != null) {
                    order = orderEntity;
                    updateContent();
                }
            });
        }
    }

    private void updateAdapterProductsList(List<String> productNames) {
        adapterProductsList.updateData(new ArrayList<>(productNames));
    }

    private void updateAdapterRiderList(List<String> riderNames) {
        adapterRidersList.updateData(new ArrayList<>(riderNames));
    }

    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        spinnerProducts = findViewById(R.id.spinnerProducts);
        eQuantity = findViewById(R.id.td_input_quantity);
        eDelivDate = findViewById(R.id.td_input_deliveryDate);
        eDelivTime = findViewById(R.id.td_input_deliveryTime);
        eClient = findViewById(R.id.td_input_client);
        ePickupAddress = findViewById(R.id.td_input_pickupAddress);
        eDeliveryAddress = findViewById(R.id.td_input_deliveryAddress);
        spinnerRiders = findViewById(R.id.spinnerRiders);
        tvStatus = findViewById(R.id.td_input_status);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        btnChangeStatus = findViewById(R.id.button_change_status);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                saveChanges();
            }
        }
        );

        //get order ID from intent and set edit mode to false if new order
        editMode = getIntent().getBooleanExtra("isEdit", false);

        if(editMode){
            orderId = getIntent().getExtras().get("orderId").toString();
        }else{
            btnChangeStatus.setVisibility(View.GONE);
        }
    }

    private void updateContent() {
        if (order != null) {
            eQuantity.setText(Float.toString(order.getQuantity()));
            eDelivDate.setText(order.getDateDelivery());
            eDelivTime.setText(order.getTimeDelivery());
            eClient.setText(order.getIdCustomer());
            ePickupAddress.setText(order.getIdPickupCheckpoint());
            eDeliveryAddress.setText(order.getIdDeliveryCheckpoint());

            //set status and button accordingly
            if(order.getStatus().equals("1")){
                tvStatus.setText(R.string.s_loaded);
                btnChangeStatus.setText("Unload");
                isLoaded = true;
            }else if(order.getStatus().equals("0")){
                tvStatus.setText(R.string.s_pending);
                btnChangeStatus.setText("Load");
                isLoaded = false;
            }

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
    public void ButtonChangeStatus(View view)
    {
        updateOrderDB(true);
    }

    private void saveChanges() {

        if(editMode){
            updateOrderDB(false);
        }else{

            Order order = new Order();
            order.setIdProduct(spinnerProducts.getSelectedItem().toString());
            order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
            order.setDateDelivery(eDelivDate.getText().toString());
            order.setTimeDelivery(eDelivTime.getText().toString());
            order.setIdCustomer(eClient.getText().toString());
            order.setIdPickupCheckpoint(ePickupAddress.getText().toString());
            order.setIdDeliveryCheckpoint(eDeliveryAddress.getText().toString());
            order.setIdResponsibleRider(spinnerRiders.getSelectedItem().toString());
            order.setStatus("0");

            viewModel.createOrder(order, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Creation succesful", Toast.LENGTH_LONG).show();
                    onBackPressed(); //finally, go back to previous screen
                }

                @Override
                public void onFailure(Exception e) {
                    if(e.getMessage().contains("FOREIGN KEY")){
                        Toast.makeText(getApplicationContext(), "Creation error: stage name doesn't exist", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * Updates an existing order in the DB. Different behaviour if
     * the user only changes the status.
     * @param isChangingStatus true if only status is changing
     */
    private void updateOrderDB(boolean isChangingStatus){
        order.setIdProduct(spinnerProducts.getSelectedItem().toString());
        order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
        order.setDateDelivery(eDelivDate.getText().toString());
        order.setTimeDelivery(eDelivTime.getText().toString());
        order.setIdCustomer(eClient.getText().toString());
        order.setIdPickupCheckpoint(ePickupAddress.getText().toString());
        order.setIdDeliveryCheckpoint(eDeliveryAddress.getText().toString());
        order.setIdResponsibleRider(spinnerRiders.getSelectedItem().toString());
        Log.d("RESPID", spinnerRiders.getSelectedItem().toString());

        //Changes status if needed (1 = loaded, 0 = unloaded)
        if(isChangingStatus){
            if(order.getStatus().equals("1")){
                order.setStatus("0");
            }else if(order.getStatus().equals("0")){
                order.setStatus("1");
            }
        }

        viewModel.updateOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),
                        "Update succesful", Toast.LENGTH_LONG).show();
                if(!isChangingStatus){
                    onBackPressed(); //finally, go back to previous screen
                }else{
                    updateContent(); //If only status has changed, stay on page and update content
                }
            }

            @Override
            public void onFailure(Exception e) {

                Toast.makeText(getApplicationContext(),
                        "Update failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
