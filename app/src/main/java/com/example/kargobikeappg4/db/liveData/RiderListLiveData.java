package com.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.kargobikeappg4.db.entities.Rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class RiderListLiveData extends LiveData<List<Rider>> {

    private final DatabaseReference reference;
    private final RiderListLiveData.MyValueEventListener listener = new RiderListLiveData.MyValueEventListener();

    public RiderListLiveData(DatabaseReference reference) {
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
            setValue(toRiders(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Rider> toRiders(DataSnapshot snapshot){
        List<Rider> riders = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Rider entity = childSnapshot.getValue(Rider.class);
            entity.setIdRider(childSnapshot.getKey());
            riders.add(entity);
        }

        return riders;
    }



}
