package deploy.example.kargobikeappg4.viewmodel.customer;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.db.repository.CustomerRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomerViewModel extends AndroidViewModel {

    //Atributes
    private CustomerRepository repository;
    private final MediatorLiveData<Customer> observableCustomer;


    //Constructor and initialize all values
    public CustomerViewModel(@NonNull Application application, String customerId,
                             CustomerRepository repository) {
        super(application);

        this.repository = repository;

        observableCustomer = new MediatorLiveData<>();
        observableCustomer.setValue(null); //Null by default until we get data from DB
        if(customerId != null)
        {
            //Give a customer back
            LiveData<Customer> customer = repository.getCustomer(customerId);

            //observer changes from db and forward them
            observableCustomer.addSource(customer, observableCustomer::setValue);
        }

    }

    /**
     * A creator is used to inject the customer id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String customerId;

        private final CustomerRepository repository;

        public Factory(@NonNull Application application, String customerId) {
            this.application = application;
            this.customerId = customerId;
            repository = ((BaseApp) application).getCustomerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CustomerViewModel(application, customerId, repository);
        }
    }

    //Method to give one customer back
    public LiveData<Customer> getCustomer() {
        return observableCustomer;
    }


}
