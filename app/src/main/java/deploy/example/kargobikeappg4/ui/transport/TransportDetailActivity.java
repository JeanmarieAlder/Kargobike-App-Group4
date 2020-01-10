package deploy.example.kargobikeappg4.ui.transport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import deploy.example.kargobikeappg4.db.entities.Order;
import deploy.example.kargobikeappg4.db.entities.User;

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
import android.widget.TimePicker;
import android.widget.Toast;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.ListAdapter;
import deploy.example.kargobikeappg4.adapter.RecyclerAdapter;
import deploy.example.kargobikeappg4.db.entities.Checkpoint;
import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.db.entities.Product;
import deploy.example.kargobikeappg4.ui.checkpoint.CheckpointActivity;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.checkpoint.CheckpointListViewModel;
import deploy.example.kargobikeappg4.viewmodel.checkpoint.CheckpointViewModel;
import deploy.example.kargobikeappg4.viewmodel.customer.CustomerViewModel;
import deploy.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import deploy.example.kargobikeappg4.viewmodel.product.ProductListViewModel;
import deploy.example.kargobikeappg4.viewmodel.user.UserListViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportDetailActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue ;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    public static int nbDelivery;
    private static final String TAG = "TransportDetailActivity";
    //Variable instanciations
    private String orderId;
    private boolean editMode;
    private boolean isLoaded;
    private Order order;
    private OrderViewModel viewModel;
    private ProductListViewModel viewModelProducts;
    private UserListViewModel viewModelUsers;
    private Intent currIntent;

    private Button btnSave;
    private Button btnCancel;
    private Button btnChangeStatus;
    private Button btnClient;

    private EditText eQuantity;
    private EditText eDelivDate;
    private EditText eDelivTime;
    private EditText eClient;
    private EditText ePickupAddress;
    private EditText eDeliveryAddress;
    private TextView tvStatus;
    private DatabaseReference reff;
    private Spinner spinnerProducts;
    private TimePicker timePicker;
    private ListAdapter adapterProductsList;

    private Spinner spinnerRiders;
    private ListAdapter adapterRidersList;

    //Checkpoint list
    private RecyclerAdapter<Checkpoint> adapter;
    private List<Checkpoint> checkpoints;
    private CheckpointListViewModel checkpointListViewModel;
    private CheckpointViewModel checkpointViewModel;
    private Checkpoint updatedCheckpoint;
    private RecyclerView rView;

    //variables to deal with timestamps
    private String newPickupTimestamp = null;
    private String newDeliveryTimestamp = null;

    private String clientSelected;
    private String idClientSelected;
    private String addressClientSelected;
    private CustomerViewModel customerViewModel;
    private Customer customer;

    private String unsignedMinute;
    private String unsignedHour;

    private String imageURL;

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
        eDelivDate.setOnClickListener(view -> {
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
        });

        //DateSetListener for Date Delivery
        DateSetListenerDelivery = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String dayy = Integer.toString(day);
                String monthh = Integer.toString(day);


                if(day <10){
                    dayy = "0"+day;

                }
                if(month <10){
                    monthh = "0"+month;

                }
                String date =  dayy+ "/" + monthh + "/" + year;
                eDelivDate.setText(date);
            }
        };
        setupTimePicker();


        //Receive all names of the users from DB
        UserListViewModel.Factory factory3 = new UserListViewModel.Factory(
                getApplication());
        viewModelUsers = ViewModelProviders.of(this, factory3)
                .get(UserListViewModel.class);

        viewModelUsers.getAllUsers().observe(this, users -> {
            if (users != null) {

                Log.d(TAG,"products Not null");
                //Array productNames
                ArrayList<String> userNames = new ArrayList<>();
                for (User u : users) {
                    userNames.add(u.getName());
                }
                updateAdapterRiderList(userNames);
            }
        });


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

            Log.d("CheckpointsRecyclerViewPosition", "-------------" + position);
            intent.putExtra("checkpointId", checkpoints.get(position).getIdCheckpoint());
            intent.putExtra("orderId", orderId);
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        if(editMode){
            CheckpointListViewModel.Factory factoryCheckpoints = new CheckpointListViewModel.Factory(
                    getApplication(), orderId
            );

            checkpointListViewModel = ViewModelProviders.of(this, factoryCheckpoints)
                    .get(CheckpointListViewModel.class);
            checkpointListViewModel.getAllCheckpoints().observe(this, checkpointsEntities -> {
                if (checkpointsEntities != null) {
                    checkpoints = checkpointsEntities;
                    adapter.setData(checkpoints);
                }
            });

            rView.setAdapter(adapter);
        }

        if(editMode) {
            viewModel.getOrder().observe(this, orderEntity -> {
                if (orderEntity != null) {
                    order = orderEntity;
                    setupCustomverViewModel();
                    updateContent();

                }
            });
        }
        updateContent();
    }

    private void setupTimePicker() {
        // Launch Time Picker Dialog

        //get current time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        timePicker.setVisibility(View.VISIBLE);

        timePicker.setMinute(mMinute);
        timePicker.setHour(mHour);
        timePicker.setOnTimeChangedListener((viewTimePicker, hour, minutes) ->{

            unsignedMinute = "" + timePicker.getMinute();
            unsignedHour = "" + timePicker.getHour();

            //Check that the hour and minutes each have two digits, if not, add a "0" in front
            if(unsignedMinute.length() < 2){
                unsignedMinute = "0" + unsignedMinute;
            }
            if(unsignedHour.length() < 2){
                unsignedHour = "0" + unsignedHour;
            }
            String newTime = "" + unsignedHour + ":" + unsignedMinute;
            eDelivTime.setText(newTime);
            Log.d(TAG, "TIME HAS CHANGED");
        });
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
    private void setupCustomverViewModel(){
        CustomerViewModel.Factory factory = new CustomerViewModel.Factory(
                getApplication(), order.getIdCustomer());
        customerViewModel = ViewModelProviders.of(this, factory)
                .get(CustomerViewModel.class);
        customerViewModel.getCustomer().observe(this, customerEntity ->{
            if (customerEntity != null) {
                customer = customerEntity;
                Log.d("customerViewModel", "---------------------- " + customerEntity.toString());
                updateContent();
            }
        });
    }

    private void updateAdapterProductsList(List<String> productNames) {
        adapterProductsList.updateData(new ArrayList<>(productNames));
    }

    private void updateAdapterRiderList(List<String> riderNames) {
        adapterRidersList.updateData(new ArrayList<>(riderNames));
    }

    private void initializeNotification(){

        mRequestQueue=Volley.newRequestQueue(this);

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
        timePicker = findViewById(R.id.td_timepicker_delivery_time);
        timePicker.setIs24HourView(true);
        tvStatus = findViewById(R.id.td_input_status);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        btnCancel = findViewById(R.id.td_button_cancel);
        btnChangeStatus = findViewById(R.id.button_change_status);
        btnClient = findViewById(R.id.button_ClientList);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                saveChanges();
                sendNotification();
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

    private void sendNotification(){

        initializeNotification();
        String userID = spinnerRiders.getSelectedItem().toString();


        for(int i=0; i<userID.length();i++)
        {
            if(userID.charAt(i)==' ') {
                String debut = userID.substring(0,i);
                String fin = userID.substring(i+1,userID.length());
                userID = debut+fin;
            }
        }


        Log.d("----------IDTOPIC-------- ",userID);

        JSONObject mainObj = new JSONObject();

        try {
            
            mainObj.put("to","/topics/"+userID);
            JSONObject notifObj = new JSONObject();
            notifObj.put("title",getResources().getString(R.string.s_title_notification));
            notifObj.put("body", "Hey "+userID+", "+getResources().getString(R.string.s_body_notification));
            mainObj.put("notification", notifObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization","key=AIzaSyC1XCuqDhyMVMIy_mQllyBAvSkIRDdINHc");
                    return header;
                }
            };

            mRequestQueue.add(request);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void updateContent() {
        Log.d("UPDATECONTENT", "----------------------------- started updateContent()");
        if (order != null) {
            setTitle("Order Detail (" + order.getStatus() + ")");
            eQuantity.setText(Float.toString(order.getQuantity()));
            eDelivDate.setText(order.getDateDelivery());
            eDelivTime.setText(order.getTimeDelivery());
            if(clientSelected != null){
                eClient.setText(clientSelected);
            }else if(customer != null){
                eClient.setText(customer.getBillingName());
            }
            ePickupAddress.setText(order.getIdPickupCheckpoint());
            eDeliveryAddress.setText(order.getIdDeliveryCheckpoint());

            //set status and button accordingly
            tvStatus.setText(order.getStatus());
            switch (order.getStatus()) {
                case "Loaded":
                    btnChangeStatus.setText("Unload");
                    break;
                case "Pending":
                    btnChangeStatus.setText("Load");
                    break;
                case "Delivered":

                case "Cancelled":
                    btnChangeStatus.setVisibility(View.GONE);
                    break;
            }
            if(order.getIdResponsibleRider() != null){
                int spinnerPosition = adapterRidersList.getPosition(order.getIdResponsibleRider());
                spinnerRiders.setSelection(spinnerPosition);
            }

            if(order.getIdProduct() != null){
                Log.d("getidProduct", " IS NOT NULL");
                int spinnerPosition = adapterProductsList.getPosition(order.getIdProduct());
                spinnerProducts.setSelection(spinnerPosition);
            }

            //If the order is cancelled or loaded, settings cannot change
            if(order.getStatus().equals("Cancelled") || order.getStatus().equals("Loaded")
            || order.getStatus().equals("Delivered")){
                eQuantity.setEnabled(false);
                eDelivDate.setEnabled(false);
                eClient.setEnabled(false);
                ePickupAddress.setEnabled(false);
                eDeliveryAddress.setEnabled(false);
                spinnerProducts.setEnabled(false);
                spinnerRiders.setEnabled(false);
                btnClient.setEnabled(false);
                btnCancel.setEnabled(false);

                timePicker.setVisibility(View.GONE);
            }
            else{
                eQuantity.setEnabled(true);
                eDelivDate.setEnabled(true);
                eClient.setEnabled(false);
                ePickupAddress.setEnabled(true);
                eDeliveryAddress.setEnabled(true);
                spinnerProducts.setEnabled(true);
                spinnerRiders.setEnabled(true);
                btnClient.setEnabled(true);
                btnCancel.setEnabled(true);

                timePicker.setVisibility(View.VISIBLE);
            }
        }
    }

    public void Transport_button_cancel(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.s_cancel));
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Cancel the order?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.s_cancel), (dialog, which) -> {
            updateOrderDB("Cancelled", false);
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.s_back), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void Transport_button_clientList(View view)
    {
        if(editMode){

            updateOrderDB(null, true);
        }
        Intent intent = new Intent(TransportDetailActivity.this, ClientListActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, " entered onActivityResult" + requestCode + " " + resultCode);
        if(requestCode == 1){
            Log.d(TAG, "Request code 1 " + (resultCode == RESULT_OK));
            if(resultCode == RESULT_OK){
                clientSelected = data.getStringExtra("clientSelected");
                idClientSelected = data.getStringExtra("idClientSelected");
                addressClientSelected = data.getStringExtra("addressClientSelected");
                Log.d("ONACTIVITYRESULT", data.getStringExtra("clientSelected"));
                if(!editMode){
                    eClient.setText(data.getStringExtra("clientSelected"));
                }

                //set Addresses if not filled only
                if(ePickupAddress.getText().length() <= 0){
                    ePickupAddress.setText(addressClientSelected);
                }
                if(eDeliveryAddress.getText().length() <= 0){
                    eDeliveryAddress.setText(addressClientSelected);
                }
            }
        }
        if(requestCode == 2){
            Log.d("OLOLOLOLOL", "OOOOOOOOOOOOOOOOOOOOOO " + (resultCode == RESULT_OK));
            if(resultCode == RESULT_OK
                    && data.getBooleanExtra("checkpointCreated", false)){
                if(data.getStringExtra("newResponsible") != null){
                    int spinnerPosition = adapterRidersList.
                            getPosition(data.getStringExtra("newResponsible"));
                    spinnerRiders.setSelection(spinnerPosition);
                }
                updateOrderDB("Pending", true);
                //Restart Activity in order to refresh viewModels
                finish();
                startActivity(getIntent());
            }
        }
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                imageURL = data.getStringExtra("SignatureURL");
                Log.d("IMAGE URL S", "The Url is : " + imageURL);
                updateOrderDB(null,true);

            }
        }
        if(requestCode == 4){
            if(resultCode == RESULT_OK){
                imageURL = data.getStringExtra("ImageURL");
                Log.d("IMAGE URL I", "The Url is : " + imageURL);
                updateOrderDB(null,true);

            }
        }
    }

    public void addCheckpoint(boolean isTrainStation)
    {
        isLoaded = true;
        Intent intent = new Intent(this, CheckpointActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("respRider", order.getIdResponsibleRider());
        intent.putExtra("isTrainStation", isTrainStation);
        startActivityForResult(intent, 2);
    }

    public void Transport_button_photoScreen(View view)
    {
        Intent intent = new Intent(this, PhotoScreenActivity.class);
        intent.putExtra("IdOrder", orderId );
        startActivityForResult(intent, 4);
    }

    public void Transport_button_signScreen(View view)
    {
        Intent intent = new Intent(TransportDetailActivity.this, SignScreenActivity.class);
        intent.putExtra("IdOrder", orderId );
        startActivityForResult(intent, 3);


    }
    public void ButtonChangeStatus(View view)
    {
        switch (order.getStatus()){
            case "Pending":
                managePickup();
                break;
            case "Loaded":
                //Prompts a popup to choose the method of unload
                showAlertDialogButtonClicked();
                break;

            default :
                Log.d("Button change status", order.getStatus());
                break;
        }
    }

    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.s_status_unload_popup);

        // add a list
        String[] menuOptions = {
                getResources().getString(R.string.s_status_unload_checkpoint),
                getResources().getString(R.string.s_status_unload_trainstation),
                getResources().getString(R.string.s_status_unload_destination)
        };
        builder.setItems(menuOptions, (dialog, which) -> {
            switch (which) {
                case 0: // checkpoint
                    addCheckpoint(false);
                    break;
                case 1: // train station
                    //TODO: Train station load (specify arrival and new responsible rider)
                    addCheckpoint(true);
                    break;
                case 2: // final destination
                    nbDelivery+=1;
                    manageDelivery();
                    //updateOrderDB("Delivered", true);
                    break;
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void managePickup(){
        isLoaded = false;
        if(checkpoints == null || checkpoints.size() <= 0){
            newPickupTimestamp = getCurrentDateTime();
            Log.d("MANAGEPICKUP", "LOADED---------------------- - - -");
            updateOrderDB("Loaded", false);
            newPickupTimestamp = null;
        }
        else{
            //Get the last checkpoint added and add departure timestamp
            setCheckpointDepartureTimestamp();
        }
    }
    private void manageDelivery(){
        newDeliveryTimestamp = getCurrentDateTime();
        updateOrderDB("Delivered", true);
        newDeliveryTimestamp = null;
    }

    private void setCheckpointDepartureTimestamp(){
        isLoaded = false;
        int lastCheckpointId = adapter.getItemCount() - 1;

        /*if(updatedCheckpoint != null){
            updatedCheckpoint.setDepartureTimestamp(getCurrentDateTime());
            checkpointViewModel.updateCheckpoint(updatedCheckpoint, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Timestamp Added", Toast.LENGTH_SHORT).show();
                    Log.d("CHECKPOINTOBSERVER", "HEEEEEEEEEEEEEEEEERE LOADED");
                    updateOrderDB("Loaded", true);
                }
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Update failed", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(
                getApplication(), orderId,
                checkpoints.get(lastCheckpointId).getIdCheckpoint());
        checkpointViewModel = ViewModelProviders.of(this, factory)
                .get(CheckpointViewModel.class);
        checkpointViewModel.getCheckpoint().observe(this, checkpointEntity -> {
            if (checkpointEntity != null) {
                updatedCheckpoint = checkpointEntity;
                updatedCheckpoint.setDepartureTimestamp(getCurrentDateTime());
                checkpointViewModel.updateCheckpoint(updatedCheckpoint, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("CHECKPOINTOBSERVER", "LOADED");
                        updateOrderDB("Loaded", true);

                        //Restart Activity in order to refresh viewModels
                        finish();
                        startActivity(getIntent());
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveChanges() {

        if(editMode){
            updateOrderDB(null, false);
        }else{

            Order order = new Order();
            order.setIdProduct(spinnerProducts.getSelectedItem().toString());
            order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
            order.setDateDelivery(eDelivDate.getText().toString().trim());
            order.setTimeDelivery(eDelivTime.getText().toString());
            if(idClientSelected != null){
                order.setIdCustomer(idClientSelected);
            }else{
                order.setIdCustomer("");
            }

            order.setIdPickupCheckpoint(ePickupAddress.getText().toString());
            order.setIdDeliveryCheckpoint(eDeliveryAddress.getText().toString());
            order.setIdResponsibleRider(spinnerRiders.getSelectedItem().toString());
            order.setStatus("Pending");

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
    private String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy / HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * Updates an existing order in the DB. Different behaviour if
     * the user only changes the status.
     * @param newStatus the new status to update to database
     */
    private void updateOrderDB(String newStatus, boolean isQuickSave){
        order.setIdProduct(spinnerProducts.getSelectedItem().toString());
        order.setQuantity(Float.parseFloat(eQuantity.getText().toString()));
        order.setDateDelivery(eDelivDate.getText().toString());
        order.setTimeDelivery(eDelivTime.getText().toString());
        if(newPickupTimestamp != null){
            order.setPickupTimestamp(newPickupTimestamp);
        }
        if(newDeliveryTimestamp != null){
            order.setDeliveryTimestamp(newDeliveryTimestamp);
        }
        if(idClientSelected != null){
            order.setIdCustomer(idClientSelected);
        }else{
            order.setIdCustomer(order.getIdCustomer());
        }
        if(imageURL != null){
            order.setSignature(imageURL);
        }
        order.setIdPickupCheckpoint(ePickupAddress.getText().toString());
        order.setIdDeliveryCheckpoint(eDeliveryAddress.getText().toString());
        order.setIdResponsibleRider(spinnerRiders.getSelectedItem().toString());
        Log.d("RESPID", spinnerRiders.getSelectedItem().toString());

        //Changes status if a new status has been provided
        if(newStatus != null){
            order.setStatus(newStatus);
        }

        viewModel.updateOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),
                        "Update succesful", Toast.LENGTH_LONG).show();
                if((newStatus == null) && !isQuickSave){
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
