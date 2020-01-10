package deploy.example.kargobikeappg4.db.entities;

import java.util.HashMap;
import java.util.Map;

public class TrainStation implements Comparable{
    private String idTrainStation;
    private String name;

    public TrainStation() {
    }

    public TrainStation(String idTrainStation, String name) {
        this.idTrainStation = idTrainStation;
        this.name = name;
    }

    public String getIdTrainStation() {
        return idTrainStation;
    }

    public void setIdTrainStation(String idTrainStation) {
        this.idTrainStation = idTrainStation;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }

    @Override
    public String toString() {
        return "TrainStation{" +
                "name='" + name + '\'' +
                '}';
    }
}
