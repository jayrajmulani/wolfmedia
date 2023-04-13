package info;

import models.SongAlbum;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {

    public void deleteSong(Connection connection, long songId) {
        // Flush the individual listen records
        String query = "delete from SONG where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, songId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteGuest(Connection connection, long guestId) {
        // Flush the individual listen records
        String query = "delete from GUEST where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, guestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteArtist(Connection connection, long artistId) {
        // Flush the individual listen records
        String query = "delete from ARTIST where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, artistId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteRecordLabel(Connection connection, long recordLabelId) {
        // Flush the individual listen records
        String query = "delete from RECORD_LABEL where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, recordLabelId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deletePodcast(Connection connection, long podcastId) {
        // Flush the individual listen records
        String query = "delete from PODCAST where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteEpisode(Connection connection, long podcastId, long episodeNum) {
        // Flush the individual listen records
        String query = "delete from EPISODE where podcast_id = ? AND episode_num = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            statement.setLong(2, episodeNum);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteUser(Connection connection, long userId) {
        // Flush the individual listen records
        String query = "delete from USER where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteAlbum(Connection connection, long albumId) {
        // Flush the individual listen records
        String query = "delete from ALBUM where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, albumId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteService(Connection connection, long serviceId) {
        // Flush the individual listen records
        String query = "delete from SERVICE where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, serviceId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteSponsor(Connection connection, long sponsorId) {
        // Flush the individual listen records
        String query = "delete from SPONSOR where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, sponsorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deleteHost(Connection connection, long hostId) {
        // Flush the individual listen records
        String query = "delete from HOST where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, hostId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

}
