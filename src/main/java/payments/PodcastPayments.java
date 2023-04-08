package payments;

import models.*;
import utils.PaymentUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PodcastPayments {
    public Optional<PaymentInfo> calculateHostPayAmount(Connection connection, long podcastId, long episodeNum) {
        String query = "select 1 as service_id, PH.host_id, CURRENT_TIMESTAMP as timestamp, " +
                " (P.flat_fee + E.adv_count * E.bonus_rate) / HC.host_count as amount " +
                "from PODCAST P " +
                "JOIN EPISODE E on P.id = E.podcast_id " +
                "JOIN PODCAST_HOST PH on P.id = PH.podcast_id " +
                "CROSS JOIN (select " +
                "                count(PH2.podcast_id) as host_count " +
                "            from PODCAST_HOST PH2 " +
                "            where podcast_id=4) as HC " +
                "WHERE " +
                "    P.id= ? " +
                "    AND E.episode_num = ? ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            statement.setLong(2, episodeNum);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new PaymentInfo(
                        rs.getLong("service_id"),
                        rs.getLong("host_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp"),
                        PaymentUtils.Stakeholder.SERVICE,
                        PaymentUtils.Stakeholder.PODCAST_HOST
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    public List<PaymentHistoryItem> getHostPaymentHistory(Connection connection, long hostId) throws SQLException {
        List<PaymentHistoryItem> paymentHistoryItems = new ArrayList<>();
        String query = "SELECT HP.host_id, H.first_name, H.last_name, S.id as service_id, S.name as service_name, HP.amount, HP.timestamp " +
                "FROM HOST_PAY HP, HOST H, SERVICE S " +
                "WHERE HP.host_id = H.id AND HP.service_id = S.id " +
                "AND HP.host_id = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, hostId);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            paymentHistoryItems.add(
                    new PaymentHistoryItem(
                            PaymentUtils.Stakeholder.SERVICE,
                            new Service(resultSet.getLong("service_id"), resultSet.getString("service_name")),
                            PaymentUtils.Stakeholder.PODCAST_HOST,
                            new Host(resultSet.getLong("host_id"), resultSet.getString("first_name"), resultSet.getString("last_name")),
                            resultSet.getTimestamp("timestamp"),
                            resultSet.getDouble("amount")
                    )
            );
        }
        return paymentHistoryItems;
    }
}
