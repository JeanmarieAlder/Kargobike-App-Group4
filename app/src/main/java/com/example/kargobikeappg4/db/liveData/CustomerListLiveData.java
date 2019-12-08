package com.example.kargobikeappg4.db.liveData;

import android.util.Log;

import com.example.kargobikeappg4.db.entities.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CustomerListLiveData extends LiveData<List<Customer>> {
    private static final String TAG = "CustomerListLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public CustomerListLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive"); }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toCustomers(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<Customer> toCustomers(DataSnapshot snapshot){
        List<Customer> customers = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Customer entity = childSnapshot.getValue(Customer.class);
            entity.setIdCustomer(childSnapshot.getKey());
            customers.add(entity);
        }

        return customers;
    }
}
