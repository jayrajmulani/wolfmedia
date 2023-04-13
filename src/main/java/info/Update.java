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
    public void updateArtist(Connection connection, Artist artist){
        // TODO
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
}
