package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>>seats;
    private Map<String, String>stationTimes;
    private List<String>stations;

    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes, List<String>stations){
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public Train(){

    }

    public String getTrainId(){
        return this.trainId;
    }

    public List<String> getStations(){
        return this.stations;
    }

    public Map<String, String> getStationTimes(){
        return this.stationTimes;
    }

    public List<List<Integer>> getSeats(){
        return this.seats;
    }
}
