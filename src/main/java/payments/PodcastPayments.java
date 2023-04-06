package payments;

import models.PaymentInfo;
import utils.PaymentUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
