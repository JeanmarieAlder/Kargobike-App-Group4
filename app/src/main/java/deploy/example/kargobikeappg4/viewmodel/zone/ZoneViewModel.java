package deploy.example.kargobikeappg4.viewmodel.zone;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import deploy.example.kargobikeappg4.db.entities.Zone;
import deploy.example.kargobikeappg4.db.repository.ZoneRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class ZoneViewModel extends AndroidViewModel {

    private ZoneRepository repository;
    private MediatorLiveData<Zone> observableZone;

    public ZoneViewModel(@NonNull Application application,
                          final String zoneId, ZoneRepository repository) {
        super(application);

        this.repository = repository;

        observableZone = new MediatorLiveData<>();
        observableZone.setValue(null); //Null by default until we get data from DB
        if(zoneId != null)
        {
            LiveData<Zone> zone = repository.getZone(zoneId);

            //observer changes from db and forward them
            observableZone.addSource(zone, observableZone::setValue);
        }

    }


    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String zoneId;

        private final ZoneRepository repository;

        public Factory(@NonNull Application application, String zoneId) {
            this.application = application;
            this.zoneId = zoneId;
            repository = ((BaseApp) application).getZoneRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ZoneViewModel(application, zoneId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<Zone> getZone() {
        return observableZone;
    }

    public void createZone(Zone zone, OnAsyncEventListener callback) {
        repository.insert(zone, callback);
    }

    public void updateZone(Zone zone, OnAsyncEventListener callback) {
        repository.update(zone, callback);
    }

    public void deleteZone(Zone zone, OnAsyncEventListener callback) {
        repository.delete(zone, callback);
    }
}
