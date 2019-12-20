package deploy.example.kargobikeappg4.db.liveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.entities.Function;

public class FunctionListLiveData extends LiveData<List<Function>> {

    private final DatabaseReference reference;
    private final FunctionListLiveData.MyValueEventListener listener = new FunctionListLiveData.MyValueEventListener();

    public FunctionListLiveData(DatabaseReference reference) {
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
            setValue(toFunction(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    }

    private List<Function> toFunction(DataSnapshot snapshot){
        List<Function> functions = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Function entity = childSnapshot.getValue(Function.class);
            if (entity != null) {
                entity.setIdFunction(childSnapshot.getKey());
                functions.add(entity);
            }
        }

        //Sort functions by name
        functions.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
        return functions;
    }
}
