package com.example.kargobikeappg4.viewmodel.product;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.db.repository.ProductRepository;
import com.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductListViewModel extends AndroidViewModel {

    private ProductRepository repository;

    private final MediatorLiveData<List<Product>> observableProduct;

    public ProductListViewModel(@NonNull Application application,
                                ProductRepository productRepository) {
        super(application);
        this.repository = productRepository;

        observableProduct= new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProduct.setValue(null);

        LiveData<List<Product>> products = productRepository.getAllProducts();


        // observe the changes of the entities from the database and forward them
        observableProduct.addSource(products, observableProduct::setValue);
    }

    public LiveData<List<Product>> getAllProducts(){
        return observableProduct;
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ProductRepository productRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            productRepository = ((BaseApp) application).getProductRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductListViewModel(application, productRepository);
        }
    }
}

