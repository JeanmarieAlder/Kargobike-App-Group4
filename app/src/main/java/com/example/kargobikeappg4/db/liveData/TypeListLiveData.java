package com.example.kargobikeappg4.db.liveData;

import com.example.kargobikeappg4.db.entities.Type;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TypeListLiveData extends LiveData<List<Type>> {

    private final DatabaseReference reference;
    private final TypeListLiveData.MyValueEventListener listener = new TypeListLiveData.MyValueEventListener();

    public TypeListLiveData(DatabaseReference reference) {
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
            setValue(toTypes(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Type> toTypes(DataSnapshot snapshot){
        List<Type> types = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Type entity = childSnapshot.getValue(Type.class);
            if (entity != null) {
                entity.setIdType(childSnapshot.getKey());
                types.add(entity);
            }
        }

        //Sort types by name
        types.sort((t1, t2) -> t1.getName().compareTo(t2.getName()));
        return types;
    }
}
