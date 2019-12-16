package deploy.example.kargobikeappg4.db.repository;

import deploy.example.kargobikeappg4.db.entities.Checkpoint;
import deploy.example.kargobikeappg4.db.liveData.CheckpointLiveData;
import deploy.example.kargobikeappg4.db.liveData.CheckpointsListLiveData;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CheckpointRepository {
    private static final String TAG = "CheckpointRepository";

    private static CheckpointRepository instance;

    public CheckpointRepository() {
    }

    //Constructor
    public static CheckpointRepository getInstance() {
        if (instance == null) {
            synchronized (CheckpointRepository.class) {
                if (instance == null) {
                    instance = new CheckpointRepository();
                }
            }
        }
        return instance;
    }

    //Query: get a checkpoint
    public LiveData<Checkpoint> getCheckpoint(final String orderId, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(orderId)
                .child("Checkpoints")
                .child(id);
        return new CheckpointLiveData(reference);
    }

    //Query: get checkpoints for an order
    public LiveData<List<Checkpoint>> getcheckpointsByOrder(final String orderId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(orderId)
                .child("Checkpoints");
        return new CheckpointsListLiveData(reference, orderId);
    }


    //Query: insert a checkpoint
    public void insert(final Checkpoint checkpoint, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Order").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(checkpoint.getIdOrder())
                .child("Checkpoints")
                .child(id)
                .setValue(checkpoint, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a checkpoint
    public void update(final Checkpoint checkpoint, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(checkpoint.getIdOrder())
                .child("Checkpoints")
                .child(checkpoint.getIdCheckpoint())
                .updateChildren(checkpoint.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: delete a checkpoint
    public void delete(final Checkpoint checkpoint, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(checkpoint.getIdOrder())
                .child("Checkpoints")
                .child(checkpoint.getIdCheckpoint())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
