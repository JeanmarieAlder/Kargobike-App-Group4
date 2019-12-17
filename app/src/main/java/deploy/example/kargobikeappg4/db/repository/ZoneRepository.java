package deploy.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.liveData.ZoneListLiveData;
import deploy.example.kargobikeappg4.db.liveData.ZoneLiveData;

import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ZoneRepository {

    private static final String TAG = "ZoneRepository";

    private static ZoneRepository instance;

    public ZoneRepository() {
    }

    //Constructor
    public static ZoneRepository getInstance() {
        if (instance == null) {
            synchronized (ZoneRepository.class) {
                if (instance == null) {
                    instance = new ZoneRepository();
                }
            }
        }
        return instance;
    }

    //Query: get one zone by id
    public LiveData<Zone> getZone(final String idZone){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Zone")
                .child(idZone);
        return new ZoneLiveData(reference);
    }
    public LiveData<List<Zone>> getAllZones(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Zone");
        return new ZoneListLiveData(reference);
    }

    //Query: insert a order
    public void insert(final Zone zone, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Zone").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Zone")
                .child(id)
                .setValue(zone, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a order
    public void update(final Zone zone, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Zone")
                .child(zone.getIdZone())
                .updateChildren(zone.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Zone zone, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Zone")
                .child(zone.getIdZone())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
