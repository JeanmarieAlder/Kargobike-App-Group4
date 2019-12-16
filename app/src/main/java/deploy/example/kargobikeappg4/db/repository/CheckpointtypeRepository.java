package deploy.example.kargobikeappg4.db.repository;

import deploy.example.kargobikeappg4.db.entities.Checkpointtype;
import deploy.example.kargobikeappg4.db.liveData.ChechkpointtypeListLiveData;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CheckpointtypeRepository {

    private static final String TAG = "CheckpointtypeRepository";

    private static CheckpointtypeRepository instance;

    public CheckpointtypeRepository() {
    }

    //Constructor
    public static CheckpointtypeRepository getInstance() {
        if (instance == null) {
            synchronized (CheckpointtypeRepository.class) {
                if (instance == null) {
                    instance = new CheckpointtypeRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all types
    public LiveData<List<Checkpointtype>> getAllTypes() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Checkpointtype");
        return new ChechkpointtypeListLiveData(reference);
    }

    //Query: insert a location
    public void insert(final Checkpointtype type, final OnAsyncEventListener callback) {
        String id = type.getType();
        FirebaseDatabase.getInstance()
                .getReference("Checkpointtype")
                .child(id)
                .setValue(type, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
