package com.example.kargobikeappg4.db.liveData;

import com.example.kargobikeappg4.db.entities.Checkpointtype;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ChechkpointtypeListLiveData extends LiveData<List<Checkpointtype>> {

    private final DatabaseReference reference;
    private final ChechkpointtypeListLiveData.MyValueEventListener listener = new ChechkpointtypeListLiveData.MyValueEventListener();

    public ChechkpointtypeListLiveData(DatabaseReference reference) {
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
            setValue(totypes(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Checkpointtype> totypes(DataSnapshot snapshot){
        List<Checkpointtype> types = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Checkpointtype entity = childSnapshot.getValue(Checkpointtype.class);
            entity.setType(childSnapshot.getKey());
            types.add(entity);
        }

        return types;
    }


}
