package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

public class Checkpoint implements Comparable{

    //Attributes
    private String idCheckpoint;
    private String idAddress;
    private String idZone;
    private String idType;
    private float lat;
    private float lng;

    public Checkpoint() {}

    public Checkpoint(String idCheckpoint, String idAddress, String idZone, String idType, float lat, float lng) {
        this.idCheckpoint = idCheckpoint;
        this.idAddress = idAddress;
        this.idZone = idZone;
        this.idType = idType;
        this.lat = lat;
        this.lng = lng;
    }

    public String getIdCheckpoint() {
        return idCheckpoint;
    }

    public void setIdCheckpoint(String idCheckpoint) {
        this.idCheckpoint = idCheckpoint;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idAddress", idAddress);
        result.put("idZone", idZone);
        result.put("idType", idType);
        result.put("lat", lat);
        result.put("lng", lng);
        return result;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "idCheckpoint='" + idCheckpoint + '\'' +
                '}';
    }
}
