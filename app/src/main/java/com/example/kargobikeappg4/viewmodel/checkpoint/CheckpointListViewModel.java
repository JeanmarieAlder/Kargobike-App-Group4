package com.example.kargobikeappg4.viewmodel.checkpoint;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Checkpoint;
import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.repository.CheckpointRepository;
import com.example.kargobikeappg4.db.repository.OrderRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;
import com.example.kargobikeappg4.viewmodel.order.OrderListViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckpointListViewModel extends AndroidViewModel {
    private CheckpointRepository repository;

    private final MediatorLiveData<List<Checkpoint>> observableCheckpoint;

    public CheckpointListViewModel(@NonNull Application application, final String orderId,
                                   CheckpointRepository checkpointRepository) {
        super(application);
        this.repository = checkpointRepository;

        observableCheckpoint = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCheckpoint.setValue(null);

        LiveData<List<Checkpoint>> checkpoints = checkpointRepository.getcheckpointsByOrder(orderId);


        // observe the changes of the entities from the database and forward them
        observableCheckpoint.addSource(checkpoints, observableCheckpoint::setValue);
    }

    public LiveData<List<Checkpoint>> getAllCheckpoints(){
        return observableCheckpoint;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String orderId;
        private final CheckpointRepository checkpointRepository;

        public Factory(@NonNull Application application, String orderId) {
            this.application = application;
            this.orderId = orderId;
            checkpointRepository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointListViewModel(application, orderId, checkpointRepository);
        }
    }
}

