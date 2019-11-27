package com.example.kargobikeappg4.db.liveData;

import com.example.kargobikeappg4.db.entities.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OrderLiveData extends LiveData<Order> {
    private final DatabaseReference reference;
    private final OrderLiveData.MyValueEventListener listener = new OrderLiveData.MyValueEventListener();

    public OrderLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Order entity = dataSnapshot.getValue(Order.class);
            if(entity == null){
                return;
            }else{
                entity.setIdOrder(dataSnapshot.getKey());
                setValue(entity);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    }
}
