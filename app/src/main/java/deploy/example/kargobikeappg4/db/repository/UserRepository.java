package deploy.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.liveData.UserListLiveData;
import deploy.example.kargobikeappg4.db.liveData.UserLiveData;

import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserRepository {
    private static final String TAG = "UserRepository";

    private static UserRepository instance;

    //Constructor
    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all users
    public LiveData<List<User>> getAllUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("User");
        return new UserListLiveData(reference);
    }

    //get a user by id
    public LiveData<User> getUser(final String idUser){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("User")
                .child(idUser);
        return new UserLiveData(reference);
    }

    //Query: insert a user
    public void insert(final User user, final OnAsyncEventListener callback, String id) {
        //String id = FirebaseDatabase.getInstance().getReference("User").push().getKey();

        FirebaseDatabase.getInstance()
                .getReference("User")
                .child(id)
                .setValue(user, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a user
    public void update(final User user, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("User")
                .child(user.getIdUser())
                .updateChildren(user.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query delete a user
    public void delete(final User user, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("User")
                .child(user.getIdUser())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
