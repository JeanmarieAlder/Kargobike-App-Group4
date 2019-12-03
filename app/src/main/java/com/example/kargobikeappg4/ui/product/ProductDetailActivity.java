package com.example.kargobikeappg4.ui.product;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.ListAdapter;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.ui.transport.TransportDetailActivity;
import com.example.kargobikeappg4.ui.transport.TransportListActivity;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.order.OrderViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";

    //Variable instanciations
    private String productId;
    private boolean editMode;
    private Product product;
    private ProductViewModel viewModel;

    private Button btnSave;
    private Button btnDelete;

    //private EditText eProduct;
    private EditText ename;
    private EditText eDescription;
    private EditText ePricee;
    private EditText eDuration;
    private EditText eInterurbain;

    //DB
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Initializes buttons, views, current ID and edit mode
        initialize();

        //Create viewmodel
        ProductViewModel.Factory factory = new ProductViewModel.Factory(
                getApplication(), productId);
        viewModel = ViewModelProviders.of(this, factory)
                .get(ProductViewModel.class);


        if (editMode) {
            viewModel.getProduct().observe(this, productEntity -> {
                if (productEntity != null) {
                    product = productEntity;
                    updateContent();
                }
            });
        }
    }

    /**
     * Initializes views, buttons, id and editmode
     */
    private void initialize() {
        ename = findViewById(R.id.td_input_name);
        eDescription = findViewById(R.id.td_input_description);
        ePricee = findViewById(R.id.td_input_price);
        eDuration = findViewById(R.id.td_input_duration);
        eInterurbain = findViewById(R.id.td_input_interurbain);
        reff = FirebaseDatabase.getInstance().getReference().child("Order");
        btnSave = findViewById(R.id.button_save);
        btnDelete = findViewById(R.id.button_delete);
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
            productId = getIntent().getExtras().get("productId").toString();
        }
    }

    private void updateContent() {
        if (product != null) {
            ePricee.setText(Float.toString(product.getPrice()));
            eDuration.setText(Float.toString(product.getDuration()));
            ename.setText(product.getName());
            eDescription.setText(product.getDescription());
            eInterurbain.setText("Yes");
        }
    }

    public void Transport_button_delete(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete));
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Delete an order");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete), (dialog, which) -> {
            viewModel.deleteProduct(product, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Delete trip: success");
                    goToTripsActivity();
                }

                private void goToTripsActivity() {
                    Intent intent = new Intent(ProductDetailActivity.this, ProductListActivity.class);
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
            updateProductDB(false);
        }else{
            Product product = new Product();

            product.setName(ename.getText().toString());
            product.setDescription(eDescription.getText().toString());
            product.setInterurbain(true);
            product.setPrice(Float.parseFloat(ePricee.getText().toString()));
            product.setDuration(Float.parseFloat(eDuration.getText().toString()));

            viewModel.createProduct(product, new OnAsyncEventListener() {
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
    private void updateProductDB(boolean isChangingStatus){
        Product product = new Product();
        product.setName(ename.getText().toString());
        product.setDescription(eDescription.getText().toString());
        product.setInterurbain(true);
        product.setPrice(Float.parseFloat(ePricee.getText().toString()));
        product.setDuration(Float.parseFloat(eDuration.getText().toString()));

        viewModel.updateProduct(product, new OnAsyncEventListener() {
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
