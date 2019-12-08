package com.example.kargobikeappg4.ui.transport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.ListAdapter;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.Checkpoint;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.ui.checkpoint.CheckpointActivity;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.checkpoint.CheckpointListViewModel;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductListViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Intent currIntent;

    private Button btnSave;
    private Button btnDelete;
    private Button btnChangeStatus;
    private Button btnCheckpoint;

    private EditText eQuantity;
    private EditText eDelivDate;
    private EditText eDelivTime;
    private EditText eClient;
    private EditText ePickupAddress;
    private EditText eDeliveryAddress;
    private TextView tvStatus;
    private DatabaseReference reff;
    private Spinner spinnerProducts;
    private ListAdapter adapterProductsList;

    private Spinner spinnerRiders;
    private ListAdapter adapterRidersList;

    //Checkpoint list
    private RecyclerAdapter<Checkpoint> adapter;
    private List<Checkpoint> checkpoints;
    private CheckpointListViewModel listViewModel;
    private RecyclerView rView;

    private String clientSelected;

    private DatePickerDialog.OnDateSetListener DateSetListenerDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE TDA", "---------------------------- HERE  IT STARDED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_detail);
        if(order != null){
            Log.d("ONCREATE TDA", "---------------------------- HERE  " + order.getQuantity());
        }else{
            Log.d("ONCREATE TDA", "---------------------------- HERE  NO ORDER FOUND");
        }

        currIntent = getIntent();

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

        setupOrderViewModel();

        //OnClickListener für Date Delivery
        eDelivDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TransportDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListenerDelivery,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //DateSetListener for Date Delivery
        DateSetListenerDelivery = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                eDelivDate.setText(date);
            }
        };

        //Fill the Rider list
        ArrayList<String> riderNames = new ArrayList<String>();
        riderNames.add("Agron Asani");
        riderNames.add("David Felley");
        riderNames.add("Damian Wasmer");
        riderNames.add("Jean-Marie Alder");
        riderNames.add("Yannick Mermod");

        updateAdapterRiderList(riderNames);

        //Receive all product names from DB
        ProductListViewModel.Factory factory2 = new ProductListViewModel.Factory(
                getApplication());
        viewModelProducts = ViewModelProviders.of(this, factory2)
                .get(ProductListViewModel.class);

        viewModelProducts.getAllProducts().observe(this, products -> {
            if (products != null) {

                Log.d(TAG,"products Not null");
                //Array productNames
                ArrayList<String> productNames = new ArrayList<>();
                for (Product p : products) {
                    productNames.add(p.getName());
                }
                updateAdapterProductsList(productNames);
            }
        });

        //Fill the Checkpoints Recyclerview
        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_storage);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        //Checkpoints stuff
        checkpoints = new ArrayList<>();

        //Add click listener, opens details of the selected act
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(TransportDetailActivity.this,
                    CheckpointActivity.class);
            /*intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );*/
            intent.putExtra("checkpointId", checkpoints.get(position).getIdCheckpoint());
            intent.putExtra("orderId", orderId);
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        //if(editMode){
            CheckpointListViewModel.Factory factoryCheckpoints = new CheckpointListViewModel.Factory(
                    getApplication(), orderId
            );

            listViewModel = ViewModelProviders.of(this, factoryCheckpoints)
                    .get(CheckpointListViewModel.class);
            listViewModel.getAllCheckpoints().observe(this, checkpointsEntities -> {
                if (checkpointsEntities != null) {
                    checkpoints = checkpointsEntities;
                    adapter.setData(checkpoints);
                }
            });
            rView.setAdapter(adapter);
        //}

        if(editMode) {
            viewModel.getOrder().observe(this, orderEntity -> {
                if (orderEntity != null) {
                    order = orderEntity;
                    updateContent();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ONRESUME", "--------------------------------------");
        //setupOrderViewModel();
    }

    private void setupOrderViewModel(){
        //Create viewmodel
        OrderViewModel.Factory factory = new OrderViewModel.Factory(
                getApplication(), orderId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(OrderViewModel.class);
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
        eDelivDate.setFocusable(false);
        eDelivTime = findViewById(R.id.td_input_deliveryTime);
        eClient = findViewById(R.id.td_input_client);
        ePickupAddress = findViewById(R.id.td_input_pickupAddress);
        eDeliveryAddress = findViewById(R.id.td_input_deliveryAddress);
        spinnerRiders = findViewById(R.id.spinnerRiders);
        tvStatus = findViewById(R.id.td_input_status);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        btnDelete = findViewById(R.id.button_delete);
        btnChangeStatus = findViewById(R.id.button_change_status);
        btnCheckpoint = findViewById(R.id.button_checkpoints);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                saveChanges();
            }
        }
        );

        //get order ID from intent and set edit mode to false if new order
        editMode = currIntent.getBooleanExtra("isEdit", false);

        if(editMode){
            orderId = currIntent.getStringExtra("orderId");
        }else{
            btnChangeStatus.setVisibility(View.GONE);
        }
    }

    private void updateContent() {
        Log.d("UPDATECONTENT", "----------------------------- started updateContent()");
        if (order != null) {
            eQuantity.setText(Float.toString(order.getQuantity()));
            eDelivDate.setText(order.getDateDelivery());
            eDelivTime.setText(order.getTimeDelivery());
            if(clientSelected != null){
                eClient.setText(clientSelected);
            }else{
                eClient.setText(order.getIdCustomer());
            }
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

            if(order.getIdProduct() != null){
                Log.d("getidProduct", " IS NOT NULL");
                int spinnerPosition = adapterProductsList.getPosition(order.getIdProduct());
                spinnerProducts.setSelection(spinnerPosition);
            }
        }
    }

    public void Transport_button_delete(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete));
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Delete an order");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), (dialog, which) -> {
            viewModel.deleteOrder(order, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Delete order: success");
                    goToTripsActivity();
                }

                private void goToTripsActivity() {
                    Intent intent = new Intent(TransportDetailActivity.this, TransportListActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Exception e) {}
            });
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void Transport_button_clientList(View view)
    {
        //updateOrderDB(false, false);
        if(editMode){
            updateOrderDB(false, true);
        }
        Intent intent = new Intent(TransportDetailActivity.this, ClientListActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OLOLOLOLOL", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + requestCode + " " + resultCode);
        if(requestCode == 1){
            Log.d("OLOLOLOLOL", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + (resultCode == RESULT_OK));
            if(resultCode == RESULT_OK){
                clientSelected = data.getStringExtra("clientSelected");
                Log.d("ONACTIVITYRESULT", data.getStringExtra("clientSelected"));
                if(!editMode){
                    eClient.setText(data.getStringExtra("clientSelected"));
                }
            }
        }
    }

    public void Transport_button_photoScreen(View view)
    {
        Intent intent = new Intent(this, PhotoScreenActivity.class);
        startActivity(intent);
    }

    public void Transport_button_checkpoints(View view)
    {
        Intent intent = new Intent(this, CheckpointActivity.class);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
    }

    public void Transport_button_signScreen(View view)
    {
        Intent intent = new Intent(this, SignScreenActivity.class);
        startActivity(intent);
    }
    public void ButtonChangeStatus(View view)
    {
        updateOrderDB(true, false);
    }

    private void saveChanges() {

        if(editMode){
            updateOrderDB(false, false);
        }else{

            Order order = new Order();
            order.setIdProduct(spinnerProducts.getSelectedItem().toString());
            order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
            order.setDateDelivery(eDelivDate.getText().toString().trim());
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
    private void updateOrderDB(boolean isChangingStatus, boolean isQuickSave){
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
                if(!isChangingStatus && !isQuickSave){
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
