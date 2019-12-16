package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Product implements  Comparable {

    //Attributes
    private String idProduct;
    private String name;
    private String description;
    private float price;
    private float duration;
    private boolean interurbain;

    //Constructor
    public Product(){

    }

    public Product(String idProduct, String name, String description, float price, float duration, boolean interurbain) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.interurbain = interurbain;
    }

    //Getter and Setter
    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isInterurbain() {
        return interurbain;
    }

    public void setInterurbain(boolean interurbain) {
        this.interurbain = interurbain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Customer)) return false;
        Product p = (Product) obj;
        return p.getIdProduct().equals(this.getIdProduct());
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
        result.put("price", price);
        result.put("duration", duration);
        result.put("interurbain", interurbain);
        return result;
    }

    @Override
    public String toString() {
        return name + " " + price + " " + interurbain;
    }

}
