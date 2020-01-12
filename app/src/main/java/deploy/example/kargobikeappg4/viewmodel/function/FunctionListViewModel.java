package deploy.example.kargobikeappg4.viewmodel.function;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import deploy.example.kargobikeappg4.db.entities.Function;
import deploy.example.kargobikeappg4.db.repository.FunctionRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class FunctionListViewModel extends AndroidViewModel {

    //Attributes
    private FunctionRepository repository;
    private final MediatorLiveData<List<Function>> observableType;

    //Constructor and initialize all values
    public FunctionListViewModel(@NonNull Application application,
                                 FunctionRepository functionRepository) {
        super(application);
        this.repository = functionRepository;

        observableType = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableType.setValue(null);

        //give all functions back
        LiveData<List<Function>> functions = functionRepository.getAllFunctions();

        // observe the changes of the entities from the database and forward them
        observableType.addSource(functions, observableType::setValue);
    }

    //Give all functions back
    public LiveData<List<Function>> getAllFunctions() {
        return observableType;
    }

    /**
     * A creator is used that afterward all functions can be returned
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final FunctionRepository functionRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            functionRepository = ((BaseApp) application).getFunctionRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FunctionListViewModel(application, functionRepository);
        }
    }
}
