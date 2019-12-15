package com.example.kargobikeappg4.db.repository;

import com.example.kargobikeappg4.db.entities.Type;
import com.example.kargobikeappg4.db.liveData.TypeListLiveData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TypeRepository {

    private static final String TAG = "TypeRepository";

    private static TypeRepository instance;

    public TypeRepository(){}

    //Constructor
    public static TypeRepository getInstance() {
        if (instance == null) {
            synchronized (TypeRepository.class) {
                if (instance == null) {
                    instance = new TypeRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all orders
    public LiveData<List<Type>> getAllTypes() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Type");
        return new TypeListLiveData(reference);
    }
}
