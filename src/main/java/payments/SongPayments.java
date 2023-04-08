package payments;

import models.*;
import org.checkerframework.checker.units.qual.A;
import utils.Constants;
import utils.PaymentUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SongPayments {
    private final Constants constants = new Constants();
    public Optional<PaymentInfo> calculateRoyaltyAmount(Connection connection, long songId) {
        String query =
                "SELECT O.record_label_id, 1 as service_id, MPC.play_count * S.royalty_rate as amount, CURRENT_TIMESTAMP as timestamp " +
                        "FROM " +
                        "SONG S, OWNS O, " +
                        "(SELECT count(*) as play_count FROM SONG_LISTEN SL " +
                        "WHERE MONTH(timestamp) = MONTH(CURRENT_TIMESTAMP) " +
                        "AND YEAR(timestamp) = YEAR(CURRENT_TIMESTAMP) " +
                        "AND song_id = ?) as MPC WHERE O.song_id = ? AND S.id = O.song_id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, songId);
            statement.setLong(2, songId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new PaymentInfo(
                        rs.getLong("service_id"),
                        rs.getLong("record_label_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp"),
                        PaymentUtils.Stakeholder.SERVICE,
                        PaymentUtils.Stakeholder.RECORD_LABEL
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    public List<PaymentHistoryItem> getRecordLabelPaymentHistory(Connection connection, long recordLabelId) throws SQLException {
        List<PaymentHistoryItem> paymentHistoryItems = new ArrayList<>();
        String query = "SELECT record_label_id, RL.name, RP.service_id, S.name as service_name, RP.amount, RP.timestamp " +
                "FROM RL_PAY RP, RECORD_LABEL RL, SERVICE S " +
                "WHERE RP.record_label_id = RL.id AND RP.service_id = S.id " +
                "AND RP.record_label_id = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, recordLabelId);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            paymentHistoryItems.add(
                    new PaymentHistoryItem(
                            PaymentUtils.Stakeholder.SERVICE,
                            new Service(resultSet.getLong("service_id"), resultSet.getString("service_name")),
                            PaymentUtils.Stakeholder.RECORD_LABEL,
                            new RecordLabel(resultSet.getLong("record_label_id"), resultSet.getString("name")),
                            resultSet.getTimestamp("timestamp"),
                            resultSet.getDouble("amount")
                    )
            );
        }
        return paymentHistoryItems;
    }
    public List<PaymentInfo> calculateRoyaltyAmountsForArtist(Connection connection, long songId) throws SQLException {
        List<PaymentInfo> paymentInfo = new ArrayList<>();
        String query = """
                select C.artist_id, O.record_label_id,
                    (MPC.play_count * S.royalty_rate * ? / CC.artist_count) as amount,
                    CURRENT_TIMESTAMP as timestamp
                from
                    SONG S, OWNS O, CREATES C,
                    (select
                         count(*) as play_count
                     from
                         SONG_LISTEN SL
                     where
                             MONTH(timestamp) = MONTH(CURRENT_TIMESTAMP)
                       AND YEAR(timestamp) = YEAR(CURRENT_TIMESTAMP)
                       and song_id = 1) as MPC,
                    (select
                         count(C2.artist_id) as artist_count
                     from CREATES C2
                     where C2.song_id = ?) as CC
                where
                        O.song_id = S.id
                  AND S.id = C.song_id
                  AND C.song_id = ?;
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, constants.ARTIST_ROYALTY_SHARE);
        statement.setLong(2, songId);
        statement.setLong(3, songId);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            paymentInfo.add(
            new PaymentInfo(
                    resultSet.getLong("artist_id"),
                    resultSet.getLong("record_label_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getTimestamp("timestamp"),
                    PaymentUtils.Stakeholder.RECORD_LABEL,
                    PaymentUtils.Stakeholder.ARTIST
                )
            );
        }
        return paymentInfo;
    }
    public List<PaymentHistoryItem> getArtistPaymentHistory(Connection connection, long artistId) throws SQLException {
        List<PaymentHistoryItem> paymentHistoryItems = new ArrayList<>();
        String query = "SELECT artist_id, A.name as artist_name, RL.id, RL.name as record_label_name, AP.amount, AP.timestamp " +
                "FROM ARTIST_PAY AP, ARTIST A, RECORD_LABEL RL " +
                "WHERE AP.artist_id = A.id AND AP.record_label_id = RL.id " +
                "AND A.id = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            paymentHistoryItems.add(
                    new PaymentHistoryItem(
                            PaymentUtils.Stakeholder.RECORD_LABEL,
                            new RecordLabel(resultSet.getLong("record_label_id"), resultSet.getString("record_label_name")),
                            PaymentUtils.Stakeholder.ARTIST,
                            new Artist(resultSet.getLong("artist_id"), resultSet.getString("artist_name")),
                            resultSet.getTimestamp("timestamp"),
                            resultSet.getDouble("amount")
                    )
            );
        }
        return paymentHistoryItems;
    }
}
