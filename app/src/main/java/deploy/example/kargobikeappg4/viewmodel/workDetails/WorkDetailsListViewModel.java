package deploy.example.kargobikeappg4.viewmodel.workDetails;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.WorkDetails;
import deploy.example.kargobikeappg4.db.repository.WorkDetailsRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WorkDetailsListViewModel extends AndroidViewModel {
    private WorkDetailsRepository repository;

    private final MediatorLiveData<List<WorkDetails>> observableWorkDetails;

    public WorkDetailsListViewModel(@NonNull Application application, final String userId,
                                    WorkDetailsRepository workDetailsRepository) {
        super(application);
        repository = workDetailsRepository;

        observableWorkDetails = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableWorkDetails.setValue(null);

        LiveData<List<WorkDetails>> workdetails = repository.getAllWorkDetailsFromUser(userId);


        // observe the changes of the entities from the database and forward them
        observableWorkDetails.addSource(workdetails, observableWorkDetails::setValue);
    }

    public LiveData<List<WorkDetails>> getAllWorkDetails(){
        return observableWorkDetails;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String userId;
        private final WorkDetailsRepository workDetailsRepository;

        public Factory(@NonNull Application application, String userId) {
            this.application = application;
            this.userId = userId;
            workDetailsRepository = ((BaseApp) application).getWorkDetailsRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WorkDetailsListViewModel(application, userId, workDetailsRepository);
        }
    }
}

