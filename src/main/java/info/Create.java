package info;

import models.Guest;
import models.Song;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Create {
    public void createSong(Connection connection, Song song) throws SQLException {

    }
    public void createGuest(Connection connection, Guest guest) throws SQLException {
        String query = "INSERT INTO GUEST(name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, guest.getName());
            statement.executeUpdate();
            System.out.println("Guest created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
}
