package info;

import models.Guest;
import models.Song;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Create {
    public void createSong(Connection connection, Song song) throws SQLException {
        String query = "insert into SONG (title, release_country, language, duration, royalty_rate, release_date, royalty_paid) values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, song.getTitle());
            statement.setString(2, song.getReleaseCountry());
            statement.setString(3, song.getLanguage());
            statement.setFloat(4, song.getDuration());
            statement.setFloat(5, song.getRoyaltyRate());
            statement.setDate(6, song.getReleaseDate());
            statement.setBoolean(7,song.isRoyaltyPaid());
            if(statement.executeUpdate() > 0 ){
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                System.out.println("Song created successfully with id " + rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
    public void createGuest(Connection connection, Guest guest) throws SQLException {
        String query = "INSERT INTO GUEST(name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, guest.getName());
            if(statement.executeUpdate() > 0 ){
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                System.out.println("Guest created successfully with id " + rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
}
