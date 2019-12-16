package deploy.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.Address;
import deploy.example.kargobikeappg4.db.liveData.AddressLiveData;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddressRepository {
    private static final String TAG = "AddressRepository";

    private static AddressRepository instance;

    public AddressRepository() {
    }

    //Constructor
    public static AddressRepository getInstance() {
        if (instance == null) {
            synchronized (AddressRepository.class) {
                if (instance == null) {
                    instance = new AddressRepository();
                }
            }
        }
        return instance;
    }

    //get a address by id
    public LiveData<Address> getAddress(final String idAddress){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Address")
                .child(idAddress);
        return new AddressLiveData(reference);
    }

    //Query: insert a user
    public void insert(final Address address, final OnAsyncEventListener callback, String id) {

        FirebaseDatabase.getInstance()
                .getReference("Address")
                .child(id)
                .setValue(address, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
