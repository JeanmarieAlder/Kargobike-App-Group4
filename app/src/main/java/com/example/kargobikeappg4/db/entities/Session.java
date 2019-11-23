package com.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Session implements Comparable{

    //Attributes
    private String idSession;
    private String timeStart;
    private String timeEnd;
    private float duration;
    private String idUser;

    //Constructor
    public Session(){

    }

    public Session(String idSession, String timeStart, String timeEnd, float duration, String idUser) {
        this.idSession = idSession;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.duration = duration;
        this.idUser = idUser;
    }

    //Getter and Setter
    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
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
        Session s = (Session) obj;
        return s.getIdSession().equals(this.getIdSession());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("timeStart", timeStart);
        result.put("timeStart", timeStart);
        result.put("duration", duration);
        result.put("idUser", idUser);
        return result;
    }

    @Override
    public String toString() {
        return timeStart + " " + timeStart + " " + idUser;
    }
}
