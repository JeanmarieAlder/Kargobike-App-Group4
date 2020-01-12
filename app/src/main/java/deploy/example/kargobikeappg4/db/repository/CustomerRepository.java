package deploy.example.kargobikeappg4.db.repository;

import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.db.liveData.CustomerListLiveData;
import deploy.example.kargobikeappg4.db.liveData.CustomerLiveData;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CustomerRepository {
    private static final String TAG = "CustomerRepository";

    private static CustomerRepository instance;

    public CustomerRepository() {
    }

    //Constructor
    public static CustomerRepository getInstance() {
        if (instance == null) {
            synchronized (CustomerRepository.class) {
                if (instance == null) {
                    instance = new CustomerRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all orders
    public LiveData<List<Customer>> getAllCustomers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Customer");
        return new CustomerListLiveData(reference);
    }

    //Query: get one order
    public LiveData<Customer> getCustomer(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Customer")
                .child(id);
        return new CustomerLiveData(reference);
    }

    //Query: insert a order
    public void insert(final Customer customer, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Order").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Customer")
                .child(id)
                .setValue(customer, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
