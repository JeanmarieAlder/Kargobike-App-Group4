package deploy.example.kargobikeappg4.viewmodel.type;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Type;
import deploy.example.kargobikeappg4.db.repository.TypeRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TypeListViewModel extends AndroidViewModel {

    //Attributes
    private TypeRepository repository;
    private final MediatorLiveData<List<Type>> observableType;

    //Constructor and initialize all values
    public TypeListViewModel(@NonNull Application application,
                             TypeRepository typeRepository) {
        super(application);
        this.repository = typeRepository;

        observableType = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableType.setValue(null);

        //give all types back
        LiveData<List<Type>> types = typeRepository.getAllTypes();

        // observe the changes of the entities from the database and forward them
        observableType.addSource(types, observableType::setValue);
    }

    //give all types back
    public LiveData<List<Type>> getAllTypes() { return observableType; }

    /**
     * A creator is used that afterward all types can be returned
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final TypeRepository typeRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            typeRepository = ((BaseApp) application).getTypeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TypeListViewModel(application, typeRepository);
        }
    }
}
