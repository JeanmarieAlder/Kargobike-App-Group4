package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Function implements Comparable {

    //Attributes
    private String idFunction;
    private String name;
    private String description;

    //Constructor
    public Function(){

    }

    public Function(@NonNull String idFunction, String name, String description) {
        this.idFunction = idFunction;
        this.name = name;
        this.description = description;
    }

    //Getter and Setter
    public String getIdFunction() {
        return idFunction;
    }

    public void setIdFunction(String idFunction) {
        this.idFunction = idFunction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Customer)) return false;
        Function f = (Function) obj;
        return f.getIdFunction().equals(this.getIdFunction());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        return result;
    }

    @Override
    public String toString() {
        return name + " " + description;
    }

}
