package deploy.example.kargobikeappg4.db.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Checkpointtype implements Comparable{

    //Attributes
    private String type;

    private Checkpointtype(){

    }

    public Checkpointtype(@NonNull String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Checkpointtype)) return false;
        Checkpointtype c = (Checkpointtype) obj;
        return c.getType().equals(this.getType());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    //Put informations in a map
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        return result;
    }

    @Override
    public String toString() {
        return type;
    }


}
