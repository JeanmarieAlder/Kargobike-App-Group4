package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Address implements Comparable{

    //Attributes
    private String idAddress;
    private String city;
    private String postcode;
    private String street;
    private String idZone;

    //Constructor
    public Address(){

    }

    public Address(String idAddress, String city, String postcode, String street, String idZone) {
        this.idAddress = idAddress;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.idZone = idZone;
    }

    //Getter and Setter
    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Customer)) return false;
        Address a = (Address) obj;
        return a.getIdAddress().equals(this.getIdAddress());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("city", city);
        result.put("postcode", postcode);
        result.put("street", street);
        result.put("idZone", idZone);
        return result;
    }

    @Override
    public String toString() {
        return city + " " + postcode + " " + street;
    }

}
