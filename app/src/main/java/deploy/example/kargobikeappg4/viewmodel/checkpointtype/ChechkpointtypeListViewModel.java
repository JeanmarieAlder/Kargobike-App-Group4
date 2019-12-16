package deploy.example.kargobikeappg4.viewmodel.checkpointtype;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Checkpointtype;
import deploy.example.kargobikeappg4.db.repository.CheckpointtypeRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChechkpointtypeListViewModel extends AndroidViewModel {

    private CheckpointtypeRepository repository;

    private final MediatorLiveData<List<Checkpointtype>> observabletype;

    public ChechkpointtypeListViewModel(@NonNull Application application,
                                        CheckpointtypeRepository checkpointtypeRepository) {
        super(application);
        this.repository = checkpointtypeRepository;

        observabletype = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observabletype.setValue(null);

        LiveData<List<Checkpointtype>> types = checkpointtypeRepository.getAllTypes();


        // observe the changes of the entities from the database and forward them
        observabletype.addSource(types, observabletype::setValue);
    }

    public LiveData<List<Checkpointtype>> getAllTypes(){
        return observabletype;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final CheckpointtypeRepository checkpointtypeRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            checkpointtypeRepository = ((BaseApp) application).getCheckpointtypeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ChechkpointtypeListViewModel(application, checkpointtypeRepository);
        }
    }
}
