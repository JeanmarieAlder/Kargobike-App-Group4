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
import com.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

public class RiderListViewModel extends AndroidViewModel {
    private RiderRepository repository;

    private final MediatorLiveData<List<Rider>> observableRider;

    public RiderListViewModel(@NonNull Application application,
                              RiderRepository riderRepository) {
        super(application);
        this.repository = riderRepository;

        observableRider = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableRider.setValue(null);

        LiveData<List<Rider>> riders = riderRepository.getAllRiders();

        // observe the changes of the entities from the database and forward them
        observableRider.addSource(riders, observableRider::setValue);
    }

    public LiveData<List<Rider>> getAllRiders(){
        return observableRider;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final RiderRepository riderRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            riderRepository = ((BaseApp) application).getRiderRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RiderListViewModel(application, riderRepository);
        }
    }
}