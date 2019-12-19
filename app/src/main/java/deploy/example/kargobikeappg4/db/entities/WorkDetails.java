package deploy.example.kargobikeappg4.db.entities;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class WorkDetails implements Comparable {

    private String idWorkDetails;
    private String Date;
    private String Hours;
    private int Deliveries;

    //Constructor
    public WorkDetails(){

    }

    public WorkDetails( String Date, String Hours, int Deliveries) {
        this.Date = Date;
        this.Hours = Hours;
        this.Deliveries = Deliveries;
    }

    //Getter and Setter
    public String getIdWorkDetails() {
        return idWorkDetails;
    }

    public void setIdWorkDetails(String idWorkDetails) {
        this.idWorkDetails = idWorkDetails;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String Hours) {
        this.Hours = Hours;
    }

    public int getDeliveries(){return Deliveries;}

    public void setDeliveries(int Deliveries){this.Deliveries=Deliveries; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof WorkDetails)) return false;
        WorkDetails o = (WorkDetails) obj;
        return o.getIdWorkDetails().equals(this.getIdWorkDetails());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idWorkDetails", idWorkDetails);
        result.put("Date", Date);
        result.put("Hours", Hours);
        result.put("Deliveries", Deliveries);

        return result;
    }
}
