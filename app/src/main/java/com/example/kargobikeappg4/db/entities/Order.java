package com.example.kargobikeappg4.db.entities;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Order implements Comparable {

    //Attributes
    private String idOrder;
    private float weight;
    private String dateOrder;
    private String dateDelivery;
    private String timeDelivery;
    private String signature;
    private String deliveryPicture;
    private String idCustomer;
    private String idProduct;
    private String idPickupCheckpoint;
    private String idDeliveryCheckpoint;

    //Constructor
    public Order(){

    }

    public Order(float weight, String dateOrder, String dateDelivery,
                 String timeDelivery, String signature,
                 String deliveryPicture, String idCustomer, String idProduct,
                 String idPickupCheckpoint, String idDeliveryCheckpoint) {
        this.weight = weight;
        this.dateOrder = dateOrder;
        this.dateDelivery = dateDelivery;
        this.timeDelivery = timeDelivery;
        this.signature = signature;
        this.deliveryPicture = deliveryPicture;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.idPickupCheckpoint = idPickupCheckpoint;
        this.idDeliveryCheckpoint = idDeliveryCheckpoint;
    }

    //Getter and Setter
    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
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

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDeliveryPicture() {
        return deliveryPicture;
    }

    public void setDeliveryPicture(String deliveryPicture) {
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

    public String getIdPickupCheckpoint() {
        return idPickupCheckpoint;
    }

    public void setIdPickupCheckpoint(String idPickupCheckpoint) {
        this.idPickupCheckpoint = idPickupCheckpoint;
    }

    public String getIdDeliveryCheckpoint() {
        return idDeliveryCheckpoint;
    }

    public void setIdDeliveryCheckpoint(String idDeliveryCheckpoint) {
        this.idDeliveryCheckpoint = idDeliveryCheckpoint;
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
        result.put("weight", weight);
        result.put("dateOrder", dateOrder);
        result.put("dateDelivery", dateDelivery);
        result.put("timeDelivery", timeDelivery);
        result.put("signature", signature);
        result.put("deliveryPicture", deliveryPicture);
        result.put("idCustomer", idCustomer);
        result.put("idProduct", idProduct);
        result.put("idPickupCheckpoint", idPickupCheckpoint);
        result.put("idDeliveryCheckpoint", idDeliveryCheckpoint);
        return result;
    }

    @Override
    public String toString() {
        return idOrder;
    }

}
