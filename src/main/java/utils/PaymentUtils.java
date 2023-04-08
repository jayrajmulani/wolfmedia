package utils;

import models.PaymentInfo;

import java.sql.*;
import java.util.NoSuchElementException;

public class PaymentUtils {
    public void processPayment(Connection connection, PaymentInfo paymentInfo) throws SQLException {
        // Start a Transaction
        connection.setAutoCommit(false);

        if (paymentInfo.getSenderType() == Stakeholder.SERVICE) {
            if (paymentInfo.getReceiverType() == Stakeholder.RECORD_LABEL) {
                // Make an entry in the RL_PAY table
                String query = "INSERT INTO RL_PAY(service_id, record_label_id, amount) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, paymentInfo.getSenderId());
                statement.setLong(2, paymentInfo.getReceiverId());
                statement.setDouble(3, paymentInfo.getAmount());
                statement.executeUpdate();

                query = "UPDATE SERVICE set balance = balance - ? where id = ?";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, paymentInfo.getAmount());
                statement.setLong(2, paymentInfo.getSenderId());
                statement.executeUpdate();
                connection.commit();

            } else if (paymentInfo.getReceiverType() == Stakeholder.PODCAST_HOST) {
                // Make an entry in the HOST_PAY table
                String query = "INSERT INTO HOST_PAY(service_id, host_id, amount) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, paymentInfo.getSenderId());
                statement.setLong(2, paymentInfo.getReceiverId());
                statement.setDouble(3, paymentInfo.getAmount());
                statement.executeUpdate();

                query = "UPDATE SERVICE set balance = balance - ? where id = ?";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, paymentInfo.getAmount());
                statement.setLong(2, paymentInfo.getSenderId());
                statement.executeUpdate();
                connection.commit();
            } else {
                throw new IllegalArgumentException("Illegal Receiver Type. " +
                        "Service can only make payments to Record Labels or Podcast Hosts");
            }
        } else if (paymentInfo.getSenderType() == Stakeholder.RECORD_LABEL) {
            if (paymentInfo.getReceiverType() == Stakeholder.ARTIST) {
                // Make an entry in the ARTIST_PAY table
                String query = "INSERT INTO ARTIST_PAY(record_label_id, ARTIST_PAY.artist_id, amount) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, paymentInfo.getSenderId());
                statement.setLong(2, paymentInfo.getReceiverId());
                statement.setDouble(3, paymentInfo.getAmount());
                statement.executeUpdate();
            } else {
                connection.rollback();
                throw new IllegalArgumentException("Illegal Receiver Type. " +
                        "Record Labels can only make payments to Artists");
            }
        } else if (paymentInfo.getSenderType() == Stakeholder.USER) {
            if (paymentInfo.getReceiverType() == Stakeholder.SERVICE) {
                // Make an entry in the EARNS table
                String query = "INSERT INTO EARNS(service_id, user_id, amount) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, paymentInfo.getSenderId());
                statement.setLong(2, paymentInfo.getReceiverId());
                statement.setDouble(3, paymentInfo.getAmount());
                statement.executeUpdate();

                query = "UPDATE SERVICE set balance = balance + ? where id = ?";
                statement = connection.prepareStatement(query);
                statement.setDouble(1, paymentInfo.getAmount());
                statement.setLong(2, paymentInfo.getSenderId());
                statement.executeUpdate();
                connection.commit();
            } else {
                connection.rollback();
                throw new IllegalArgumentException("Illegal Receiver Type. " +
                        "Users can only make payments to Service");
            }
        } else {
            connection.rollback();
            throw new IllegalArgumentException("Illegal Sender Type");
        }
        // End Transaction
        connection.setAutoCommit(true);
    }
    public double getBalanceForService(Connection connection) throws SQLException {
        String query = "SELECT balance from SERVICE WHERE id = 1";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        if(!rs.next()){
            throw new NoSuchElementException("Missing Service with id 1");
        }
        return rs.getDouble("balance");
    }
    public enum Stakeholder{
        RECORD_LABEL, ARTIST, PODCAST_HOST, SERVICE, USER
    }
}
