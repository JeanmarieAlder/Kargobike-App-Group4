package com.example.kargobikeappg4.viewmodel.product;

import android.app.Application;

import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.db.repository.ProductRepository;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private final MediatorLiveData<Product> observableOrder;

    public ProductViewModel(@NonNull Application application,
                          final String productId, ProductRepository repository) {
        super(application);

        this.repository = repository;

        observableOrder = new MediatorLiveData<>();
        observableOrder.setValue(null); //Null by default until we get data from DB
        if(productId != null)
        {
            LiveData<Product> product = repository.getProduct(productId);

            //observer changes from db and forward them
            observableOrder.addSource(product, observableOrder::setValue);
        }

    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String productId;

        private final ProductRepository repository;

        public Factory(@NonNull Application application, String actId) {
            this.application = application;
            this.productId = actId;
            repository = ((BaseApp) application).getProductRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(application, productId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<Product> getProduct() {
        return observableOrder;
    }

    public void createProduct(Product product, OnAsyncEventListener callback) {
        repository.insert(product, callback);
    }

    public void updateProduct(Product product, OnAsyncEventListener callback) {
        repository.update(product, callback);
    }

    public void deleteProduct(Product product, OnAsyncEventListener callback) {
        repository.delete(product, callback);
    }

}
