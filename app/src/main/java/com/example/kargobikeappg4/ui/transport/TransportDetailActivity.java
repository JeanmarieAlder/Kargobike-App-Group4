package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TransportDetailActivity extends AppCompatActivity {

    //Variable instanciations
    private String orderId;
    private boolean editMode;
    private Order order;
    private OrderViewModel viewModel;

    private Button btnSave;

    private EditText eProduct;
    private EditText eQuantity;
    private EditText eDelivDate;
    private EditText eDelivTime;
    private EditText eClient;
    private EditText ePickupAddress;
    private EditText eDeliveryAddress;
    private EditText eResponsibleRider;
    private TextView tvStatus;
    private DatabaseReference reff;

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
        tvStatus = findViewById(R.id.td_input_status);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");

        btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                saveChanges();


            }
        }
        );

        //get order ID from intent and set edit mode to false if new order

        //editMode = getIntent().getBooleanExtra("isEdit", true);
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

            //set status accordingly
            if(order.getStatus().equals("1")){
                tvStatus.setText(R.string.s_loaded);
            }else if(order.getStatus().equals("0")){
                tvStatus.setText(R.string.s_pending);
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

    }

    private void saveChanges() {

        if(editMode){

            /**
            act.setArtistName(eaName.getText().toString());
            act.setArtistCountry(eaCountry.getText().toString());
            act.setArtistImage("Not implemented yet");
            act.setGenre(egenre.getText().toString());
            act.setDate(edate.getSelectedItem().toString());
            act.setStartTime(estartTime.getText().toString());

            String pr = eprice.getText().toString();
            Float f;
            try{
                f = new Float(pr);
                act.setPrice(f);
            }catch(Exception e){
                e.getMessage();
                act.setPrice(0f);
            }
            act.setIdStage(estage.getText().toString());

            viewModel.updateAct(act, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Update succesful", Toast.LENGTH_LONG).show();
                    onBackPressed(); //finally, go back to previous screen
                }

                @Override
                public void onFailure(Exception e) {

                    Toast.makeText(getApplicationContext(),
                            "Update failed", Toast.LENGTH_LONG).show();
                }

            });

             */
        }else{

            Order order = new Order();
            order.setIdProduct(eProduct.getText().toString());
            order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
            order.setDateDelivery(eDelivDate.getText().toString());
            order.setTimeDelivery(eDelivTime.getText().toString());
            order.setIdCustomer(eClient.getText().toString());
            order.setIdPickupCheckpoint(ePickupAddress.getText().toString());
            order.setIdDeliveryCheckpoint(eDeliveryAddress.getText().toString());
            order.setIdResponsibleRider(eResponsibleRider.getText().toString());

            eProduct.setText("");
            eQuantity.setText("");
            eDelivDate.setText("");
            eDelivTime.setText("");
            eClient.setText("");
            ePickupAddress.setText("");
            eDeliveryAddress.setText("");
            eResponsibleRider.setText("");

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


}
