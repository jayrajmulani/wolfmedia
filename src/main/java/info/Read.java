package info;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class Read {
    public Optional<Song> getSong(long id, Connection connection) throws SQLException {
        String query = "SELECT S.id, title, release_country, language, duration, " +
                "royalty_rate, royalty_paid, S.release_date,  " +
                "G.name genre, G.id AS genre_id " +
                "FROM SONG S " +
                "JOIN SONG_GENRE SG on S.id = SG.song_id " +
                "JOIN GENRE G on G.id = SG.genre_id " +
                "WHERE S.id = ? ";

        String artistQuery = "SELECT S.id, " +
                "C.artist_id, A.name artist_name, C.is_collaborator " +
                "FROM SONG S " +
                "JOIN CREATES C on S.id = C.song_id " +
                "JOIN ARTIST A on A.id = C.artist_id " +
                "WHERE S.id = ? ";
        String rlQuery = "SELECT record_label_id, RL.name rl_name " +
                "FROM OWNS " +
                "JOIN RECORD_LABEL RL on RL.id = OWNS.record_label_id " +
                "WHERE song_id = ?";
        String albumQuery = "SELECT SA.album_id, A.name album_name " +
                "FROM SONG_ALBUM SA " +
                "JOIN ALBUM A on A.id = SA.album_id " +
                "WHERE song_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            PreparedStatement artistStatement = connection.prepareStatement(artistQuery);
            PreparedStatement rlStatement = connection.prepareStatement(rlQuery);
            PreparedStatement albumStatement = connection.prepareStatement(albumQuery);

            statement.setLong(1, id);
            artistStatement.setLong(1, id);
            rlStatement.setLong(1, id);
            albumStatement.setLong(1, id);
            statement.executeQuery();
            artistStatement.executeQuery();
            rlStatement.executeQuery();
            albumStatement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            ResultSet artistResultSet = artistStatement.getResultSet();
            ResultSet rlResultSet = rlStatement.getResultSet();
            ResultSet albumResultSet = albumStatement.getResultSet();
            HashSet<Genre> genres = new HashSet<>();
            HashSet<Artist> artists = new HashSet<>();
            HashSet<Artist> collaborators = new HashSet<>();


            while (resultSet.next()) {
                genres.add(new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre")));
            }
            while (artistResultSet.next()) {
                Artist tmpArtist = new Artist(artistResultSet.getLong("artist_id"),
                        artistResultSet.getString("artist_name"));
                if (artistResultSet.getBoolean("is_collaborator")) {
                    collaborators.add(tmpArtist);
                } else {
                    artists.add(tmpArtist);
                }
            }
            resultSet.beforeFirst();
            artistResultSet.beforeFirst();
            if (resultSet.next()) {
                artistResultSet.next();
                rlResultSet.next();
                Album album = new Album();
                if (albumResultSet.next()) {
                    album = new Album(albumResultSet.getLong("album_id"),
                            albumResultSet.getString("album_name"));
                }
                return Optional.of(
                        new Song(resultSet.getLong("id"), resultSet.getString("title"),
                                resultSet.getString("release_country"), resultSet.getString("language"),
                                resultSet.getFloat("duration"), resultSet.getFloat("royalty_rate"),
                                resultSet.getDate("release_date"), resultSet.getBoolean("royalty_paid"),
                                0,
                                genres.stream().toList(),
                                album,
                                artists.stream().toList(),
                                collaborators.stream().toList(),
                                new RecordLabel(rlResultSet.getLong("record_label_id"), rlResultSet.getString("rl_name"))

                        ));
            }
            return Optional.empty();
        }
    }

    public Optional<Guest> getGuest(long id, Connection connection) throws SQLException {
        String query = "SELECT id, name FROM GUEST WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new Guest(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }

    public Optional<Sponsor> getSponsor(long id, Connection connection) throws SQLException {
        String query = "SELECT id, name FROM SPONSOR WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new Sponsor(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }

    public Optional<Genre> getGenreByName(Connection connection, String name) throws SQLException {
        String query = "SELECT id, name FROM GENRE WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new Genre(resultSet.getLong("id"), resultSet.getString("name")));
            }
            return Optional.empty();
        }
    }

    public Optional<Host> getHost(Long id, Connection connection) throws SQLException {
        String query = "SELECT HOST.id, first_name, last_name, city, email, phone " +
                "FROM HOST WHERE HOST.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(
                        new Host(resultSet.getLong("id"), resultSet.getString("first_name"),
                                resultSet.getString("last_name"), resultSet.getString("city"),
                                resultSet.getString("email"), resultSet.getString("phone")
                        ));
            }
            return Optional.empty();
        }

    }

    public Optional<Podcast> getPodcast(Long id, Connection connection) throws SQLException {
        String query = "SELECT PODCAST.id, name, language, country, flat_fee, HOST.id, " +
                " HOST.first_name, HOST.last_name " +
                " FROM PODCAST " +
                " JOIN PODCAST_HOST ON PODCAST.id = PODCAST_HOST.podcast_id" +
                " JOIN HOST ON HOST.id = PODCAST_HOST.host_id" +
                " WHERE PODCAST.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            List<Host> hosts = new ArrayList<>();
            while (resultSet.next()) {
                hosts.add(
                        new Host(
                                resultSet.getLong("HOST.id"),
                                resultSet.getString("HOST.first_name"),
                                resultSet.getString("HOST.first_name")
                        )
                );
            }
            resultSet.beforeFirst();
            if (resultSet.next()) {
                return Optional.of(
                        new Podcast(
                                resultSet.getLong("PODCAST.id"),
                                resultSet.getString("name"),
                                resultSet.getString("language"),
                                resultSet.getString("country"),
                                resultSet.getDouble("flat_fee"),
                                0, // TODO rating
                                0, // TODO sub count
                                hosts,
                                new ArrayList<Sponsor>(), // TODO sponsor
                                new ArrayList<Genre>(), // TODO
                                new ArrayList<Episode>() // TODO
                        ));
            }
            return Optional.empty();
        }
    }

    public Optional<Episode> getEpisode(Connection connection, Long podcastID, Long episodeNum) throws SQLException {
        String query = "SELECT * from EPISODE WHERE podcast_id=? AND episode_num=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastID);
            statement.setLong(2, episodeNum);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return Optional.of(
                        new Episode(
                                resultSet.getLong("podcast_id"),
                                resultSet.getLong("episode_num"),
                                resultSet.getString("title"),
                                resultSet.getDate("release_date"),
                                resultSet.getDouble("duration"),
                                resultSet.getInt("adv_count"),
                                resultSet.getDouble("bonus_rate")
                        ));
            }
            return Optional.empty();
        }
    }

    public List<Host> getAllHosts(Connection connection) throws SQLException {
        String query = "SELECT id, first_name, last_name from HOST";
        List<Host> hosts = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            hosts.add(new Host(
                            resultSet.getLong("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    )
            );
        }
        return hosts;
    }

    public List<Song> getAllSongs(Connection connection) throws SQLException {
        String query = "SELECT * from SONG";
        List<Song> songs = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            songs.add(new Song(
                            resultSet.getLong("id"),
                            resultSet.getString("title"),
                            resultSet.getString("release_country"),
                            resultSet.getString("language"),
                            resultSet.getDouble("duration"),
                            resultSet.getDouble("royalty_rate"),
                            resultSet.getDate("release_date"),
                            resultSet.getBoolean("royalty_paid"),
                            List.of()
                    )
            );
        }
        return songs;
    }

    public List<Podcast> getAllPodcasts(Connection connection) throws SQLException {
        String query = "SELECT * from PODCAST";
        List<Podcast> podcasts = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            podcasts.add(new Podcast(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country"),
                            resultSet.getString("language"),
                            resultSet.getDouble("flat_fee")
                    )
            );
        }
        return podcasts;
    }

    public List<Rates> getAllRates(Connection connection) throws SQLException {
        String query = "SELECT * from RATES";
        List<Rates> rates = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            rates.add(new Rates(
                            resultSet.getLong("user_id"),
                            resultSet.getLong("podcast_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getDate("updated_at")
                    )
            );
        }
        return rates;
    }

    public List<Episode> getAllPodcastEpisodes(Connection connection, long podcastId) throws SQLException {
        String query = "SELECT * from EPISODE WHERE podcast_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);
        List<Episode> episodes = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            episodes.add(new Episode(
                            resultSet.getLong("podcast_id"),
                            resultSet.getLong("episode_num"),
                            resultSet.getString("title"),
                            resultSet.getDate("release_date"),
                            resultSet.getDouble("duration"),
                            resultSet.getInt("adv_count"),
                            resultSet.getDouble("bonus_rate")
                    )
            );
        }
        return episodes;
    }

    public List<RecordLabel> getAllRecordLabels(Connection connection) throws SQLException {
        String query = "SELECT * from RECORD_LABEL";
        List<RecordLabel> recordLabels = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            recordLabels.add(new RecordLabel(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return recordLabels;
    }

    public List<Artist> getAllArtists(Connection connection) throws SQLException {
        String query = "SELECT id,name, country, status from ARTIST";
        List<Artist> artists = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            artists.add(new Artist(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("country"),
                            Artist.ArtistStatus.valueOf(resultSet.getString("status"))
                    )
            );
        }
        return artists;
    }

    public List<Album> getAllAlbums(Connection connection) throws SQLException {
        String query = "SELECT id,name,release_date,edition from ALBUM";
        List<Album> albums = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            albums.add(new Album(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getDate("release_date"),
                            resultSet.getInt("edition")
                    )
            );
        }
        return albums;
    }

    public List<Service> getAllServices(Connection connection) throws SQLException {
        String query = "SELECT id,name from SERVICE";
        List<Service> services = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            services.add(new Service(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return services;
    }

    public List<Owns> getAllOwns(Connection connection) throws SQLException {
        String query = "SELECT record_label_id, song_id from OWNS";
        List<Owns> owns = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            owns.add(new Owns(
                            resultSet.getLong("record_label_id"),
                            resultSet.getLong("song_id")
                    )
            );
        }
        return owns;
    }

    public List<SongAlbum> getAllSongAlbum(Connection connection) throws SQLException {
        String query = "SELECT song_id, album_id, track_num from SONG_ALBUM";
        List<SongAlbum> songAlbum = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            songAlbum.add(new SongAlbum(
                            new Song(resultSet.getLong("song_id")),
                            new Album(resultSet.getLong("album_id")),
                            resultSet.getLong("track_num")
                    )
            );
        }
        return songAlbum;
    }

    public List<User> getAllUsers(Connection connection) throws SQLException {
        String query = "SELECT id,f_name, l_name, premium_status, monthly_premium_fees from USER";
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            users.add(new User(
                            resultSet.getLong("id"),
                            resultSet.getString("f_name"),
                            resultSet.getString("l_name"),
                            resultSet.getBoolean("premium_status"),
                            resultSet.getDouble("monthly_premium_fees")
                    )
            );
        }
        return users;
    }

    public Optional<User> getUserById(Connection connection, long id) throws SQLException {
        String query = "SELECT id,f_name, l_name, premium_status, monthly_premium_fees FROM USER WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(
                    new User(
                            resultSet.getLong("id"),
                            resultSet.getString("f_name"),
                            resultSet.getString("l_name"),
                            resultSet.getBoolean("premium_status"),
                            resultSet.getDouble("monthly_premium_fees")
                    )
            );
        }
        return Optional.empty();
    }

    public Optional<Double> getRatingByPodcastIdUserId(Connection connection, long user_id, long podcast_id) throws SQLException {
        String query = "SELECT rating From RATES WHERE user_id = ? AND podcast_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, user_id);
        statement.setLong(2, podcast_id);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(resultSet.getDouble("rating"));
        }
        return Optional.empty();
    }

}
