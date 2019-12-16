package deploy.example.kargobikeappg4.ui.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import deploy.example.kargobikeappg4.adapter.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.ArrayList;
import java.util.List;

public class CustomerAddActivity extends AppCompatActivity {

    private Spinner spinnerTitle;
    private EditText eFirstname;
    private EditText eLastname;
    private EditText eCompany;
    private EditText eAddress;
    private EditText eCity;
    private EditText ePostcode;

    private ListAdapter titleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);

        initializeLayout();
    }
    private void initializeLayout(){
        spinnerTitle = findViewById(R.id.ca_spinner_title);
        eFirstname = findViewById(R.id.ca_input_firstname);
        eLastname = findViewById(R.id.ca_input_lastname);
        eCompany = findViewById(R.id.ca_input_company);
        eAddress = findViewById(R.id.ca_input_address);
        eCity = findViewById(R.id.ca_input_city);
        ePostcode = findViewById(R.id.ca_input_postcode);

        List<String> titles = new ArrayList<>();
        titles.add("");
        titles.add("Mr");
        titles.add("Mrs");
        titles.add("Dr");

        titleListAdapter = new ListAdapter<>(CustomerAddActivity.this, R.layout.rowlist, titles);
        spinnerTitle.setAdapter(titleListAdapter);
    }

    public void createCustomer(View view){
        Customer newCustomer = new Customer();

        String billingName = eLastname.getText().toString()
                + " " + eFirstname.getText().toString()
                + " " + eCompany.getText().toString();
        String address = eAddress.getText().toString()
                + ", " + ePostcode.getText().toString()
                + " " + eCity.getText().toString();
        newCustomer.setBillingName(billingName);
        newCustomer.setIdAddress(address);
        newCustomer.setIdProduct("none");
        newCustomer.setTitre(spinnerTitle.getSelectedItem().toString());

        ((BaseApp) getApplication())
            .getCustomerRepository().insert(newCustomer, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Creation succesful", Toast.LENGTH_LONG).show();
                    onBackPressed(); //finally, go back to previous screen
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Creation failed", Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("TEST", "TEST------");
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
