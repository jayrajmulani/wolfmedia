package info;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Read {
    public Optional<Song> getSong(long id, Connection connection) throws SQLException {
        String query = "SELECT SONG.id, title, release_country, language, duration, royalty_rate, royalty_paid, release_date, GENRE.name genre, GENRE.id genre_id " +
                "FROM SONG, SONG_GENRE, GENRE WHERE SONG.id = ? AND SONG.id = SONG_GENRE.song_id AND SONG_GENRE.genre_id = GENRE.id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            List<Genre> genres = new ArrayList<>();
            while (resultSet.next()) {
                genres.add(new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre")));
            }
            resultSet.beforeFirst();
            if (resultSet.next()) {
                return Optional.of(
                        new Song(resultSet.getLong("id"), resultSet.getString("title"),
                                resultSet.getString("release_country"), resultSet.getString("language"),
                                resultSet.getFloat("duration"), resultSet.getFloat("royalty_rate"),
                                resultSet.getDate("release_date"), resultSet.getBoolean("royalty_paid"),
                                genres
                        ));
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
            if (resultSet.next()) {
                return Optional.of(new Guest(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }

    public Optional<Genre> getGenreByName(Connection connection, String name) throws SQLException {
        String query = "SELECT id, name FROM GENRE WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new Genre(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }

    public Optional<Host> getHost(Long id, Connection connection) throws SQLException {
        String query = "SELECT HOST.id, first_name, last_name, city, email, phone " +
                "FROM HOST WHERE HOST.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        new Host(resultSet.getLong("id"), resultSet.getString("first_name"),
                                resultSet.getString("last_name"), resultSet.getString("city"),
                                resultSet.getString("email"), resultSet.getString("phone")
                        ));
            }
            return Optional.empty();
        }

    }

    public Optional<Podcast> getPodcast(Long id, Connection connection) throws SQLException {
        String query = "SELECT PODCAST.id, name, language, country, flat_fee, HOST.id, " +
                " HOST.first_name, HOST.last_name " +
                " FROM PODCAST " +
                " JOIN PODCAST_HOST ON PODCAST.id = PODCAST_HOST.podcast_id" +
                " JOIN HOST ON HOST.id = PODCAST_HOST.host_id" +
                " WHERE PODCAST.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            List<Host> hosts = new ArrayList<>();
            while (resultSet.next()) {
                hosts.add(
                        new Host(
                                resultSet.getLong("HOST.id"),
                                resultSet.getString("HOST.first_name"),
                                resultSet.getString("HOST.first_name")
                        )
                );
            }
            resultSet.beforeFirst();
            if (resultSet.next()) {
                return Optional.of(
                        new Podcast(
                                resultSet.getLong("PODCAST.id"),
                                resultSet.getString("name"),
                                resultSet.getString("language"),
                                resultSet.getString("country"),
                                resultSet.getDouble("flat_fee"),
                                0, // TODO rating
                                0, // TODO sub count
                                hosts,
                                new ArrayList<Sponsor>(), // TODO sponsor
                                new ArrayList<Genre>() // TODO
                        ));
            }
            return Optional.empty();
        }

    }
}
