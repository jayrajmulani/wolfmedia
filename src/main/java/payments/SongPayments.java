package payments;

import models.PaymentInfo;
import utils.PaymentUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SongPayments {
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
}
