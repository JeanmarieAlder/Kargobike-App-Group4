package com.example.kargobikeappg4.viewmodel.customer;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Customer;
import com.example.kargobikeappg4.db.repository.CustomerRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;

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


    public CustomerListViewModel(@NonNull Application application,
                                 CustomerRepository customerRepository) {
        super(application);

        this.repository = customerRepository;

        observableCustomer = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCustomer.setValue(null);

        LiveData<List<Customer>> customers = customerRepository.getAllCustomers();


        // observe the changes of the entities from the database and forward them
        observableCustomer.addSource(customers, observableCustomer::setValue);
    }
    public LiveData<List<Customer>> getAllCustomers(){
        return observableCustomer;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
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
