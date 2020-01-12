package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Rider implements Comparable {

    //Attributes
    private String idRider;
    private String location;
    private String idZone;
    private String idUser;

    //Constructor
    public Rider() {

    }

    public Rider(String idRider, String location, String idZone, String idUser) {
        this.idRider = idRider;
        this.location = location;
        this.idZone = idZone;
        this.idUser = idUser;
    }

    public String getIdRider() {
        return idRider;
    }

    public void setIdRider(String idRider) {
        this.idRider = idRider;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Customer)) return false;
        Rider r = (Rider) obj;
        return r.getIdRider().equals(this.getIdRider());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("location", location);
        result.put("idZone", idZone);
        result.put("idUser", idUser);
        return result;
    }

    @Override
    public String toString() {
        return location + " " + idZone;
    }
}
