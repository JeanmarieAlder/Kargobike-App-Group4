package deploy.example.kargobikeappg4.db.liveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.TrainStation;

public class TrainStationListLiveData extends LiveData<List<TrainStation>> {

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //constructor
    public TrainStationListLiveData(DatabaseReference reference) {
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
            setValue(toTrainStations(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private List<TrainStation> toTrainStations(DataSnapshot snapshot) {
        //get values for list
        List<TrainStation> trainStations = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            TrainStation entity = childSnapshot.getValue(TrainStation.class);
            if (entity != null) {
                entity.setIdTrainStation(childSnapshot.getKey());
                trainStations.add(entity);
            }
        }

        //Sort types by name
        trainStations.sort((t1, t2) -> t1.getName().compareTo(t2.getName()));
        return trainStations;
    }
}
