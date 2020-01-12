package deploy.example.kargobikeappg4.viewmodel.customer;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Customer;
import deploy.example.kargobikeappg4.db.repository.CustomerRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomerListViewModel extends AndroidViewModel {
    private CustomerRepository repository;

    private final MediatorLiveData<List<Customer>> observableCustomer;

    //Constructor and initialize all values
    public CustomerListViewModel(@NonNull Application application,
                                 CustomerRepository customerRepository) {
        super(application);

        this.repository = customerRepository;

        observableCustomer = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCustomer.setValue(null);

        //Give all customers back
        LiveData<List<Customer>> customers = customerRepository.getAllCustomers();


        // observe the changes of the entities from the database and forward them
        observableCustomer.addSource(customers, observableCustomer::setValue);
    }

    //Gives all customers back
    public LiveData<List<Customer>> getAllCustomers() {
        return observableCustomer;
    }

    /**
     * A creator is used that all customers can afterwards be displayed
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final CustomerRepository customerRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            customerRepository = ((BaseApp) application).getCustomerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CustomerListViewModel(application, customerRepository);
        }

    }
}
