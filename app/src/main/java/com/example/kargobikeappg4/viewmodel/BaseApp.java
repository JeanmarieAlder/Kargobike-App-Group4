package com.example.kargobikeappg4.viewmodel;

import android.app.Application;

import com.example.kargobikeappg4.db.repository.OrderRepository;
import com.example.kargobikeappg4.db.repository.ProductRepository;
import com.example.kargobikeappg4.db.repository.RiderRepository;
import com.example.kargobikeappg4.db.repository.TransportRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Base application, used to retreive an instance of the database,
 * Act and Stage table.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("Act_Added");

    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    public OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
    }

    public ProductRepository getProductRepository() {
        return ProductRepository.getInstance();
    }

    public RiderRepository getRiderRepository() {
        return RiderRepository.getInstance();
    }

    public TransportRepository getTransportRepository() {
        return TransportRepository.getInstance();
    }

    public CheckpointRepository getCheckpointRepository() {
        return CheckpointRepository.getInstance();
    }
    public ZoneRepository getZoneRepository() {
        return ZoneRepository.getInstance();
    }

}
