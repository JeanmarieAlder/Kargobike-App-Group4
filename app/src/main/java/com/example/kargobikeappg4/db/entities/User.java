package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class User implements Comparable{

    //Attributes
    private String idUser;
    private String name;
    private String language;
    private String workingsince;
    private String email;
    private String phoneNumber;
    private String idFunction;
    private String idAddress;

    //Constructor
    public User(){

    }

    public User(@NonNull String idUser, String name, String language, String workingsince, String email, String phoneNumber, String idFunction, String idAddress) {
        this.idUser = idUser;
        this.name = name;
        this.language = language;
        this.workingsince = workingsince;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.idFunction = idFunction;
        this.idAddress = idAddress;
    }

    //Getter and Setter
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWorkingsince() {
        return workingsince;
    }

    public void setWorkingsince(String workingsince) {
        this.workingsince = workingsince;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdFunction() {
        return idFunction;
    }

    public void setIdFunction(String idFunction) {
        this.idFunction = idFunction;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        User o = (User) obj;
        return o.getIdUser().equals(this.getIdUser());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("language", language);
        result.put("workingsince", workingsince);
        result.put("workingsince", email);
        result.put("phoneNumber", phoneNumber);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
