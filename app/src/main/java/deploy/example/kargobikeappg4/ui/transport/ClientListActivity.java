package deploy.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.adapter.RecyclerAdapter;
import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.viewmodel.customer.CustomerListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //Attributes
    private RecyclerAdapter<Customer> adapter;
    private List<Customer> customers;
    private List<Customer> customersFiltered;
    private CustomerListViewModel listViewModel;
    private RecyclerView rView;
    private SearchView sView;

    //On create method, initialize all stuff
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

    //initialize the list
    private void initRecyclerView(){
        //initializes recyclerview
        rView = findViewById(R.id.cl_rv_client_result);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes
    }

    //initialize the adapter for the list
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

    //initialize the viewmodel, -> getting the clients afterwards
    private void initViewModel(){
        CustomerListViewModel.Factory factory = new CustomerListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(CustomerListViewModel.class);
        setViewModelData("");
    }

    //Set all clients to the list
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

    //Filter the clients by the name
    private void applyFilter(String filterString){
        customersFiltered = new ArrayList<>();
        for(Customer c : customers){
            if(c.getBillingName().toLowerCase().contains(filterString)){
                customersFiltered.add(c);
            }
        }
    }

    //Go one site back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Select a client form the list
    public void onClientSelected(View view) {
        Intent intent = new Intent();
        intent.putExtra("clientSelected", "The selected Client");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    //Add a new client to the DB
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
