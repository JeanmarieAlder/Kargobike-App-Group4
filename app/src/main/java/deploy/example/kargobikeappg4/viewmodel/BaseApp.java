package deploy.example.kargobikeappg4.viewmodel;

import android.app.Application;

import deploy.example.kargobikeappg4.db.repository.CheckpointRepository;
import deploy.example.kargobikeappg4.db.repository.CheckpointtypeRepository;
import deploy.example.kargobikeappg4.db.repository.CustomerRepository;
import deploy.example.kargobikeappg4.db.repository.FunctionRepository;
import deploy.example.kargobikeappg4.db.repository.OrderRepository;
import deploy.example.kargobikeappg4.db.repository.ProductRepository;
import deploy.example.kargobikeappg4.db.repository.RiderRepository;
import deploy.example.kargobikeappg4.db.repository.TrainStationRepository;
import deploy.example.kargobikeappg4.db.repository.TransportRepository;
import deploy.example.kargobikeappg4.db.repository.TypeRepository;
import deploy.example.kargobikeappg4.db.repository.UserRepository;
import deploy.example.kargobikeappg4.db.repository.WorkDetailsRepository;
import deploy.example.kargobikeappg4.db.repository.ZoneRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Base application, used to retreive an instance of the database
 * and the repositories.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("Act_Added");

    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    //Initialize all Repos
    public OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
    }

    public ProductRepository getProductRepository() {
        return ProductRepository.getInstance();
    }

    public UserRepository getUserRepository() { return UserRepository.getInstance();}

    public TransportRepository getTransportRepository() {
        return TransportRepository.getInstance();
    }

    public CheckpointRepository getCheckpointRepository() {
        return CheckpointRepository.getInstance();
    }
    public CustomerRepository getCustomerRepository(){
        return CustomerRepository.getInstance();
    }

    public ZoneRepository getZoneRepository() {
        return ZoneRepository.getInstance();
    }

    public CheckpointtypeRepository getCheckpointtypeRepository() {
        return CheckpointtypeRepository.getInstance();
    }

    public TypeRepository getTypeRepository() { return TypeRepository.getInstance(); }

    public FunctionRepository getFunctionRepository() { return FunctionRepository.getInstance(); }

    public WorkDetailsRepository getWorkDetailsRepository() {
        return WorkDetailsRepository.getInstance();
    }
    public TrainStationRepository getTrainStationRepository() {
        return TrainStationRepository.getInstance();
    }
}
