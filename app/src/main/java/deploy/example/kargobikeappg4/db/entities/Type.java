package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

public class Type implements Comparable{

    //Attributes
    private String idType;
    private String name;

    //Constructor
    public Type() {
    }

    public Type(String idType, String name) {
        this.idType = idType;
        this.name = name;
    }

    //Getter and Setter
    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    //Comparing two objects
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                '}';
    }
}
