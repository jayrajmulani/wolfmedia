package info;

import models.Guest;
import models.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Read {
    public Optional<Song> getSong(long id, Connection connection) throws SQLException {
        String query = "SELECT id, title, release_country, language, duration, royalty_rate, royalty_paid FROM SONG WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                return Optional.of(
                        new Song(resultSet.getLong("id"), resultSet.getString("title"),
                                resultSet.getString("release_country"), resultSet.getString("language"),
                                resultSet.getFloat("duration"),resultSet.getFloat("royalty_rate"),
                                resultSet.getDate("release_date"), resultSet.getBoolean("royalty_paid")));
            }
            return Optional.empty();
        }
    }
    public Optional<Guest> getGuest(long id, Connection connection) throws SQLException {
        String query = "SELECT id, name FROM GUEST WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                return Optional.of(new Guest(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }
}
