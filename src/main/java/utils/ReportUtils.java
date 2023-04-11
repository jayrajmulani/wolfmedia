package utils;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportUtils {
    public List<Stats> getSongPlayCountByMonth(Connection connection, long songId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
            select
             S.id, S.title, MONTH(SL.timestamp) month ,YEAR(SL.timestamp) year,
                count(*) as monthly_count
            from
                SONG_LISTEN SL, SONG S
            where
                    SL.song_id = S.id
            group by
                S.id, S.title, month, year
            having
                    S.id = ?
            UNION 
            select s.id, s.title, month, year, play_count as monthly_count
            from HISTORICAL_SONG_PLAY_COUNT , SONG s WHERE s.id = HISTORICAL_SONG_PLAY_COUNT.song_id 
            AND s.id = ?
            order by year desc , month desc
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, songId);
        statement.setLong(2, songId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Song(resultSet.getLong("id"), resultSet.getString("title")),
                            resultSet.getInt("month"),
                            resultSet.getInt("year"),
                            resultSet.getLong("monthly_count")
                    )
            );
        }
        return stats;
    }
    public List<Stats> getArtistPlayCountByMonth(Connection connection, long artistId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
            select
                C.artist_id  id, A.name, MONTH(SL.timestamp) as month, YEAR(SL.timestamp) as year,
                count(*) as monthly_count
            from
                SONG_LISTEN SL, CREATES C, SONG S, ARTIST A
            where
                SL.song_id = C.song_id
                AND S.id = SL.song_id
                AND C.artist_id = A.id
            group by
                C.artist_id, A.name, month, year
            having
                C.artist_id = ?   
                
            UNION 
            select
                C.artist_id  id, A.name, month,  year, sum(play_count) as monthly_count
            from
                HISTORICAL_SONG_PLAY_COUNT SL, CREATES C, SONG S, ARTIST A
            where
                SL.song_id = C.song_id
                AND S.id = SL.song_id
                AND C.artist_id = A.id
            group by C.artist_id , A.name, month,  year
            having artist_id = ?
            order by year desc , month desc           
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
        statement.setLong(2, artistId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Artist(resultSet.getLong("id"), resultSet.getString("name")),
                            resultSet.getInt("month"),
                            resultSet.getInt("year"),
                            resultSet.getLong("monthly_count")
                    )
            );
        }
        return stats;
    }
    public List<Stats> getAlbumPlayCountByMonth(Connection connection, long albumId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
            select
                SA.album_id id, A.name, MONTH(SL.timestamp) as month, YEAR(SL.timestamp) as year,
                count(*) as monthly_count
            from
                SONG_LISTEN SL, SONG S, ALBUM A, SONG_ALBUM SA
            where
                SL.song_id = S.id
                AND S.id = SA.song_id
                AND SA.album_id = A.id
            group by
                SA.album_id, A.name, month, year
            having
                SA.album_id = ?   
            UNION
            select
                SA.album_id id, A.name, month,  year, sum(play_count) as monthly_count
            from
                HISTORICAL_SONG_PLAY_COUNT SL, SONG_ALBUM SA, SONG S, ALBUM A
            where
                SL.song_id = SA.song_id
                AND SA.song_id = S.id
                AND SA.album_id = A.id
            group by SA.album_id, A.name, month, year
            having album_id = ?
            order by year desc , month desc           
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, albumId);
        statement.setLong(2, albumId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Album(resultSet.getLong("id"), resultSet.getString("name")),
                            resultSet.getInt("month"),
                            resultSet.getInt("year"),
                            resultSet.getLong("monthly_count")
                    )
            );
        }
        return stats;
    }
    public Optional<Double> generatePaymentReport(Connection connection, PaymentReportInput input) throws SQLException {
        String query = null;
        PreparedStatement statement;
        ResultSet rs;
        switch (input.getStakeholder()){
            case ARTIST -> {
                query = "SELECT SUM(amount) as amount FROM ARTIST_PAY WHERE ARTIST_ID = ? AND TIMESTAMP BETWEEN ? AND ?";
            }
            case RECORD_LABEL -> {
                query = "SELECT SUM(amount) as amount FROM RL_PAY WHERE RECORD_LABEL_ID = ? AND TIMESTAMP BETWEEN ? AND ?";
            }

            case PODCAST_HOST -> {
                query = "SELECT SUM(amount) as amount FROM HOST_PAY WHERE HOST_ID = ? AND TIMESTAMP BETWEEN ? AND ?";
            }
        }
        return fetchTotalPaymentAmount(connection, input, query);
    }

    private Optional<Double> fetchTotalPaymentAmount(Connection connection, PaymentReportInput input, String query) throws SQLException {
        PreparedStatement statement;
        ResultSet rs;
        if(query == null){
            return  Optional.empty();
        }
        statement = connection.prepareStatement(query);
        statement.setLong(1, input.getId());
        statement.setDate(2, input.getStartDate());
        statement.setDate(3, input.getEndDate());
        rs = statement.executeQuery();
        if(rs.next()){
            return Optional.of(rs.getDouble("amount"));
        }
        return Optional.empty();
    }
    public List<RevenueStats> getMonthlyRevenueForService(Connection connection, long id) throws SQLException {
        List<RevenueStats> revenueStats = new ArrayList<>();
        String query = """
                select
                    S.id, S.name, MONTH(E.timestamp) month ,YEAR(E.timestamp) year,
                    SUM(amount) as revenue
                from
                    SERVICE S, EARNS E
                where
                        S.id = E.service_id
                group by
                    S.id, S.name, month, year
                having
                        S.id = ?
                order by year desc , month desc 
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            revenueStats.add(
                    new RevenueStats(
                            new Service(rs.getLong("id"), rs.getString("name")),
                            rs.getInt("month"),
                            rs.getInt("year"),
                            rs.getDouble("revenue")
                    )
            );
        }
        return revenueStats;
    }
    public List<RevenueStats> getYearlyRevenueForService(Connection connection, long id) throws SQLException {
        List<RevenueStats> revenueStats = new ArrayList<>();
        String query = """
                select
                    S.id, S.name,YEAR(E.timestamp) year,
                    SUM(amount) as revenue
                from
                    SERVICE S, EARNS E
                where
                        S.id = E.service_id
                group by
                    S.id, S.name, year
                having
                        S.id = ?
                order by year desc  
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            revenueStats.add(
                    new RevenueStats(
                            new Service(rs.getLong("id"), rs.getString("name")),
                            rs.getInt("year"),
                            rs.getDouble("revenue")
                    )
            );
        }
        return revenueStats;
    }
}
