package deploy.example.kargobikeappg4.viewmodel.order;

import android.app.Application;

import deploy.example.kargobikeappg4.db.entities.Order;
import deploy.example.kargobikeappg4.db.repository.OrderRepository;
import deploy.example.kargobikeappg4.viewmodel.BaseApp;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderListViewModel extends AndroidViewModel {

    //Atributes
    private OrderRepository repository;
    private final MediatorLiveData<List<Order>> observableOrder;

    //Constructor and initialize all values
    public OrderListViewModel(@NonNull Application application,
                              OrderRepository orderRepository) {
        super(application);
        this.repository = orderRepository;

        observableOrder = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOrder.setValue(null);

        //Give all orders back
        LiveData<List<Order>> orders = orderRepository.getAllOrders();


        // observe the changes of the entities from the database and forward them
        observableOrder.addSource(orders, observableOrder::setValue);
    }

    //Gives all orders back
    public LiveData<List<Order>> getAllOrders() {
        return observableOrder;
    }

    /**
     * A creator is used that afterward all orders can be returned
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final OrderRepository orderRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            orderRepository = ((BaseApp) application).getOrderRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new OrderListViewModel(application, orderRepository);
        }
    }
}
