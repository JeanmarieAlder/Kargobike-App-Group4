package deploy.example.kargobikeappg4.db.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import deploy.example.kargobikeappg4.db.entities.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListLiveData extends LiveData<List<User>> {

    private final DatabaseReference reference;
    private final UserListLiveData.MyValueEventListener listener = new UserListLiveData.MyValueEventListener();

    //constructor
    public UserListLiveData(DatabaseReference reference) {
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
            setValue(toUsers(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    private List<User> toUsers(DataSnapshot snapshot) {
        //get values for list
        List<User> users = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            User entity = childSnapshot.getValue(User.class);
            entity.setIdUser(childSnapshot.getKey());
            users.add(entity);
        }
        return users;
    }
}
