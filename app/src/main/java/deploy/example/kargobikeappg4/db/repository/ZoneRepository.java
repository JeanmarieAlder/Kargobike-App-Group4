package deploy.example.kargobikeappg4.db.repository;

import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.liveData.ZoneLiveData;

import deploy.example.kargobikeappg4.db.entities.Zone;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ZoneRepository {

    private static final String TAG = "ZoneRepository";

    private static ZoneRepository instance;

    public ZoneRepository() {
    }

    //Constructor
    public static ZoneRepository getInstance() {
        if (instance == null) {
            synchronized (ZoneRepository.class) {
                if (instance == null) {
                    instance = new ZoneRepository();
                }
            }
        }
        return instance;
    }

    //Query: get one rider by id
    public LiveData<Zone> getZone(final String idZone){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Zone")
                .child(idZone);
        return new ZoneLiveData(reference);
    }
}
