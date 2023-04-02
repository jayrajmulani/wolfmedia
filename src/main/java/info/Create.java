package info;

import models.*;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Create {
    private static Read read = new Read();
    public long createSong(Connection connection, Song song) throws SQLException {
        String query = "insert into SONG (title, release_country, language, duration, royalty_rate, release_date, royalty_paid) " +
                       "values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, song.getTitle());
            statement.setString(2, song.getReleaseCountry());
            statement.setString(3, song.getLanguage());
            statement.setDouble(4, song.getDuration());
            statement.setDouble(5, song.getRoyaltyRate());
            statement.setDate(6, song.getReleaseDate());
            statement.setBoolean(7,song.isRoyaltyPaid());
            long songId;
            if(statement.executeUpdate() > 0 ) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                songId = rs.getInt(1);
                song.getGenres().forEach(inputGenre -> {
                    try {
                        Optional<Genre> genre = read.getGenreByName(connection, inputGenre.getName());
                        long genreId = genre.isPresent() ? genre.get().getId() : createGenre(connection, inputGenre);
                        String mapSongGenreQuery = "INSERT INTO SONG_GENRE(song_id, genre_id) values (?,?)";
                        PreparedStatement mapSongGenreStatement = connection.prepareStatement(mapSongGenreQuery);
                        mapSongGenreStatement.setLong(1, songId);
                        mapSongGenreStatement.setLong(2, genreId);
                        mapSongGenreStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                songId = 0L;
            }
            return songId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }
    public long createGuest(Connection connection, Guest guest) throws SQLException {
        String query = "INSERT INTO GUEST(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, guest.getName());
    }
    public long createGenre(Connection connection, Genre genre) throws SQLException {
        String query = "INSERT INTO GENRE(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, genre.getName());
    }
    public long createSponsor(Connection connection, Sponsor sponsor) throws SQLException {
        String query = "INSERT INTO SPONSOR(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, sponsor.getName());
    }
    public long createArtistType(Connection connection, ArtistType artistType) throws SQLException {
        String query = "INSERT INTO SPONSOR(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, artistType.getName());
    }

    public long createHost(Connection connection, Host host) throws SQLException {
        String query = "INSERT INTO HOST(first_name, last_name, city, email, phone) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, host.getFirst_name());
            statement.setString(2, host.getLast_name());
            statement.setString(3, host.getCity());
            statement.setString(4, host.getEmail());
            statement.setString(5, host.getPhone());
            long hostID;
            if(statement.executeUpdate() > 0 ) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                hostID = rs.getInt(1);
            } else {
                hostID = 0L;
            }
            return hostID;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    private long insertAndGetIdForSingleNameColumnTables(Connection connection, String query, String name) {
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            if(statement.executeUpdate() > 0 ){
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }
}
