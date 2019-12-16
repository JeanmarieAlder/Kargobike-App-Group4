package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Transport implements Comparable {

    //Attributes
    private String idTransport;
    private String timeStart;
    private String timeEnd;
    private float duration;
    private String idPackage;
    private String idStartCheckpoint;
    private String idEndCheckpoint;
    private String idSession;

    //Constructor
    public Transport(){

    }

    public Transport(String idTransport, String timeStart, String timeEnd, float duration, String idPackage, String idStartCheckpoint, String idEndCheckpoint, String idSession) {
        this.idTransport = idTransport;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.duration = duration;
        this.idPackage = idPackage;
        this.idStartCheckpoint = idStartCheckpoint;
        this.idEndCheckpoint = idEndCheckpoint;
        this.idSession = idSession;
    }

    //Getter and Setter
    public String getIdTransport() {
        return idTransport;
    }

    public void setIdTransport(String idTransport) {
        this.idTransport = idTransport;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(String idPackage) {
        this.idPackage = idPackage;
    }

    public String getIdStartCheckpoint() {
        return idStartCheckpoint;
    }

    public void setIdStartCheckpoint(String idStartCheckpoint) {
        this.idStartCheckpoint = idStartCheckpoint;
    }

    public String getIdEndCheckpoint() {
        return idEndCheckpoint;
    }

    public void setIdEndCheckpoint(String idEndCheckpoint) {
        this.idEndCheckpoint = idEndCheckpoint;
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Order)) return false;
        Transport t = (Transport) obj;
        return t.getIdTransport().equals(this.getIdTransport());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("timeStart", timeStart);
        result.put("timeEnd", timeEnd);
        result.put("duration", duration);
        result.put("idPackage", idPackage);
        result.put("idStartCheckpoint", idStartCheckpoint);
        result.put("idEndCheckpoint", idEndCheckpoint);
        result.put("idSession", idSession);
        return result;
    }

    @Override
    public String toString() {
        return idStartCheckpoint + " " + idEndCheckpoint + " " + duration + " " + idPackage;
    }

}
