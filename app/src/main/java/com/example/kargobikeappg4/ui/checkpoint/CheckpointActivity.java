package com.example.kargobikeappg4.ui.checkpoint;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.ListAdapter;
import com.example.kargobikeappg4.db.entities.Checkpoint;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.checkpoint.CheckpointViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

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

    private EditText eLat;
    private EditText eLng;
    private EditText eTimeStamp;
    private EditText eRemark;

    //private EditText eResponsibleRider;
    private DatabaseReference reff;
    private Spinner spinnerTypes;
    private ListAdapter adapterTypesList;

    //GPS
    LocationManager locationManager;
    String locationText = "";
    String locationLatitude = "";
    String locationLongitude = "";
    private int mInterval = 3000; // 3 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoints);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        //GPS
        //Alert Dialog
        /*
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                CheckpointActivity.this);

        // Setting Dialog Title
        alertDialog2.setTitle("Notification");

        // Setting Dialog Message
        String string1 = "It will take some time (5 seconds)";

        alertDialog2.setMessage(string1);

        // Setting Icon to Dialog
        alertDialog2.setIcon(R.drawable.ic_launcher_background);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();
        */

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                mHandler = new Handler();
                startRepeatingTask();
            }
        }, 5000);   //5 seconds

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        orderId = getIntent().getExtras().get("orderId").toString();

        //Spinner for Products
        spinnerTypes = (Spinner) findViewById(R.id.spinnerTypes);
        adapterTypesList = new ListAdapter<>(CheckpointActivity.this, R.layout.rowlist, new ArrayList<>());
        spinnerTypes.setAdapter(adapterTypesList);

        //Fill the Rider list
        ArrayList<String> types = new ArrayList<String>();
        types.add("Train station");
        types.add("load");

        updateAdapterTypesList(types);

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

    private String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy / HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    private void updateAdapterTypesList(List<String> types) {
        adapterTypesList.updateData(new ArrayList<>(types));
    }

    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        spinnerTypes = findViewById(R.id.spinnerTypes);
        eLat = findViewById(R.id.td_input_lat);
        eLng = findViewById(R.id.td_input_lng);
        eTimeStamp = findViewById(R.id.td_input_timeStamp);
        eTimeStamp.setText(getCurrentDateTime());
        eRemark = findViewById(R.id.td_input_remark);
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
        editMode = getIntent().getBooleanExtra("isEdit", false);

        if(editMode){
            checkpointId = getIntent().getExtras().get("checkpointId").toString();
        }
    }

    private void updateContent() {
        if (checkpoint != null) {
            eLat.setText(Float.toString(checkpoint.getLat()));
            eLng.setText(Float.toString(checkpoint.getLng()));
            eTimeStamp.setText(checkpoint.getTimeStamp());
            eRemark.setText(checkpoint.getRemark());
        }
    }

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

        if(editMode){
            updateCheckpointDB(false);
        }else{
            Checkpoint checkpoint = new Checkpoint();

            checkpoint.setType(spinnerTypes.getSelectedItem().toString());
            checkpoint.setLat(Float.parseFloat(eLat.getText().toString()));
            checkpoint.setLng(Float.parseFloat(eLng.getText().toString()));
            checkpoint.setRemark(eRemark.getText().toString());
            checkpoint.setTimeStamp(eTimeStamp.getText().toString().trim());
            checkpoint.setIdOrder(orderId);

            viewModel.createCheckpoint(checkpoint, new OnAsyncEventListener() {
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
    private void updateCheckpointDB(boolean isChangingStatus){
        checkpoint.setType(spinnerTypes.getSelectedItem().toString());
        checkpoint.setLat(Float.parseFloat(eLat.getText().toString()));
        checkpoint.setLng(Float.parseFloat(eLng.getText().toString()));
        checkpoint.setRemark(eRemark.getText().toString());
        checkpoint.setTimeStamp(eTimeStamp.getText().toString());
        Log.d("RESPID", spinnerTypes.getSelectedItem().toString());

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

    //GSP Stuff
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {


            try {
                getLocation(); //this function can change value of mInterval.

                if (locationText.toString() == "") {
                    Toast.makeText(getApplicationContext(), "Trying to retrieve coordinates.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(eLat.getText().toString().isEmpty()){
                        eLat.setText(locationLatitude.toString());
                    }
                    if(eLng.getText().toString().isEmpty()){
                        eLng.setText(locationLongitude.toString());
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
        mHandler.removeCallbacks(mStatusChecker);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        locationText = location.getLatitude() + "," + location.getLongitude();
        locationLatitude = location.getLatitude() + "";
        locationLongitude = location.getLongitude() + "";
    }

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
