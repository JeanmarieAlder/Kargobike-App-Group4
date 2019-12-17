package deploy.example.kargobikeappg4.viewmodel.zone;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.db.repository.ZoneRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class ZoneListViewModel extends AndroidViewModel {

    ZoneRepository repository;

    private final MediatorLiveData<List<Zone>> observableZone;

    public ZoneListViewModel(@NonNull Application application,
                              ZoneRepository zoneRepository) {
        super(application);
        this.repository = zoneRepository;

        observableZone = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableZone.setValue(null);

        LiveData<List<Zone>> zones = zoneRepository.getAllZones();


        // observe the changes of the entities from the database and forward them
        observableZone.addSource(zones, observableZone::setValue);
    }

    public LiveData<List<Zone>> getAllZones(){
        return observableZone;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ZoneRepository zoneRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            zoneRepository = ((BaseApp) application).getZoneRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ZoneListViewModel(application, zoneRepository);
        }
    }
}
