package deploy.example.kargobikeappg4.ui.checkpoint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.ListAdapter;
import deploy.example.kargobikeappg4.db.entities.Checkpoint;
import deploy.example.kargobikeappg4.db.entities.Type;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.ui.transport.TransportListActivity;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.checkpoint.CheckpointViewModel;
import deploy.example.kargobikeappg4.viewmodel.type.TypeListViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import deploy.example.kargobikeappg4.viewmodel.user.UserListViewModel;

public class CheckpointActivity extends AppCompatActivity implements LocationListener{

    private static final String TAG = "CheckpointActivity";

    //Variable instanciations
    private boolean editMode;
    private Checkpoint checkpoint;
    private CheckpointViewModel viewModel;

    private Button btnSave;

    //Intent informations
    private String checkpointId;
    private String orderId;
    private String respRider;

    private EditText eLat;
    private EditText eLng;
    private EditText eTimeStamp;
    private EditText eRemark;

    //private EditText eResponsibleRider;
    private DatabaseReference reff;
    private Spinner spinnerTypes;
    private ListAdapter adapterTypesList;
    private TypeListViewModel typesViewModel;

    private ProgressBar loadingLatSpinner;
    private ProgressBar loadingLngSpinner;

    //GPS
    LocationManager locationManager;
    String locationText = "";
    String locationLatitude = "";
    String locationLongitude = "";
    private int mInterval = 2000; // 3 seconds by default, can be changed later
    private Handler mHandler;

    //Train station components
    private TextView tvArrivalCity;
    private EditText eArrivalCity;
    private TextView tvArrivalTime;
    private EditText eArrivalTime;
    private TextView tvNewResponsible;
    private Spinner spinnerNewResponsible;
    private boolean isTrainStation;
    private TimePicker timePicker;
    private String unsignedMinute;
    private String unsignedHour;

    //Train station requires userListViewmodel
    private UserListViewModel viewModelUsers;
    private List<User> userList;
    private ListAdapter userAdapter;


    //On create method, initialize all stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoints);

        isTrainStation = getIntent().getBooleanExtra("isTrainStation", false);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        if(isTrainStation){
            initializeTrainStation();
        }
        else{
            setTrainStationVisibility(View.GONE);
        }

        initTypeListViewModel();

        Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            mHandler = new Handler();
            startRepeatingTask();
        }, 5000);   //5 seconds

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        orderId = getIntent().getStringExtra("orderId");
        respRider = getIntent().getStringExtra("respRider");

        //Spinner for Products
        spinnerTypes = findViewById(R.id.spinnerTypes);
        adapterTypesList = new ListAdapter<>(CheckpointActivity.this, R.layout.rowlist, new ArrayList<>());
        spinnerTypes.setAdapter(adapterTypesList);


        //Create viewmodel
        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(
                getApplication(), orderId, checkpointId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(CheckpointViewModel.class);

        if(editMode) {
            viewModel.getCheckpoint().observe(this, checkpointEntity -> {
                if (checkpointEntity != null) {
                    checkpoint = checkpointEntity;
                    updateContent();
                }
            });
        }
    }
    private void initTypeListViewModel(){
        TypeListViewModel.Factory factoryTypes = new TypeListViewModel.Factory(
                getApplication());
        typesViewModel = ViewModelProviders.of(this, factoryTypes)
                .get(TypeListViewModel.class);

        typesViewModel.getAllTypes().observe(this, typeList -> {
            if (typeList != null) {

                Log.d(TAG,"Types Not null");
                //Array productNames
                ArrayList<String> typeNames = new ArrayList<>();
                for (Type t : typeList) {
                    typeNames.add(t.getName());
                }
                updateAdapterTypesList(typeNames);
            }
        });
    }

    private String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy / HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    private void updateAdapterTypesList(List<String> types) {
        adapterTypesList.updateData(new ArrayList<>(types));
        if(isTrainStation){
            String trainStationType = "Train Station";
            int spinnerPosition = adapterTypesList.getPosition(trainStationType);
            spinnerTypes.setSelection(spinnerPosition);
            spinnerTypes.setEnabled(false);
        }
    }

    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        spinnerTypes = findViewById(R.id.spinnerTypes);
        eLat = findViewById(R.id.td_input_lat);
        eLat.setEnabled(false);
        eLng = findViewById(R.id.td_input_lng);
        eLng.setEnabled(false);
        eTimeStamp = findViewById(R.id.td_input_timeStamp);
        eTimeStamp.setText(getCurrentDateTime());
        eRemark = findViewById(R.id.td_input_remark);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        loadingLatSpinner = findViewById(R.id.c_pb_loading_lat);
        loadingLngSpinner = findViewById(R.id.c_pb_loading_lng);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                saveChanges();
            }
        }
        );

        tvArrivalCity = findViewById(R.id.cp_tv_arrival_city);
        tvArrivalTime = findViewById(R.id.cp_tv_arrival_time);
        tvNewResponsible = findViewById(R.id.cp_tv_new_responsible);
        eArrivalCity = findViewById(R.id.cp_input_arrival_city);
        eArrivalTime = findViewById(R.id.cp_input_arrival_time);
        spinnerNewResponsible = findViewById(R.id.cp_spinner_riders);
        timePicker = findViewById(R.id.cp_timepicker_arrival_time);

        //get order ID from intent and set edit mode to false if new order
        editMode = getIntent().getBooleanExtra("isEdit", false);

        if(editMode){
            checkpointId = getIntent().getStringExtra("checkpointId");
            loadingLatSpinner.setVisibility(View.GONE);
            loadingLngSpinner.setVisibility(View.GONE);
        }
    }

    //Initialize train station
    private void initializeTrainStation(){
        setTrainStationVisibility(View.VISIBLE);
        setupUserViewModel();
    }

    //Trainstation, special, so only with this checkpoint some UI Elements will enabled
    public void setTrainStationVisibility(int viewType){
        tvArrivalCity.setVisibility(viewType);
        tvArrivalTime.setVisibility(viewType);
        tvNewResponsible.setVisibility(viewType);
        eArrivalCity.setVisibility(viewType);
        eArrivalTime.setVisibility(viewType);
        spinnerNewResponsible.setVisibility(viewType);
        timePicker.setVisibility(viewType);

        setupTimePicker();
    }

    //Create a TimePicker
    private void setupTimePicker(){
        //get current time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        timePicker.setIs24HourView(true);

        timePicker.setMinute(mMinute);
        timePicker.setHour(mHour);
        timePicker.setOnTimeChangedListener((viewTimePicker, hour, minutes) ->{

            //Look, that the date will be displayed right
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
            eArrivalTime.setText(newTime);
            Log.d(TAG, "TIME HAS CHANGED");
        });
    }

    //return all users
    private void setupUserViewModel(){
        userAdapter = new ListAdapter<>(CheckpointActivity.this,
                R.layout.rowlist, new ArrayList<>());
        spinnerNewResponsible.setAdapter(userAdapter);
        UserListViewModel.Factory factoryUser = new UserListViewModel.Factory(
                getApplication()
        );
        viewModelUsers = ViewModelProviders.of(this, factoryUser)
                .get(UserListViewModel.class);
        viewModelUsers.getAllUsers().observe(this, users -> {
            if (users != null) {

                userList = users;
                //Array productNames
                ArrayList<String> userNames = new ArrayList<>();
                for (User u : users) {
                    userNames.add(u.getName());
                }
                updateAdapterRiderList(userNames);
                updateContent();
            }
        });
    }

    private void updateAdapterRiderList(List<String> riderNames){
        userAdapter.updateData(new ArrayList<>(riderNames));
    }

    //Update the content of an existing Checkpoint
    private void updateContent() {
        if (checkpoint != null) {
            eLat.setText(Float.toString(checkpoint.getLat()));
            eLng.setText(Float.toString(checkpoint.getLng()));
            eTimeStamp.setText(checkpoint.getArrivalTimestamp());
            eRemark.setText(checkpoint.getRemark());

            int spinnerPosition = adapterTypesList.getPosition(checkpoint.getType());
            spinnerTypes.setSelection(spinnerPosition);
        }
    }

    //Delete a checkpoint
    public void Checkpoint_button_delete(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete));
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Delete a checkpoint");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), (dialog, which) -> {
            viewModel.deleteCheckpoint(checkpoint, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Delete checkpoint: success");
                    goToTripsActivity();
                }

                private void goToTripsActivity() {
                    Intent intent = new Intent(CheckpointActivity.this, TransportListActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Exception e) {}
            });
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }


    private void saveChanges() {

        //Requirements
        if(isTrainStation && eArrivalTime.getText().length() < 1){
            Toast.makeText(this, R.string.s_arrival_time_missing, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //There is one checkpoint, edit it
        if(editMode){
            updateCheckpointDB(false);
        }
        //create a new one
        else{
            Checkpoint checkpoint = new Checkpoint();

            //Getting the information from the UI
            checkpoint.setType(spinnerTypes.getSelectedItem().toString());
            if(eLat.getText().toString().length() > 0 && eLng.getText().toString().length() > 0){
                checkpoint.setLat(Float.parseFloat(eLat.getText().toString()));
                checkpoint.setLng(Float.parseFloat(eLng.getText().toString()));
            }

            checkpoint.setRemark(eRemark.getText().toString());
            checkpoint.setArrivalTimestamp(eTimeStamp.getText().toString().trim());
            checkpoint.setIdOrder(orderId);
            checkpoint.setResponsibleRider(respRider);
            if(isTrainStation){
                checkpoint.setArrivalCity(eArrivalCity.getText().toString());
                checkpoint.setArrivalTime(eArrivalTime.getText().toString());
                checkpoint.setNewResponsibleRider(getUserIdOfSelection());
            }

            viewModel.createCheckpoint(checkpoint, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Creation succesful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("checkpointCreated", true);
                    if(isTrainStation){
                        intent.putExtra("newResponsible",
                                spinnerNewResponsible.getSelectedItem().toString());

                        //TODO: Notification to new responsible rider
                    }
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //get the ID of a selected User
    private String getUserIdOfSelection(){

        String result = "userNotFound";
        for(User u : userList){
            if(u.getName().equals(spinnerNewResponsible.getSelectedItem().toString())){
                result = u.getIdUser();
                break;
            }
        }

        return result;
    }

    /**
     * Updates an existing order in the DB. Different behaviour if
     * the user only changes the status.
     * @param isChangingStatus true if only status is changing
     */
    private void updateCheckpointDB(boolean isChangingStatus){
        checkpoint.setType(spinnerTypes.getSelectedItem().toString());
        checkpoint.setLat(Float.parseFloat(eLat.getText().toString()));
        checkpoint.setLng(Float.parseFloat(eLng.getText().toString()));
        checkpoint.setRemark(eRemark.getText().toString());
        checkpoint.setArrivalTimestamp(eTimeStamp.getText().toString());
        Log.d("RESPID", spinnerTypes.getSelectedItem().toString());

        if(isTrainStation){
            checkpoint.setArrivalCity(eArrivalCity.getText().toString());
            checkpoint.setArrivalTime(eArrivalTime.getText().toString());
            checkpoint.setNewResponsibleRider(getUserIdOfSelection());
        }

        viewModel.updateCheckpoint(checkpoint, new OnAsyncEventListener() {
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

    //GSP Stuff, retrieved from the internet -> stackoverflow
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {

        //Run, that show the right coordinates
        @Override
        public void run() {

            try {
                getLocation(); //this function can change value of mInterval.

                if (locationText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Trying to retrieve coordinates.", Toast.LENGTH_LONG).show();
                }
                //there are coordinates, display this
                else {
                    if(eLat.getText().toString().isEmpty()){
                        eLat.setText(locationLatitude);
                        loadingLatSpinner.setVisibility(View.GONE);


                    }
                    if(eLng.getText().toString().isEmpty()){
                        eLng.setText(locationLongitude);
                        loadingLngSpinner.setVisibility(View.GONE);
                    }

                }
            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        if(mHandler != null){
            mHandler.removeCallbacks(mStatusChecker);
        }
    }

    //get the GPS cordinates
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //Get the GPS Coorinates
    @Override
    public void onLocationChanged(Location location) {

        locationText = location.getLatitude() + "," + location.getLongitude();
        locationLatitude = location.getLatitude() + "";
        locationLongitude = location.getLongitude() + "";
    }

    //Look, that GPS is enabled
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(CheckpointActivity.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}
