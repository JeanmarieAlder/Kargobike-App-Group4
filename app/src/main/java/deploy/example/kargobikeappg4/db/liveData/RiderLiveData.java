package deploy.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.entities.Rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RiderLiveData extends LiveData<Rider> {

    private final DatabaseReference reference;
    private final RiderLiveData.MyValueEventListener listener = new RiderLiveData.MyValueEventListener();

    public RiderLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Rider entity = dataSnapshot.getValue(Rider.class);
            if (entity == null) {
                return;
            } else {
                entity.setIdRider(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
