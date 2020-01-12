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
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    //Attributes
    private UserRepository repository;
    private final MediatorLiveData<List<User>> observableUser;

    //Constructor and initialize all values
    public UserListViewModel(@NonNull Application application,
                             UserRepository userRepository) {
        super(application);
        this.repository = userRepository;

        observableUser = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableUser.setValue(null);

        //Give all users back
        LiveData<List<User>> users = userRepository.getAllUsers();


        // observe the changes of the entities from the database and forward them
        observableUser.addSource(users, observableUser::setValue);
    }

    //Give all users back
    public LiveData<List<User>> getAllUsers() {
        return observableUser;
    }

    /**
     * A creator is used that afterward all users can be returned
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final UserRepository userRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            userRepository = ((BaseApp) application).getUserRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserListViewModel(application, userRepository);
        }
    }
}
