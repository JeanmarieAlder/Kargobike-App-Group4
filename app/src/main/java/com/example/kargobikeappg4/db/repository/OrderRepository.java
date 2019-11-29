package com.example.kargobikeappg4.db.repository;

import com.example.kargobikeappg4.db.entities.Order;
import com.example.kargobikeappg4.db.liveData.OrderListLiveData;
import com.example.kargobikeappg4.db.liveData.OrderLiveData;
import com.example.kargobikeappg4.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OrderRepository {
    private static final String TAG = "OrderRepository";

    private static OrderRepository instance;

    public OrderRepository() {
    }

    //Constructor
    public static OrderRepository getInstance() {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository();
                }
            }
        }
        return instance;
    }

    //Query: get a order
    /*public LiveData<Order> getOrder(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(id);
        return new OrderLiveData(reference);
    }*/

    //Query: get all orders
    public LiveData<List<Order>> getAllOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Order");
        return new OrderListLiveData(reference);
    }

    //Query: get one order by id
    public LiveData<Order> getOrder(final String idOrder){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(idOrder);
        return new OrderLiveData(reference);
    }

    //Query: insert a order
    public void insert(final Order order, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("Order").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(id)
                .setValue(order, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a order
    public void update(final Order order, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("Order")
                .child(order.getIdOrder())
                .updateChildren(order.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
