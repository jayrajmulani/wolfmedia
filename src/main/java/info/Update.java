package info;

import models.*;
import utils.DB;

import java.sql.*;
import java.util.Optional;

public class Update {

    public void updateSong(Connection connection, SongAlbum songAlbum, long songID) {

    }
    public void updateGuest(Connection connection, Guest guest) throws SQLException {
        String query = "UPDATE GUEST SET NAME = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, guest.getName());
        statement.setLong(2, guest.getId());
        statement.executeUpdate();
    }
    public void updateSponsor(Connection connection, Sponsor sponsor) throws SQLException {
        String query = "UPDATE SPONSOR SET NAME = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, sponsor.getName());
        statement.setLong(2, sponsor.getId());
        statement.executeUpdate();
    }
    public void updateService(Connection connection, Service service) throws SQLException {
        String query = "UPDATE SERVICE SET NAME = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, service.getName());
        statement.setLong(2, service.getId());
        statement.executeUpdate();
    }
    public void updateArtist(Connection connection, Artist artist) throws SQLException {
        String query = "UPDATE ARTIST SET " +
                "name = ?,  " +
                "country = ?, " +
                "status = ? " +
                "WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, artist.getName());
        statement.setString(1, artist.getCountry());
        statement.setString(1, artist.getStatus().getStatus());
        statement.setLong(1, artist.getId());
        statement.executeUpdate();
    }
    public void updateAlbum(Connection connection, Album album) throws SQLException {
        String query = "UPDATE ALBUM SET " +
                "name = ?,  " +
                "release_date = ?, " +
                "edition = ? " +
                "WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, album.getName());
        statement.setDate(1, album.getRelease_date());
        statement.setInt(1, album.getEdition());
        statement.setLong(1, album.getId());
        statement.executeUpdate();
    }
    public void updateRecordLabel(Connection connection, RecordLabel recordLabel) throws SQLException {
        String query = "UPDATE RECORD_LABEL SET NAME = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, recordLabel.getName());
        statement.setLong(2, recordLabel.getId());
        statement.executeUpdate();
    }
    public void updateHost(Connection connection, Host host) throws SQLException {
        String query = "UPDATE HOST SET " +
                "first_name = ?,  " +
                "last_name = ?, " +
                "city = ?," +
                "email = ?," +
                "phone = ? " +
                "WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, host.getFirstName());
        statement.setString(2, host.getLastName());
        statement.setString(3, host.getCity());
        statement.setString(4, host.getEmail());
        statement.setString(5, host.getPhone());
        statement.setLong(6, host.getId());
        statement.executeUpdate();
    }

    public void updateUser(Connection connection, User user) throws SQLException {
        String query = "UPDATE USER SET " +
                "f_name = ?,  " +
                "l_name = ?, " +
                "reg_date = ?," +
                "phone = ?," +
                "email = ? " +
                "WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getFName());
        statement.setString(2, user.getLName());
        statement.setDate(3, (Date) user.getRegDate());
        statement.setString(4, user.getPhone());
        statement.setString(5, user.getEmail());
        statement.setLong(6, user.getId());
        statement.executeUpdate();
    }

    public void updateEpisode(Connection connection, Episode episode) throws SQLException {
        String query = "UPDATE EPISODE SET " +
                "title = ?," +
                "release_date = ?," +
                "duration = ?, " +
                "adv_count = ?, " +
                "bonus_rate = ?, " +
                "episode_id = ? " +
                "WHERE episode_num = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, episode.getTitle());
        statement.setDate(2, (Date) episode.getReleaseDate());
        statement.setDouble(3, episode.getDuration());
        statement.setDouble(4, episode.getAdvCount());
        statement.setDouble(5, episode.getBonusRate());
        statement.setString(6, episode.getEpisodeId());
        statement.setLong(7, episode.getEpisodeNum());

        statement.executeUpdate();
    }

    public void updatePodcast(Connection connection, Podcast podcast) throws SQLException {
        String query = "UPDATE PODCAST SET " +
                "name = ?," +
                "country = ?," +
                "language = ?, " +
                "flat_fee = ? " +
                "WHERE id = ?";

        String hostDel = "DELETE FROM PODCAST_HOST WHERE podcast_id=?";
        String hostAdd = "INSERT INTO PODCAST_HOST(host_id, podcast_id) VALUES (?,?)";
//        String sponsorDel = "DELETE FROM PODCAST_SPONSOR WHERE podcast_id=?";
//        String sponsorAdd = "INSERT INTO PODCAST_SPONSOR(podcast_id, sponsor_id) VALUES (?,?)";
//        String genreDel = "DELETE FROM PODCAST_GENRE WHERE podcast_id=?";
//        String genreAdd = "INSERT INTO PODCAST_GENRE(podcast_id, genre_id) VALUES (?,?)";

        try {

            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement hostDeleteStatement = connection.prepareStatement(hostDel);
            PreparedStatement hostAddStatement = connection.prepareStatement(hostAdd);
//            PreparedStatement sponsorDelStatement = connection.prepareStatement(sponsorDel);
//            PreparedStatement sponsorAddStatement = connection.prepareStatement(sponsorAdd);
//            PreparedStatement genreDelStatement = connection.prepareStatement(genreDel);
//            PreparedStatement genreAddStatement = connection.prepareStatement(genreAdd);

            statement.setString(1, podcast.getName());
            statement.setString(2, podcast.getCountry());
            statement.setString(3, podcast.getLanguage());
            statement.setDouble(4, podcast.getFlatFee());
            statement.setLong(5, podcast.getId());

            statement.executeUpdate();

            hostDeleteStatement.setLong(1, podcast.getId());
//            sponsorDelStatement.setLong(1, podcast.getId());
//            genreDelStatement.setLong(1, podcast.getId());

            hostDeleteStatement.executeUpdate();
//            sponsorDelStatement.executeUpdate();
//            genreDelStatement.executeUpdate();

            podcast.getHosts().forEach(host -> {
                try {
                    hostAddStatement.setLong(1, host.getId());
                    hostAddStatement.setLong(2, podcast.getId());
                    hostAddStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    DB.rollBackTransaction(connection);
                }
            });
//            podcast.getSponsors().forEach(sponsor -> {
//                try {
//                    sponsorAddStatement.setLong(1, podcast.getId());
//                    sponsorAddStatement.setLong(2, sponsor.getId());
//                    sponsorAddStatement.executeUpdate();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    DB.rollBackTransaction(connection);
//                }
//            });
//            podcast.getGenres().forEach(genre -> {
//                try {
//                    sponsorAddStatement.setLong(1, podcast.getId());
//                    sponsorAddStatement.setLong(2, genre.getId());
//                    sponsorAddStatement.executeUpdate();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    DB.rollBackTransaction(connection);
//                }
//            });
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }finally {
            connection.setAutoCommit(true);
        }

    }
}
