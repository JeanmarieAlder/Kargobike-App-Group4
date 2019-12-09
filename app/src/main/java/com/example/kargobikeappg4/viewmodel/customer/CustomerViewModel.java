package com.example.kargobikeappg4.viewmodel.customer;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Customer;
import com.example.kargobikeappg4.db.repository.CustomerRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomerViewModel extends AndroidViewModel {
    private CustomerRepository repository;
    private final MediatorLiveData<Customer> observableCustomer;

    public CustomerViewModel(@NonNull Application application, String customerId,
                             CustomerRepository repository) {
        super(application);

        this.repository = repository;

        observableCustomer = new MediatorLiveData<>();
        observableCustomer.setValue(null); //Null by default until we get data from DB
        if(customerId != null)
        {
            LiveData<Customer> customer = repository.getCustomer(customerId);

            //observer changes from db and forward them
            observableCustomer.addSource(customer, observableCustomer::setValue);
        }

    }

    /**
     * A creator is used to inject the account id into the ViewModel
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

    public LiveData<Customer> getCustomer() {
        return observableCustomer;
    }


}
