package utils;

import models.*;
import org.checkerframework.checker.units.qual.A;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportUtils {

    public List<Stats> getHistoricalSongPlayCountByMonth(Connection connection, long songId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """ 
            select 
                s.id, s.title, month, year, 
                play_count as monthly_count
            from HISTORICAL_SONG_PLAY_COUNT , SONG s WHERE s.id = HISTORICAL_SONG_PLAY_COUNT.song_id 
            AND s.id = ?
            AND HISTORICAL_SONG_PLAY_COUNT.month <> MONTH(CURRENT_TIMESTAMP)
            AND HISTORICAL_SONG_PLAY_COUNT.year = YEAR(CURRENT_TIMESTAMP)
            order by year desc , month desc 
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, songId);
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
    public long getSongPlayCountForCurrentMonth(Connection connection, long songId) throws SQLException {
        String query = """ 
                select sum(play_count) count from
                    (    
                        (select count(*) play_count
                    from SONG_LISTEN
                    where song_id = ?)
                    union
                    (
                    select play_count
                    from HISTORICAL_SONG_PLAY_COUNT
                    where month = month(current_timestamp)
                    and year = year(current_timestamp)
                    and song_id = ?
                    )) as SLpcHSPCpc;     
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, songId);
        statement.setLong(2, songId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getLong("count");
        }
        return 0L;
    }
    public List<Stats> getHistoricalArtistPlayCount(Connection connection, long artistId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
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
            AND SL.month <> MONTH(CURRENT_TIMESTAMP)
            AND SL.year = YEAR(CURRENT_TIMESTAMP)
            order by year desc , month desc           
            """;
        return getArtistStatsByQuery(connection, artistId, stats, query);
    }

    private List<Stats> getArtistStatsByQuery(Connection connection, long artistId, List<Stats> stats, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
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
    public long getArtistPlayCountForCurrentMonth(Connection connection, long artistId) throws SQLException {
        String query = """
            select sum(play_count) count from
            (    (select count(*) play_count
            from SONG_LISTEN SL, CREATES C
            where SL.song_id = C.song_id
            AND C.artist_id = ?)
            union
            (
            select play_count
            from HISTORICAL_SONG_PLAY_COUNT HSPC, CREATES C
            where month = month(current_timestamp)
            and year = year(current_timestamp)
            and HSPC.song_id = C.song_id
            and C.artist_id = ?
            )) as SLpcHSPCpc
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
        statement.setLong(2, artistId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getLong("count");
        }
        return 0L;
    }
    public List<Stats> getHistoricalAlbumPlayCount(Connection connection, long albumId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
            select
                SA.album_id id, A.name, month,  year, sum(play_count) as monthly_count
            from
                HISTORICAL_SONG_PLAY_COUNT SL, SONG_ALBUM SA, SONG S, ALBUM A
            where
                SL.song_id = SA.song_id
                AND SA.song_id = S.id
                AND SA.album_id = A.id
            group by SA.album_id, A.name, month, year
            AND SL.month <> MONTH(CURRENT_TIMESTAMP)
            AND SL.year = YEAR(CURRENT_TIMESTAMP)
            having album_id = ?
            order by year desc , month desc     
            """;
        return getAlbumStatsByQuery(connection, albumId, stats, query);
    }
    public long getAlbumPlayCountForCurrentMonth(Connection connection, long albumId) throws SQLException {
        String query = """
            select sum(play_count) count from
            (    (select count(*) play_count
            from SONG_LISTEN SL, SONG_ALBUM SA
            where SL.song_id = SA.song_id
            AND SA.album_id = ?)
            union
            (
            select play_count
            from HISTORICAL_SONG_PLAY_COUNT HSPC, SONG_ALBUM SA
            where month = month(current_timestamp)
            and year = year(current_timestamp)
            and HSPC.song_id = SA.song_id
            and SA.album_id = ?
            )) as SLpcHSPCpc
           """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, albumId);
        statement.setLong(2, albumId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getLong("count");
        }
        return 0L;
    }

    private List<Stats> getAlbumStatsByQuery(Connection connection, long albumId, List<Stats> stats, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, albumId);
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
    public void flushSongPlayCountForCurrentMonth(Connection connection, long songId) throws SQLException {
        // Get play count for current month
        String query = "SELECT COUNT(*) play_count FROM SONG_LISTEN WHERE song_id = ? " +
                "AND MONTH(timestamp) = MONTH(CURRENT_TIMESTAMP) " +
                "AND YEAR(timestamp) = YEAR(CURRENT_TIMESTAMP)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, songId);
        ResultSet rs = statement.executeQuery();
        long playCount = 0;
        if(rs.next()){
            playCount = rs.getLong("play_count");
        }
        rs.close();
        if(playCount == 0){
            System.out.println("Nothing to flush. Play Count is 0.");
            return;
        }
        statement.close();
        // Push the value to historical play count (update if exists, else insert)
        query = "SELECT * FROM HISTORICAL_SONG_PLAY_COUNT WHERE song_id = ? " +
                "AND month = MONTH(CURRENT_TIMESTAMP)" +
                "AND year = YEAR(CURRENT_TIMESTAMP)";
        statement = connection.prepareStatement(query);
        statement.setLong(1, songId);
        ResultSet hRs = statement.executeQuery();
        if(hRs.next()){
            // Data was already present for current month, hence update
            System.out.println("Found existing data for this month, updating the same");
            String updateQuery = "UPDATE HISTORICAL_SONG_PLAY_COUNT SET play_count = play_count + ? " +
                    "WHERE song_id = ? AND month = month(CURRENT_TIMESTAMP) AND year = year(CURRENT_TIMESTAMP)";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setLong(1,playCount);
            updateStatement.setLong(2, songId);
            updateStatement.executeUpdate();
            updateStatement.close();
        }
        else{
            // Data was missing, hence insert
            String insertQuery = "INSERT INTO HISTORICAL_SONG_PLAY_COUNT(song_id, month, year, play_count) " +
                    " VALUES (? ,month(CURRENT_TIMESTAMP), year(CURRENT_TIMESTAMP), ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setLong(1, songId);
            insertStatement.setLong(2, playCount);
            insertStatement.executeUpdate();
            insertStatement.close();
        }

        // Flush the individual listen records
        String deleteQuery = "DELETE FROM SONG_LISTEN WHERE song_id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1,songId);
        deleteStatement.executeUpdate();
        deleteStatement.close();
    }
    public List<Song> getSongsByArtist(Connection connection, long artistId) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String query = """
        SELECT S.id, S.title
        FROM   CREATES C, SONG S
        WHERE  S.id = C.song_id AND C.artist_id = ?
        """;
        return getSongs(connection, artistId, songs, query);
    }

    private List<Song> getSongs(Connection connection, long artistId, List<Song> songs, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            songs.add(
                    new Song(rs.getLong("id"), rs.getString("title"))
            );
        }
        return songs;
    }

    public List<Song> getSongsByAlbum(Connection connection, long albumId) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String query = """
        SELECT S.id, S.title
        FROM   SONG_ALBUM SA, SONG S
        WHERE  S.id = SA.song_id AND SA.album_id = ?
        """;
        return getSongs(connection, albumId, songs, query);
    }
    public long getEpisodePlayCountForCurrentMonth(Connection connection, long podcastId, long episodeNum) throws SQLException {
        String query = """ 
                select sum(play_count) count from
                    (    
                        (select count(*) play_count
                    from PODCAST_EP_LISTEN
                    where podcast_id = ? AND episode_num = ?)
                    union
                    (
                    select play_count
                    from HISTORICAL_EPISODE_PLAY_COUNT
                    where month = month(current_timestamp)
                    and year = year(current_timestamp)
                    and podcast_id = ?
                    and episode_num = ?
                    )) as SLpcHSPCpc;     
                """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);
        statement.setLong(2, episodeNum);
        statement.setLong(3, podcastId);
        statement.setLong(4, episodeNum);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            return rs.getLong("count");
        }
        return 0L;
    }
    public void flushEpisodePlayCountForCurrentMonth
            (Connection connection, long podcastId, long episodeNum) throws SQLException {
        // Begin Transaction
        connection.setAutoCommit(false);
        try {
            // Get play count for current month
            String query = "SELECT COUNT(*) play_count FROM " +
                    "PODCAST_EP_LISTEN WHERE podcast_id = ? AND episode_num = ? " +
                    "AND MONTH(timestamp) = MONTH(CURRENT_TIMESTAMP) " +
                    "AND YEAR(timestamp) = YEAR(CURRENT_TIMESTAMP)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, podcastId);
            statement.setLong(1, episodeNum);
            ResultSet rs = statement.executeQuery();
            long playCount = 0;
            if (rs.next()) {
                playCount = rs.getLong("play_count");
            }
            rs.close();
            if (playCount == 0) {
                System.out.println("Nothing to flush. Play Count is 0.");
                return;
            }
            statement.close();
            // Push the value to historical play count (update if exists, else insert)
            query = "SELECT * FROM " +
                    "HISTORICAL_EPISODE_PLAY_COUNT WHERE podcast_id = ? AND " +
                    "episode_num = ? " +
                    "AND month = MONTH(CURRENT_TIMESTAMP)" +
                    "AND year = YEAR(CURRENT_TIMESTAMP)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, podcastId);
            statement.setLong(1, episodeNum);
            ResultSet hRs = statement.executeQuery();
            if (hRs.next()) {
                // Data was already present for current month, hence update
                System.out.println("Found existing data for this month, updating the same");
                String updateQuery = "UPDATE HISTORICAL_EPISODE_PLAY_COUNT " +
                        "SET play_count = play_count + ? " +
                        "WHERE podcast_id = ? AND episode_num = ? " +
                        "AND month = month(CURRENT_TIMESTAMP) AND year = year(CURRENT_TIMESTAMP)";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setLong(1, playCount);
                updateStatement.setLong(2, podcastId);
                updateStatement.setLong(3, episodeNum);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // Data was missing, hence insert
                String insertQuery = "INSERT INTO " +
                        "HISTORICAL_EPISODE_PLAY_COUNT" +
                        "(podcast_id, episode_num, month, year, play_count) " +
                        " VALUES (? ,?, month(CURRENT_TIMESTAMP), year(CURRENT_TIMESTAMP), ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setLong(1, podcastId);
                insertStatement.setLong(2, episodeNum);
                insertStatement.setLong(3, playCount);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            // Flush the individual listen records
            String deleteQuery = "DELETE FROM PODCAST_EP_LISTEN " +
                    "WHERE podcast_id = ? AND episode_num = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setLong(1, podcastId);
            deleteStatement.setLong(1, episodeNum);
            deleteStatement.executeUpdate();
            deleteStatement.close();

            // Commit changes
            connection.commit();
        }catch (SQLException e){
            connection.rollback();
            e.printStackTrace();
        }finally {
            // End Transaction
            connection.setAutoCommit(true);
        }
    }
    public List<Stats> getHistoricalEpisodePlayCountByMonth(Connection connection, long podcastId, long episodeNum) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """ 
            select 
                p.id, p.name podcast_name , e.episode_num, e.title episode_title, month, year, 
                play_count as monthly_count
            from HISTORICAL_EPISODE_PLAY_COUNT , EPISODE e, PODCAST p 
            WHERE e.podcast_id = HISTORICAL_EPISODE_PLAY_COUNT.podcast_id
            AND e.episode_num = HISTORICAL_EPISODE_PLAY_COUNT.episode_num 
            AND e.podcast_id = ?
            AND e.episode_num = ?
            AND HISTORICAL_EPISODE_PLAY_COUNT.month <> MONTH(CURRENT_TIMESTAMP)
            AND HISTORICAL_EPISODE_PLAY_COUNT.year = YEAR(CURRENT_TIMESTAMP)
            order by year desc , month desc 
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);
        statement.setLong(2, episodeNum);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Episode(
                                    new Podcast(resultSet.getLong("id"), resultSet.getString("podcast_name")),
                                    resultSet.getLong("episode_num"),
                                    resultSet.getString("episode_title")
                            ),
                            resultSet.getInt("month"),
                            resultSet.getInt("year"),
                            resultSet.getLong("monthly_count")
                    )
            );
        }
        return stats;
    }
}
