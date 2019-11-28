package com.example.kargobikeappg4.viewmodel;

import android.app.Application;

import com.example.kargobikeappg4.db.repository.OrderRepository;
import com.example.kargobikeappg4.db.repository.RiderRepository;
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

    public RiderRepository getRiderRepository() {
        return RiderRepository.getInstance();
    }

}
