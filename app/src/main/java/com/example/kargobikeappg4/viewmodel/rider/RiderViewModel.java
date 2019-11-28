package com.example.kargobikeappg4.viewmodel.rider;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.kargobikeappg4.db.entities.Rider;
import com.example.kargobikeappg4.db.repository.RiderRepository;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.BaseApp;

public class RiderViewModel extends AndroidViewModel {

    private RiderRepository repository;
    private final MediatorLiveData<Rider> observableRider;

    public RiderViewModel(@NonNull Application application,
                          final String riderId, RiderRepository repository) {
        super(application);

        this.repository = repository;

        observableRider = new MediatorLiveData<>();
        observableRider.setValue(null); //Null by default until we get data from DB
        if(riderId != null)
        {
            LiveData<Rider> rider = repository.getRider(riderId);

            //observer changes from db and forward them
            observableRider.addSource(rider, observableRider::setValue);
        }

    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String riderId;

        private final RiderRepository repository;

        public Factory(@NonNull Application application, String actId) {
            this.application = application;
            this.riderId = actId;
            repository = ((BaseApp) application).getRiderRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RiderViewModel(application, riderId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<Rider> getRider() {
        return observableRider;
    }

    public void createRider(Rider rider, OnAsyncEventListener callback) {
        repository.insert(rider, callback);
    }

    public void updateRider(Rider rider, OnAsyncEventListener callback) {
        repository.update(rider, callback);
    }
}
