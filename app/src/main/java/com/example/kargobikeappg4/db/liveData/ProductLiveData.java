package com.example.kargobikeappg4.db.liveData;

import com.example.kargobikeappg4.db.entities.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProductLiveData extends LiveData<Product> {

    private final DatabaseReference reference;
    private final ProductLiveData.MyValueEventListener listener = new ProductLiveData.MyValueEventListener();

    public ProductLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Product entity = dataSnapshot.getValue(Product.class);
            if(entity == null){
                return;
            }else{
                entity.setIdProduct(dataSnapshot.getKey());
                setValue(entity);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}

