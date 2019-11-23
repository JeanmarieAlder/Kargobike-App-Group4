package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Zone implements  Comparable{

    //Attributes
    private String idZone;
    private String name;
    private String idMainRider;

    //Constructor
    public Zone(){

    }

    public Zone(@NonNull String idZone, String name, String idMainRider) {
        this.idZone = idZone;
        this.name = name;
        this.idMainRider = idMainRider;
    }

    //Getter and Setter
    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdMainRider() {
        return idMainRider;
    }

    public void setIdMainRider(String idMainRider) {
        this.idMainRider = idMainRider;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Zone)) return false;
        Zone z = (Zone) obj;
        return z.getIdZone().equals(this.getIdZone());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("idMainRider", idMainRider);
        return result;
    }

    @Override
    public String toString() {
        return name + " " + idMainRider;
    }


}
