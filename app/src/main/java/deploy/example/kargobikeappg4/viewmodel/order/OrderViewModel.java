package deploy.example.kargobikeappg4.viewmodel.order;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Order;
import deploy.example.kargobikeappg4.db.repository.OrderRepository;
import deploy.example.kargobikeappg4.util.OnAsyncEventListener;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderViewModel extends AndroidViewModel {

    //Attributes
    private OrderRepository repository;
    private final MediatorLiveData<Order> observableOrder;

    //Constructor and initialize all values
    public OrderViewModel(@NonNull Application application,
                          final String orderId, OrderRepository repository) {
        super(application);

        this.repository = repository;

        observableOrder = new MediatorLiveData<>();
        observableOrder.setValue(null); //Null by default until we get data from DB
        if(orderId != null)
        {
            //give a order back
            LiveData<Order> order = repository.getOrder(orderId);

            //observer changes from db and forward them
            observableOrder.addSource(order, observableOrder::setValue);
        }

    }

    /**
     * A creator is used to inject the order id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String orderId;

        private final OrderRepository repository;

        public Factory(@NonNull Application application, String actId) {
            this.application = application;
            this.orderId = actId;
            repository = ((BaseApp) application).getOrderRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new OrderViewModel(application, orderId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */

    //Give one order back
    public LiveData<Order> getOrder() {
        return observableOrder;
    }


    //all other queries
    public void createOrder(Order order, OnAsyncEventListener callback) {
        repository.insert(order, callback);
    }

    public void updateOrder(Order order, OnAsyncEventListener callback) {
        repository.update(order, callback);
    }

    public void deleteOrder(Order order, OnAsyncEventListener callback) {
        repository.delete(order, callback);
    }

}
