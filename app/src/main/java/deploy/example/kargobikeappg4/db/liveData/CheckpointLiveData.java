package deploy.example.kargobikeappg4.db.liveData;

import deploy.example.kargobikeappg4.db.entities.Checkpoint;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CheckpointLiveData extends LiveData<Checkpoint> {

    private final DatabaseReference reference;
    private final CheckpointLiveData.MyValueEventListener listener = new CheckpointLiveData.MyValueEventListener();

    //constructor
    public CheckpointLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //get entity Checkpoint
            Checkpoint entity = dataSnapshot.getValue(Checkpoint.class);
            if(entity == null){
                return;
            }else{
                entity.setIdCheckpoint(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    }
}
