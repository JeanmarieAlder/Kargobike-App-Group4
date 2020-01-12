package deploy.example.kargobikeappg4.db.liveData;

import deploy.example.kargobikeappg4.db.entities.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CustomerLiveData extends LiveData<Customer> {
    private final DatabaseReference reference;
    private final CustomerLiveData.MyValueEventListener listener = new CustomerLiveData.MyValueEventListener();

    //constructor
    public CustomerLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //get entity Customer
            Customer entity = dataSnapshot.getValue(Customer.class);
            if(entity == null){
                return;
            }else{
                entity.setIdCustomer(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    }
}
