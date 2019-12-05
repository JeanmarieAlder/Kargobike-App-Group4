package com.example.kargobikeappg4.ui.checkpoint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.ListAdapter;
import com.example.kargobikeappg4.db.entities.Checkpoint;
import com.example.kargobikeappg4.ui.transport.ClientListActivity;
import com.example.kargobikeappg4.ui.transport.PhotoScreenActivity;
import com.example.kargobikeappg4.ui.transport.SignScreenActivity;
import com.example.kargobikeappg4.ui.transport.TransportDetailActivity;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.checkpoint.CheckpointViewModel;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductListViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class CheckpointActivity extends AppCompatActivity {

    private static final String TAG = "CheckpointActivity";
    //Variable instanciations
    private String checkpointId;
    private boolean editMode;
    private Checkpoint checkpoint;
    private CheckpointViewModel viewModel;

    private Button btnSave;

    private String orderId;

    private EditText eLat;
    private EditText eLng;
    private EditText eTimeStamp;
    private EditText eRemark;

    //private EditText eResponsibleRider;
    private DatabaseReference reff;
    private Spinner spinnerTypes;
    private ListAdapter adapterTypesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoints);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        orderId = getIntent().getExtras().get("orderId").toString();

        //Spinner for Products
        spinnerTypes = (Spinner) findViewById(R.id.spinnerTypes);
        adapterTypesList = new ListAdapter<>(CheckpointActivity.this, R.layout.rowlist, new ArrayList<>());
        spinnerTypes.setAdapter(adapterTypesList);

        String loc = "";

        //Create viewmodel
        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(
                getApplication(), loc, checkpointId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(CheckpointViewModel.class);


        //Fill the Rider list
        ArrayList<String> types = new ArrayList<String>();
        types.add("Train station");
        types.add("load");

        updateAdapterTypesList(types);


        if(editMode) {
            viewModel.getCheckpoint().observe(this, checkpointEntity -> {
                if (checkpointEntity != null) {
                    checkpoint = checkpointEntity;
                    updateContent();
                }
            });
        }
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


    private void saveChanges() {

        if(editMode){
            updateCheckpointDB(false);
        }else{
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setType(spinnerTypes.getSelectedItem().toString());
            checkpoint.setLat(Float.parseFloat(eLat.getText().toString()));
            checkpoint.setLng(Float.parseFloat(eLng.getText().toString()));
            checkpoint.setRemark(eRemark.getText().toString());
            checkpoint.setTimeStamp(eTimeStamp.getText().toString());
            //checkpoint.setIdOrder("-LuqKrFnlSaLXbnpSeoN");
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
}
