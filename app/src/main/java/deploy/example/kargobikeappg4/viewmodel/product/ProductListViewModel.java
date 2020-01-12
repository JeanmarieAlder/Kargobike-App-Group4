package deploy.example.kargobikeappg4.viewmodel.product;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Product;
import deploy.example.kargobikeappg4.db.repository.ProductRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductListViewModel extends AndroidViewModel {

    //Attributes
    private ProductRepository repository;
    private final MediatorLiveData<List<Product>> observableProduct;

    //Constructor and initialize all values
    public ProductListViewModel(@NonNull Application application,
                                ProductRepository productRepository) {
        super(application);
        this.repository = productRepository;

        observableProduct= new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProduct.setValue(null);

        //gives all products back
        LiveData<List<Product>> products = productRepository.getAllProducts();


        // observe the changes of the entities from the database and forward them
        observableProduct.addSource(products, observableProduct::setValue);
    }

    //gives all products back
    public LiveData<List<Product>> getAllProducts(){
        return observableProduct;
    }

    /**
     * A creator is used that afterward all products can be returned
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

