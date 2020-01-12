package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

public class Checkpoint implements Comparable {

    //Attributes
    private String idCheckpoint;
    private String idOrder;
    private String type;
    private float lat;
    private float lng;
    private String arrivalTimestamp;
    private String departureTimestamp;
    private String responsibleRider;
    private String arrivalCity; //for train stations
    private String arrivalTime; //for train stations
    private String newResponsibleRider; //for train stations
    private String remark;


    //Constructors
    public Checkpoint() {
    }

    public Checkpoint(String type, float lat, float lng, String arrivalTimestamp,
                      String departureTimestamp, String responsibleRider,
                      String arrivalCity, String arrivalTime, String newResponsibleRider,
                      String remark, String idOrder) {
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.arrivalTimestamp = arrivalTimestamp;
        this.departureTimestamp = departureTimestamp;
        this.responsibleRider = responsibleRider;
        this.arrivalCity = arrivalCity;
        this.arrivalTime = arrivalTime;
        this.newResponsibleRider = newResponsibleRider;
        this.remark = remark;
        this.idOrder = idOrder;
    }

    //Getter and Setter
    public String getIdCheckpoint() {
        return idCheckpoint;
    }

    public void setIdCheckpoint(String idCheckpoint) {
        this.idCheckpoint = idCheckpoint;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResponsibleRider() {
        return responsibleRider;
    }

    public void setResponsibleRider(String responsibleRider) {
        this.responsibleRider = responsibleRider;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getNewResponsibleRider() {
        return newResponsibleRider;
    }

    public void setNewResponsibleRider(String newResponsibleRider) {
        this.newResponsibleRider = newResponsibleRider;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    //toMap method, add the informations in a HashMap
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("arrivalTimestamp", arrivalTimestamp);
        result.put("departureTimestamp", departureTimestamp);
        result.put("responsibleRider", responsibleRider);
        result.put("arrivalCity", arrivalCity);
        result.put("arrivalTime", arrivalTime);
        result.put("newResponsibleRider", newResponsibleRider);
        result.put("remark", remark);
        result.put("idOrder", idOrder);
        return result;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "idCheckpoint='" + idCheckpoint + '\'' +
                '}';
    }
}
