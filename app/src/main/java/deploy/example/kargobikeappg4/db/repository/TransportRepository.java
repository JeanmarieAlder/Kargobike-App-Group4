package deploy.example.kargobikeappg4.db.repository;

import deploy.example.kargobikeappg4.db.entities.Transport;
import deploy.example.kargobikeappg4.db.liveData.TransportListLiveData;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TransportRepository {

    private static final String TAG = "TransportRepository";

    private static TransportRepository instance;

    public TransportRepository() {
    }

    //Constructor
    public static TransportRepository getInstance() {
        if (instance == null) {
            synchronized (TransportRepository.class) {
                if (instance == null) {
                    instance = new TransportRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all transports
    public LiveData<List<Transport>> getAllTransports() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Transport");
        return new TransportListLiveData(reference);
    }

    //Query: insert a order
    public void insert(final Transport transport, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Transport").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Transport")
                .child(id)
                .setValue(transport, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a order
    public void update(final Transport transport, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(transport.getIdTransport())
                .updateChildren(transport.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
