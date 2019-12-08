package com.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private CustomerListViewModel listViewModel;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        customers = new ArrayList<>();

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
            intent.putExtra("clientSelected", customers.get(position).getBillingName());
            intent.putExtra("idClientSelected", customers.get(position).getIdCustomer());
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
        listViewModel.getAllCustomers().observe(this, customerEntities -> {
            if (customerEntities != null) {
                //TODO: try adding filter here
                customers = customerEntities;
                adapter.setData(customers);
            }
        });
        rView.setAdapter(adapter);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
