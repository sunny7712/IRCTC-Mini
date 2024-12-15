package ticket.booking.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        this.userList = loadUsers();
    }

    public UserBookingService() throws IOException{
        this.userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
        return userList;
    }

    public Boolean loginUser(){
        Optional<User>foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }
        catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) throws IOException {
        boolean isRemoved = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if(isRemoved){
            saveUserListToFile();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<Train> getTrains(String source, String dest){
        try{
            TrainService trainService = new TrainService();
            return trainService.getTrains(source, dest);
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        try{
            TrainService trainService = new TrainService();
            return trainService.fetchSeats(train);
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public Boolean bookTrainSeat(Train trainSelectedForBooking, int row, int col){
        try{
            TrainService trainService = new TrainService();
            if(trainService.bookTrainSeat(trainSelectedForBooking, row, col)){
                Ticket newTicket = new Ticket(UUID.randomUUID().toString(), this.user.getUserId(), trainSelectedForBooking);
                this.user.addTicket(newTicket);
                saveUserListToFile();
                return Boolean.TRUE;
            }
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }
}
