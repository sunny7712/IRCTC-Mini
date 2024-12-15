package ticket.booking.services;

import ticket.booking.entities.Train;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "src/main/java/ticket/booking/localDb/trains.json";
    List<Train> trainsList;
    public TrainService() throws IOException{
        this.trainsList = loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        return objectMapper.readValue(trains, new TypeReference<List<Train>>(){
        });
    }

    public List<Train> getTrains(String source, String dest){
        return trainsList.stream().filter(train -> {
            int sourceIndex = train.getStations().indexOf(source);
            int destIndex = train.getStations().indexOf(dest);
            return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
        }).collect(Collectors.toList());
    }

    public List<List<Integer>> fetchSeats(Train train){
        return trainsList.stream().filter(train1 -> {
            return train.getTrainId().equals(train1.getTrainId());
        }).findFirst().get().getSeats();
    }

    private void saveTrainsListToFile() throws IOException{
        File trains = new File(TRAINS_PATH);
        objectMapper.writeValue(trains, trainsList);
    }

    public Boolean bookTrainSeat(Train train, int row, int col) throws IOException {
        List<List<Integer>>seats = fetchSeats(train);
        if (row < seats.size() && col < seats.getFirst().size() && seats.get(row).get(col) == 0){
            seats.get(row).set(col, 1);
            saveTrainsListToFile();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
