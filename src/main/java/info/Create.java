package info;

import models.*;
import utils.DB;

import java.sql.*;
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
            statement.setBoolean(7, song.isRoyaltyPaid());
            long songId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                songId = rs.getLong(1);
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

    public long createUser(Connection connection, User user) throws SQLException {
        String query = "insert into USER (f_name, l_name, phone, email, reg_date, premium_status, monthly_premium_fees) values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFName());
            statement.setString(2, user.getLName());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getEmail());
            statement.setDate(5, (Date) user.getRegDate());
            statement.setBoolean(6, user.getPremiumStatus());
            statement.setDouble(7, user.getMonthlyPremiumFees());

            long userId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                userId = rs.getLong(1);
            } else
                userId = 0L;
            return userId;
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
            statement.setString(1, host.getFirstName());
            statement.setString(2, host.getLastName());
            statement.setString(3, host.getCity());
            statement.setString(4, host.getEmail());
            statement.setString(5, host.getPhone());
            long hostID;
            if (statement.executeUpdate() > 0) {
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

    public long createPodcast(Connection connection, Podcast podcast) throws SQLException {
        String query = "INSERT INTO PODCAST(name, country, language, flat_fee) VALUES (?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, podcast.getName());
            statement.setString(2, podcast.getCountry());
            statement.setString(3, podcast.getLanguage());
            statement.setDouble(4, podcast.getFlatFee());
            long podcastID;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                podcastID = rs.getInt(1);
                podcast.getHosts().forEach(inputHost -> {
                    try {
                        Optional<Host> host = read.getHost(inputHost.getId(), connection);
                        long hostID = host.isPresent() ? host.get().getId() : createHost(connection, inputHost);
                        String mapPodcastHost = "INSERT INTO PODCAST_HOST(podcast_id, host_id) values (?,?)";
                        PreparedStatement mapPodcastHostStatement = connection.prepareStatement(mapPodcastHost);
                        mapPodcastHostStatement.setLong(1, podcastID);
                        mapPodcastHostStatement.setLong(2, hostID);
                        mapPodcastHostStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                podcastID = 0L;
            }
            return podcastID;
        } catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    private long insertAndGetIdForSingleNameColumnTables(Connection connection, String query, String name) {
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            if (statement.executeUpdate() > 0) {
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
