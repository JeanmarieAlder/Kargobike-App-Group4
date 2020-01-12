package deploy.example.kargobikeappg4.viewmodel.checkpoint;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Checkpoint;
import deploy.example.kargobikeappg4.db.repository.CheckpointRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckpointViewModel extends AndroidViewModel {

    private CheckpointRepository repository;
    private final MediatorLiveData<Checkpoint> observableOrder;

    //Constructor with all and initialize all values
    public CheckpointViewModel(@NonNull Application application, final String orderId,
                               final String checkpointId, CheckpointRepository repository) {
        super(application);

        this.repository = repository;

        observableOrder = new MediatorLiveData<>();
        observableOrder.setValue(null); //Null by default until we get data from DB
        if (checkpointId != null) {
            LiveData<Checkpoint> checkpoint = repository.getCheckpoint(orderId, checkpointId);

            //observer changes from db and forward them
            observableOrder.addSource(checkpoint, observableOrder::setValue);
        }

    }

    /**
     * A creator is used to display the information of a checkpoint
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String orderId;
        private final String id;

        private final CheckpointRepository repository;

        public Factory(@NonNull Application application, String orderId, String checkpointId) {
            this.application = application;
            this.orderId = orderId;
            this.id = checkpointId;
            repository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointViewModel(application, orderId, id, repository);
        }
    }

    /**
     * Expose the LiveData CheckpointEntity query so the UI can observe it.
     */
    public LiveData<Checkpoint> getCheckpoint() {
        return observableOrder;
    }

    //Following all queries
    public void createCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.insert(checkpoint, callback);
    }

    public void updateCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.update(checkpoint, callback);
    }

    public void deleteCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.delete(checkpoint, callback);
    }

}
