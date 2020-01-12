package deploy.example.kargobikeappg4.db.liveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.Rider;
import deploy.example.kargobikeappg4.db.entities.Zone;

public class ZoneListLiveData extends LiveData<List<Zone>> {

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //constructure
    public ZoneListLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toZones(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private List<Zone> toZones(DataSnapshot snapshot) {
        List<Zone> zones = new ArrayList<>();
        //get values for list
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Zone entity = childSnapshot.getValue(Zone.class);
            if (entity != null) {
                entity.setIdZone(childSnapshot.getKey());
            }
            zones.add(entity);
        }
        //Sort zones by city
        zones.sort((z1, z2) -> z1.getCity().compareTo(z2.getCity()));
        return zones;
    }
}
