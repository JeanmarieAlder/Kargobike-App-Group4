package deploy.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.WorkDetails;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;

import deploy.example.kargobikeappg4.db.liveData.WorkDetailsListLiveData;
import deploy.example.kargobikeappg4.db.liveData.WorkDetailsLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WorkDetailsRepository {
    private static final String TAG = "WorkDetailRepository";

    private static WorkDetailsRepository instance;

    //Constructor
    public static WorkDetailsRepository getInstance() {
        if (instance == null) {
            synchronized (WorkDetailsRepository.class) {
                if (instance == null) {
                    instance = new WorkDetailsRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all workDetails from one User
    public LiveData<List<WorkDetails>> getAllWorkDetailsFromUser(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("User")
                .child(userId)
                .child("workDetails");
        return new WorkDetailsListLiveData(reference);
    }

    //Query: get one workDetails from one User
    public LiveData<WorkDetails> getOneWorkDetails(String userId, String workDetailsId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("User")
                .child(userId)
                .child("workDetails")
                .child(workDetailsId);
        return new WorkDetailsLiveData(reference);
    }

    //Query: insert a checkpoint
    public void insert(final WorkDetails workDetails, final String idUser, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("User").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("User")
                .child(idUser)
                .child("workDetails")
                .child(id)
                .setValue(workDetails, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
