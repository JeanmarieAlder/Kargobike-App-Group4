package deploy.example.kargobikeappg4.viewmodel.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import deploy.example.kargobikeappg4.db.entities.User;
import deploy.example.kargobikeappg4.db.repository.UserRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class UserViewModel extends AndroidViewModel {

    //Atributes
    private UserRepository repository;
    private final MediatorLiveData<User> observableUser;


    //Constructor and initialize all values
    public UserViewModel(@NonNull Application application,
                          final String userId, UserRepository repository) {
        super(application);

        this.repository = repository;

        observableUser = new MediatorLiveData<>();
        observableUser.setValue(null); //Null by default until we get data from DB
        if(userId != null)
        {
            LiveData<User> user = repository.getUser(userId);

            //observer changes from db and forward them
            observableUser.addSource(user, observableUser::setValue);
        }

    }

    /**
     * A creator is used to inject the user id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String userId;

        private final UserRepository repository;

        public Factory(@NonNull Application application, String actId) {
            this.application = application;
            this.userId = actId;
            repository = ((BaseApp) application).getUserRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserViewModel(application, userId, repository);
        }
    }

    /**
     * Expose the LiveData UserEntity query so the UI can observe it.
     */
    //Give one user back
    public LiveData<User> getUser() {
        return observableUser;
    }


    //all other queries
    public void createUser(User user, OnAsyncEventListener callback, String id) {
        repository.insert(user, callback, id);
    }

    public void updateUser(User user, OnAsyncEventListener callback) {
        repository.update(user, callback);
    }

    public void deleteUser(User user, OnAsyncEventListener callback) {
        repository.delete(user, callback);
    }
}
