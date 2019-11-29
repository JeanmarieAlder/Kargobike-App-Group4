package com.example.kargobikeappg4.db.liveData;

import com.example.kargobikeappg4.db.entities.Transport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TransportListLiveData extends LiveData<List<Transport>> {

    private final DatabaseReference reference;
    private final TransportListLiveData.MyValueEventListener listener = new TransportListLiveData.MyValueEventListener();

    public TransportListLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() { }


    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toTransports(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Transport> toTransports(DataSnapshot snapshot){
        List<Transport> transports = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Transport entity = childSnapshot.getValue(Transport.class);
            entity.setIdTransport(childSnapshot.getKey());
            transports.add(entity);
        }

        return transports;
    }
}

