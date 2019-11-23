package com.example.kargobikeappg4.db.entities;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Order implements Comparable {

    //Attributes
    private String idOrder;
    private String name;
    private float weight;
    private String dateOrder;
    private String dateDelivery;
    private Bitmap signature;
    private Bitmap deliveryPicture;
    private String idCustomer;
    private String idProduct;

    //Constructor
    public Order(){

    }

    public Order(@NonNull String idOrder, String name, float weight, String dateOrder, String dateDelivery, Bitmap signature, Bitmap deliveryPicture, String idCustomer, String idProduct) {
        this.idOrder = idOrder;
        this.name = name;
        this.weight = weight;
        this.dateOrder = dateOrder;
        this.dateDelivery = dateDelivery;
        this.signature = signature;
        this.deliveryPicture = deliveryPicture;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
    }

    //Getter and Setter
    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public Bitmap getSignature() {
        return signature;
    }

    public void setSignature(Bitmap signature) {
        this.signature = signature;
    }

    public Bitmap getDeliveryPicture() {
        return deliveryPicture;
    }

    public void setDeliveryPicture(Bitmap deliveryPicture) {
        this.deliveryPicture = deliveryPicture;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Order o = (Order) obj;
        return o.getIdOrder().equals(this.getIdOrder());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("weight", weight);
        result.put("dateOrder", dateOrder);
        result.put("dateDelivery", dateDelivery);
        result.put("signature", signature);
        result.put("deliveryPicture", deliveryPicture);
        result.put("idCustomer", idCustomer);
        result.put("idProduct", idProduct);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
