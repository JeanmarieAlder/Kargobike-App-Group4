package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.Customer;
import com.example.kargobikeappg4.viewmodel.customer.CustomerListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerAdapter<Customer> adapter;
    private List<Customer> customers;
    private List<Customer> customersFiltered;
    private CustomerListViewModel listViewModel;
    private RecyclerView rView;
    private SearchView sView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        customers = new ArrayList<>();
        customersFiltered = new ArrayList<>();
        // Locate the EditText in listview_main.xml
        sView = (SearchView) findViewById(R.id.cl_sb_client_search);
        sView.setOnQueryTextListener(this);

        initRecyclerView();
        initAdapter();
        initViewModel();

    }

    private void initRecyclerView(){
        //initializes recyclerview
        rView = findViewById(R.id.cl_rv_client_result);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes
    }
    private void initAdapter(){
        //Add click listener, opens details of the selected act
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent();
            intent.putExtra("clientSelected",
                    customersFiltered.get(position).getBillingName());
            intent.putExtra("idClientSelected",
                    customersFiltered.get(position).getIdCustomer());
            intent.putExtra("addressClientSelected",
                    customersFiltered.get(position).getIdAddress());
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        });
    }
    private void initViewModel(){
        CustomerListViewModel.Factory factory = new CustomerListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(CustomerListViewModel.class);
        setViewModelData("");
    }
    private void setViewModelData(String filterString){
        listViewModel.getAllCustomers().observe(this, customerEntities -> {
            if (customerEntities != null) {
                customers = customerEntities;

                if(filterString.length() > 0 && customers != null){
                    applyFilter(filterString);
                }else{
                    //if no filter, the list is complete
                    customersFiltered = customers;
                }

                adapter.setData(customersFiltered);
            }
        });
        rView.setAdapter(adapter);
    }
    private void applyFilter(String filterString){
        customersFiltered = new ArrayList<>();
        for(Customer c : customers){
            if(c.getBillingName().toLowerCase().contains(filterString)){
                customersFiltered.add(c);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClientSelected(View view) {
        Intent intent = new Intent();
        intent.putExtra("clientSelected", "The selected Client");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
    public void buttonAddClient(View view){
        Intent intent = new Intent(ClientListActivity.this, CustomerAddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("onquerytextchange", "------------------- " + newText);

        setViewModelData(newText.toLowerCase().trim());
        return false;
    }
}
