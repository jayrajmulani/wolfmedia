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
        String query = "UPDATE manand.RECORD_LABEL SET NAME = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, recordLabel.getName());
        statement.setLong(2, recordLabel.getId());
        statement.executeUpdate();
    }
}
