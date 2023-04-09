package info;

import models.*;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
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

    public Optional<Sponsor> getSponsor(long id, Connection connection) throws SQLException {
        String query = "SELECT id, name FROM SPONSOR WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new Sponsor(resultSet.getLong("id"), resultSet.getString("name")));
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
    public Optional<Episode> getEpisode(Connection connection, Long podcastID, Long episodeNum) throws SQLException {
        String query = "SELECT * from EPISODE WHERE podcast_id=? AND episode_num=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastID);
            statement.setLong(2, episodeNum);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return Optional.of(
                        new Episode(
                                resultSet.getLong("podcast_id"),
                                resultSet.getLong("episode_num"),
                                resultSet.getString("title"),
                                resultSet.getDate("release_date"),
                                resultSet.getDouble("duration"),
                                resultSet.getInt("adv_count"),
                                resultSet.getDouble("bonus_rate")
                        ));
            }
            return Optional.empty();
        }
    }
    public List<Host> getAllHosts(Connection connection) throws SQLException {
        String query = "SELECT id, first_name, last_name from HOST";
        List<Host> hosts = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            hosts.add(new Host(
                    resultSet.getLong("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
                )
            );
        }
        return hosts;
    }
    public List<Song> getAllSongs(Connection connection) throws SQLException {
        String query = "SELECT * from SONG";
        List<Song> songs = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            songs.add(new Song(
                            resultSet.getLong("id"),
                            resultSet.getString("title"),
                            resultSet.getString("release_country"),
                            resultSet.getString("language"),
                            resultSet.getDouble("duration"),
                            resultSet.getDouble("royalty_rate"),
                            resultSet.getDate("release_date"),
                            resultSet.getBoolean("royalty_paid"),
                            List.of()
                    )
            );
        }
        return songs;
    }
    public List<Podcast> getAllPodcasts(Connection connection) throws SQLException {
        String query = "SELECT * from PODCAST";
        List<Podcast> podcasts = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            podcasts.add(new Podcast(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country"),
                            resultSet.getString("language"),
                            resultSet.getDouble("flat_fee")
                    )
            );
        }
        return podcasts;
    }
    public List<Episode> getAllPodcastEpisodes(Connection connection, long podcastId) throws SQLException {
        String query = "SELECT * from EPISODE WHERE podcast_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);
        List<Episode> episodes = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            episodes.add(new Episode(
                            resultSet.getLong("podcast_id"),
                            resultSet.getLong("episode_num"),
                            resultSet.getString("title"),
                            resultSet.getDate("release_date"),
                            resultSet.getDouble("duration"),
                            resultSet.getInt("adv_count"),
                            resultSet.getDouble("bonus_rate")
                    )
            );
        }
        return episodes;
    }
    public List<RecordLabel> getAllRecordLabels(Connection connection) throws SQLException {
        String query = "SELECT * from RECORD_LABEL";
        List<RecordLabel> recordLabels = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            recordLabels.add(new RecordLabel(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return recordLabels;
    }
    public List<Artist> getAllArtists(Connection connection) throws SQLException {
        String query = "SELECT id,name, country, status from ARTIST";
        List<Artist> artists = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            artists.add(new Artist(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country"),
                            Artist.ArtistStatus.valueOf(resultSet.getString("status"))
                    )
            );
        }
        return artists;
    }
    public List<Service> getAllServices(Connection connection) throws SQLException {
        String query = "SELECT id,name from SERVICE";
        List<Service> services = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            services.add(new Service(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return services;
    }
    public List<User> getAllUsers(Connection connection) throws SQLException {
        String query = "SELECT id,f_name, l_name, premium_status, monthly_premium_fees from USER";
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            users.add(new User(
                            resultSet.getLong("id"),
                            resultSet.getString("f_name"),
                            resultSet.getString("l_name"),
                            resultSet.getBoolean("premium_status"),
                            resultSet.getDouble("monthly_premium_fees")
                    )
            );
        }
        return users;
    }
}
