package deploy.example.kargobikeappg4.db.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import deploy.example.kargobikeappg4.db.entities.Function;
import deploy.example.kargobikeappg4.db.liveData.FunctionListLiveData;

public class FunctionRepository {

    private static final String TAG = "FunctionRepository";

    private static FunctionRepository instance;

    public FunctionRepository(){}

    //Constructor
    public static FunctionRepository getInstance() {
        if (instance == null) {
            synchronized (FunctionRepository.class) {
                if (instance == null) {
                    instance = new FunctionRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all orders
    public LiveData<List<Function>> getAllFunctions() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Function");
        return new FunctionListLiveData(reference);
    }
}

