package deploy.example.kargobikeappg4.db.liveData;

import deploy.example.kargobikeappg4.db.entities.WorkDetails;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class WorkDetailsLiveData extends LiveData<WorkDetails> {

    private final DatabaseReference reference;
    private final WorkDetailsLiveData.MyValueEventListener listener = new WorkDetailsLiveData.MyValueEventListener();

    //constructor
    public WorkDetailsLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //get entity WorkDetails
            WorkDetails entity = dataSnapshot.getValue(WorkDetails.class);
            if (entity == null) {
                return;
            } else {
                entity.setIdWorkDetails(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    }
}
