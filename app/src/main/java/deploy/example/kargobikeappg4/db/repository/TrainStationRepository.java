package deploy.example.kargobikeappg4.db.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.entities.TrainStation;
import deploy.example.kargobikeappg4.db.liveData.TrainStationListLiveData;

public class TrainStationRepository {

    private static final String TAG = "TrainStationRepository";

    private static TrainStationRepository instance;

    //Constructor
    public static TrainStationRepository getInstance() {
        if (instance == null) {
            synchronized (TrainStationRepository.class) {
                if (instance == null) {
                    instance = new TrainStationRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all orders
    public LiveData<List<TrainStation>> getAllTrainStations() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("TrainStation");
        return new TrainStationListLiveData(reference);
    }
}
