package com.example.kargobikeappg4.viewmodel.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.kargobikeappg4.db.entities.User;
import com.example.kargobikeappg4.db.repository.UserRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;
import com.example.kargobikeappg4.viewmodel.user.UserListViewModel;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {
    private UserRepository repository;

    private final MediatorLiveData<List<User>> observableUser;

    public UserListViewModel(@NonNull Application application,
                              UserRepository userRepository) {
        super(application);
        this.repository = userRepository;

        observableUser = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableUser.setValue(null);

        LiveData<List<User>> users = userRepository.getAllUsers();


        // observe the changes of the entities from the database and forward them
        observableUser.addSource(users, observableUser::setValue);
    }

    public LiveData<List<User>> getAllUsers(){
        return observableUser;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
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
