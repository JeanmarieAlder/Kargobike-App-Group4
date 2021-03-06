package deploy.example.kargobikeappg4.db.liveData;

import android.util.Log;

import deploy.example.kargobikeappg4.db.entities.Checkpoint;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CheckpointsListLiveData extends LiveData<List<Checkpoint>> {

    private static final String TAG = "CheckpointsListLiveData";

    private final DatabaseReference reference;
    private final String orderId;
    private final MyValueEventListener listener = new MyValueEventListener();

    //constructor
    public CheckpointsListLiveData(DatabaseReference reference, String orderId) {
        this.reference = reference;
        this.orderId = orderId;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }


    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toCheckpoints(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<Checkpoint> toCheckpoints(DataSnapshot snapshot) {
        //get values for list
        List<Checkpoint> checkpoints = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Checkpoint entity = childSnapshot.getValue(Checkpoint.class);
            entity.setIdCheckpoint(childSnapshot.getKey());
            entity.setIdOrder(orderId);
            checkpoints.add(entity);
        }

        return checkpoints;
    }


}

