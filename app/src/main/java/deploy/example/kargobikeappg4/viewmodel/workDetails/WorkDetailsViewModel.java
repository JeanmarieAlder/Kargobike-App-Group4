package deploy.example.kargobikeappg4.viewmodel.workDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import deploy.example.kargobikeappg4.db.entities.WorkDetails;
import deploy.example.kargobikeappg4.db.repository.WorkDetailsRepository;

public class WorkDetailsViewModel extends AndroidViewModel {

    //Attributes
    private WorkDetailsRepository repository;
    private final MediatorLiveData<WorkDetails> observableWorkDetails;


    //Constructor and initialize all values
    public WorkDetailsViewModel(@NonNull Application application, final String userId,
                                final String workDetailsId, WorkDetailsRepository repository) {
        super(application);

        this.repository = repository;

        observableWorkDetails = new MediatorLiveData<>();
        observableWorkDetails.setValue(null); //Null by default until we get data from DB
        if (workDetailsId != null) {
            //give the worketails of one user
            LiveData<WorkDetails> workDetails = repository.getOneWorkDetails(userId, workDetailsId);

            //observer changes from db and forward them
            observableWorkDetails.addSource(workDetails, observableWorkDetails::setValue);
        }

    }

    /**
     * A creator is used to inject the user id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String userId;
        private final String id;
        private final WorkDetailsRepository repository;

        public Factory(@NonNull Application application, String userId, String workDetailsId) {
            this.application = application;
            this.userId = userId;
            this.id = workDetailsId;
            repository = ((BaseApp) application).getWorkDetailsRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WorkDetailsViewModel(application, userId, id, repository);
        }
    }

    /**
     * Expose the LiveData WorkdetailsEntity query so the UI can observe it.
     */
    public LiveData<WorkDetails> getWorkDetails() {
        return observableWorkDetails;
    }

    //insert a new workdetail of a user
    public void createWorkDetails(WorkDetails workDetails, String idUser, OnAsyncEventListener callback) {
        repository.insert(workDetails, idUser, callback);
    }
}
