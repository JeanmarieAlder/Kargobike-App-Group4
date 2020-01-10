package deploy.example.kargobikeappg4.viewmodel.trainstation;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import deploy.example.kargobikeappg4.db.entities.TrainStation;
import deploy.example.kargobikeappg4.db.repository.TrainStationRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class TrainStationListViewModel extends AndroidViewModel {

    private TrainStationRepository repository;
    private final MediatorLiveData<List<TrainStation>> observableTrainStation;

    public TrainStationListViewModel(@NonNull Application application,
                             TrainStationRepository trainStationRepository) {
        super(application);
        this.repository = trainStationRepository;

        observableTrainStation = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableTrainStation.setValue(null);

        LiveData<List<TrainStation>> trainStations = trainStationRepository.getAllTrainStations();

        // observe the changes of the entities from the database and forward them
        observableTrainStation.addSource(trainStations, observableTrainStation::setValue);
    }

    public LiveData<List<TrainStation>> getAllTrainStations() { return observableTrainStation; }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final TrainStationRepository trainStationRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            trainStationRepository = ((BaseApp) application).getTrainStationRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TrainStationListViewModel(application, trainStationRepository);
        }
    }
}
