package com.example.kargobikeappg4.viewmodel.transport;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Transport;
import com.example.kargobikeappg4.db.repository.TransportRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TransportListViewModel extends AndroidViewModel {

    private TransportRepository repository;

    private final MediatorLiveData<List<Transport>> observableTransport;

    public TransportListViewModel(@NonNull Application application,
                              TransportRepository transportRepository) {
        super(application);
        this.repository = transportRepository;

        observableTransport = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableTransport.setValue(null);

        LiveData<List<Transport>> transports = transportRepository.getAllTransports();


        // observe the changes of the entities from the database and forward them
        observableTransport.addSource(transports, observableTransport::setValue);
    }

    public LiveData<List<Transport>> getAllTransports(){
        return observableTransport;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final TransportRepository transportRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            transportRepository = ((BaseApp) application).getTransportRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TransportListViewModel(application, transportRepository);
        }
    }
}