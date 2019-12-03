package com.example.kargobikeappg4.db.repository;

import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.db.liveData.ProductListLiveData;
import com.example.kargobikeappg4.db.liveData.ProductLiveData;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ProductRepository {
    private static final String TAG = "ProductRepository";

    private static ProductRepository instance;

    public ProductRepository() {
    }

    //Constructor
    public static ProductRepository getInstance() {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }

    //Query: get all products
    public LiveData<List<Product>> getAllProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Product");
        return new ProductListLiveData(reference);
    }

    //Query: get one product by id
    public LiveData<Product> getProduct(final String idProduct){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Product")
                .child(idProduct);
        return new ProductLiveData(reference);
    }

    //Query: insert a order
    public void insert(final Product product, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Product").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Product")
                .child(id)
                .setValue(product, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a product
    public void update(final Product product, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(product.getIdProduct())
                .updateChildren(product.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Product product, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Product")
                .child(product.getIdProduct())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

}

