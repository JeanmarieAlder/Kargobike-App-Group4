package com.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.kargobikeappg4.db.entities.Zone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ZoneLiveData extends LiveData<Zone> {

    private final DatabaseReference reference;
    private final ZoneLiveData.MyValueEventListener listener = new ZoneLiveData.MyValueEventListener();

    public ZoneLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Zone entity = dataSnapshot.getValue(Zone.class);
            if (entity == null) {
                return;
            } else {
                entity.setIdZone(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
