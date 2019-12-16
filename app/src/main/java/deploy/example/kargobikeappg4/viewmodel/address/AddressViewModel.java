package deploy.example.kargobikeappg4.viewmodel.address;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import deploy.example.kargobikeappg4.db.entities.Address;
import deploy.example.kargobikeappg4.db.repository.AddressRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

public class AddressViewModel extends AndroidViewModel {

    private AddressRepository repository;
    private final MediatorLiveData<Address> observableAddress;

    public AddressViewModel(@NonNull Application application,
                         final String addressId, AddressRepository repository) {
        super(application);

        this.repository = repository;

        observableAddress = new MediatorLiveData<>();
        observableAddress.setValue(null); //Null by default until we get data from DB
        if(addressId != null)
        {
            LiveData<Address> address = repository.getAddress(addressId);

            //observer changes from db and forward them
            observableAddress.addSource(address, observableAddress::setValue);
        }

    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String addressId;

        private final AddressRepository repository;

        public Factory(@NonNull Application application, String addressId) {
            this.application = application;
            this.addressId = addressId;
            repository = ((BaseApp) application).getAddressRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AddressViewModel(application, addressId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<Address> getAddress() {
        return observableAddress;
    }

    public void createAddress(Address address, OnAsyncEventListener callback, String id) {
        repository.insert(address, callback, id);
    }
}
