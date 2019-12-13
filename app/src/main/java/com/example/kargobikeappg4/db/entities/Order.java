package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Order implements Comparable {

    //Attributes
    private String idOrder;
    private float quantity;
    private String dateOrder;
    private String dateDelivery;
    private String timeDelivery;
    private String pickupTimestamp;
    private String deliveryTimestamp;
    private String signature;
    private String deliveryPicture;
    private String idCustomer;
    private String idProduct;
    private String idPickupCheckpoint;
    private String idDeliveryCheckpoint;
    private String idResponsibleRider;
    private String status;

    //Constructor
    public Order(){

    }

    public Order(float quantity, String dateOrder, String dateDelivery,
                 String timeDelivery, String signature,
                 String deliveryPicture, String idCustomer, String idProduct,
                 String idPickupCheckpoint, String idDeliveryCheckpoint,
                 String idResponsibleRider, String status, String pickupTimestamp,
                 String deliveryTimestamp) {
        this.quantity = quantity;
        this.dateOrder = dateOrder;
        this.dateDelivery = dateDelivery;
        this.timeDelivery = timeDelivery;
        this.pickupTimestamp = pickupTimestamp;
        this.deliveryTimestamp = deliveryTimestamp;
        this.signature = signature;
        this.deliveryPicture = deliveryPicture;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.idPickupCheckpoint = idPickupCheckpoint;
        this.idDeliveryCheckpoint = idDeliveryCheckpoint;
        this.idResponsibleRider = idResponsibleRider;
        this.status = status;
    }

    //Getter and Setter
    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
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

    public String getIdResponsibleRider() {
        return idResponsibleRider;
    }

    public void setIdResponsibleRider(String idResponsibleRider) {
        this.idResponsibleRider = idResponsibleRider;
    }

    public String getStatus() {
        return status;
    }

    public String getPickupTimestamp() {
        return pickupTimestamp;
    }
    public void setPickupTimestamp(String pickupTimestamp) {
        this.pickupTimestamp = pickupTimestamp;
    }
    public String getDeliveryTimestamp() {
        return deliveryTimestamp;
    }
    public void setDeliveryTimestamp(String deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
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
        result.put("quantity", quantity);
        result.put("dateOrder", dateOrder);
        result.put("dateDelivery", dateDelivery);
        result.put("timeDelivery", timeDelivery);
        result.put("pickupTimestamp", pickupTimestamp);
        result.put("deliveryTimestamp", deliveryTimestamp);
        result.put("signature", signature);
        result.put("deliveryPicture", deliveryPicture);
        result.put("idCustomer", idCustomer);
        result.put("idProduct", idProduct);
        result.put("idPickupCheckpoint", idPickupCheckpoint);
        result.put("idDeliveryCheckpoint", idDeliveryCheckpoint);
        result.put("idResponsibleRider", idResponsibleRider);
        result.put("status", status);
        return result;
    }

    @Override
    public String toString() {
        return idOrder;
    }

}
