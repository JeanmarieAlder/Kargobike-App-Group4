package com.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;
import com.example.kargobikeappg4.db.entities.Rider;
import com.example.kargobikeappg4.db.liveData.RiderListLiveData;
import com.example.kargobikeappg4.db.liveData.RiderLiveData;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RiderRepository {

    private static final String TAG = "RiderRepository";

    private static RiderRepository instance;

    public RiderRepository() {
    }

    //Constructor
    public static RiderRepository getInstance() {
        if (instance == null) {
            synchronized (RiderRepository.class) {
                if (instance == null) {
                    instance = new RiderRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all riders
    public LiveData<List<Rider>> getAllRiders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Rider");
        return new RiderListLiveData(reference);
    }

    //Query: get one rider by id
    public LiveData<Rider> getRider(final String idRider){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Rider")
                .child(idRider);
        return new RiderLiveData(reference);
    }

    //Query: insert a rider
    public void insert(final Rider rider, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Rider").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Rider")
                .child(id)
                .setValue(rider, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a rider
    public void update(final Rider rider, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("riders")
                .child(rider.getIdRider())
                .updateChildren(rider.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
