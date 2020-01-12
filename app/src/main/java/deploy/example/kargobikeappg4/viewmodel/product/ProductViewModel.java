package deploy.example.kargobikeappg4.viewmodel.product;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Product;
import deploy.example.kargobikeappg4.db.repository.ProductRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductViewModel extends AndroidViewModel {

    //Atributes
    private ProductRepository repository;
    private final MediatorLiveData<Product> observableOrder;

    //Constructor and initialize all values
    public ProductViewModel(@NonNull Application application,
                            final String productId, ProductRepository repository) {
        super(application);

        this.repository = repository;

        observableOrder = new MediatorLiveData<>();
        observableOrder.setValue(null); //Null by default until we get data from DB
        if (productId != null) {
            //gives one product back
            LiveData<Product> product = repository.getProduct(productId);

            //observer changes from db and forward them
            observableOrder.addSource(product, observableOrder::setValue);
        }

    }

    /**
     * A creator is used to inject the product id into the ViewModel
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
     * Expose the LiveData ProductEntity query so the UI can observe it.
     */

    //give one product back
    public LiveData<Product> getProduct() {
        return observableOrder;
    }


    //All other queries
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
