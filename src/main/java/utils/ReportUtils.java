package utils;

import models.Artist;
import models.Song;
import models.Stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<Stats> getArtistPlayCountByMonth(Connection connection, long artistId) throws SQLException {
        List<Stats> stats = new ArrayList<>();
        String query = """
            select
                C.artist_id, A.name, MONTH(SL.timestamp) as month, YEAR(SL.timestamp) as year,
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
            order by year desc , month desc           
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, artistId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Artist(resultSet.getLong("id"), resultSet.getString("title")),
                            resultSet.getInt("month"),
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
                C.artist_id, A.name, MONTH(SL.timestamp) as month, YEAR(SL.timestamp) as year,
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
            order by year desc , month desc           
            """;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, albumId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            stats.add(
                    new Stats(
                            new Artist(resultSet.getLong("id"), resultSet.getString("title")),
                            resultSet.getInt("month"),
                            resultSet.getLong("monthly_count")
                    )
            );
        }
        return stats;
    }
}
