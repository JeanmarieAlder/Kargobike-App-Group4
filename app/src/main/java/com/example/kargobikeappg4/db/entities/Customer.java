package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Customer implements Comparable{

    //Attributes
    private String idCustomer;
    private String titre;
    private String firstname;
    private String lastname;
    private String idAddress;
    private String idProduct;

    //Constructor
    public Customer(){

    }

    public Customer(@NonNull String idCustomer, String titre, String firstname, String lastname, String idAddress, String idProduct) {
        this.idCustomer = idCustomer;
        this.titre = titre;
        this.firstname = firstname;
        this.lastname = lastname;
        this.idAddress = idAddress;
        this.idProduct = idProduct;
    }

    //Getter and Setter
    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
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
        if (!(obj instanceof Customer)) return false;
        Customer c = (Customer) obj;
        return c.getIdCustomer().equals(this.getIdCustomer());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("titre", titre);
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        return result;
    }

    @Override
    public String toString() {
        return titre + " " + firstname + " " + lastname;
    }

}
