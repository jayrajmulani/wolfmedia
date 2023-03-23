package info;

import models.Guest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Read {
    public Optional<Guest> getGuest(long id, Connection connection) throws SQLException {
        Guest guest = null;
        String query = "SELECT id, name FROM GUEST WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                guest = new Guest(resultSet.getLong("id"), resultSet.getString("name"));
            }
            return guest !=null ? Optional.of(guest) : Optional.empty();
        }
    }
//    public List<Guest> getGuest(long id){
//
//    }
}
