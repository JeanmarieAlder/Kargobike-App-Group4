package deploy.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.entities.WorkDetails;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkDetailsListLiveData extends LiveData<List<WorkDetails>> {

    private final DatabaseReference reference;
    private final WorkDetailsListLiveData.MyValueEventListener listener = new WorkDetailsListLiveData.MyValueEventListener();

    public WorkDetailsListLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() { }


    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(workDetails(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<WorkDetails> workDetails (DataSnapshot snapshot) {
        List<WorkDetails> wd = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            WorkDetails entity = childSnapshot.getValue(WorkDetails.class);
            entity.setIdWorkDetails(childSnapshot.getKey());
            wd.add(entity);
        }
        return wd;
    }
}
