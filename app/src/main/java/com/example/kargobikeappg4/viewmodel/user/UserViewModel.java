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
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.BaseApp;
import com.example.kargobikeappg4.viewmodel.user.UserViewModel;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private final MediatorLiveData<User> observableUser;

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
     * A creator is used to inject the account id into the ViewModel
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
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<User> getUser() {
        return observableUser;
    }

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