package info;

import models.*;
import org.checkerframework.checker.units.qual.A;
import utils.ReportUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class Read {
    private static final ReportUtils reportUtils = new ReportUtils();
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

        try (PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement artistStatement = connection.prepareStatement(artistQuery);
            PreparedStatement rlStatement = connection.prepareStatement(rlQuery);
            PreparedStatement albumStatement = connection.prepareStatement(albumQuery)) {
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

    public void  getAlbum(long id, Connection connection) throws SQLException {
        String query = "SELECT id, name, edition, release_date FROM ALBUM WHERE ALBUM.id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("Album not found.");
            return;
        }
        Album album = new Album(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("release_date"),
            resultSet.getInt("edition")
        );
        System.out.println("Album");
        System.out.println(album);
        System.out.println("Songs in Album: ");
        reportUtils.getSongsByAlbum(connection, id).forEach(System.out::println);
    }

    public Optional<Artist> getArtist(Connection connection, long id) throws SQLException {
        Artist artist = new Artist();
        String query = "SELECT A.id, A.name, A.country, A.status, PG.genre_id genre_id, G.name genre_name, RL.name record_label_name, RL.id record_label_id " +
                "FROM ARTIST A , PRIMARY_GENRE PG, GENRE G , RECORD_LABEL RL, SIGNS S " +
                "WHERE A.id = ?" +
                " AND G.id = PG.genre_id AND PG.artist_id = A.id " +
                " AND RL.id = S.record_label_id AND S.artist_id = A.id";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        statement.executeQuery();
        ResultSet resultSet = statement.getResultSet();
        if (!resultSet.next()) {
            return Optional.empty();
        }
        artist.setId(resultSet.getLong("id"));
        artist.setName(resultSet.getString("name"));
        artist.setCountry(resultSet.getString("country"));
        artist.setStatus(Artist.ArtistStatus.valueOf(resultSet.getString("status")));
        artist.setPrimaryGenre(new Genre( resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
        artist.setRecordLabel(new RecordLabel(resultSet.getLong("record_label_id"), resultSet.getString("record_label_name")));

        String typeQuery = "SELECT id, name FROM ARTIST_TYPE AT, ARTIST_IS AI" +
                " WHERE AT.id = AI.artist_type_id AND AI.artist_id = ?" ;
        PreparedStatement typeStatement = connection.prepareStatement(typeQuery);
        typeStatement.setLong(1, id);
        ResultSet typeRs = typeStatement.executeQuery();
        List<ArtistType> ats = new ArrayList<>();
        while(typeRs.next()){
            ats.add(new ArtistType(typeRs.getLong("id"), typeRs.getString("name")));
        }
        artist.setTypes(ats);
        return Optional.of(artist);
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
    public Optional<ArtistType> getArtistTypeByName(Connection connection, String name) throws SQLException {
        String query = "SELECT id, name FROM ARTIST_TYPE WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(new ArtistType(resultSet.getLong("id"), resultSet.getString("name")));
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
        String query = "SELECT PODCAST.id, PODCAST.name, language, country, flat_fee, HOST.id, " +
                "HOST.first_name, HOST.last_name," +
                "RATING.rating, " +
                "SUBS.subCount " +
                "FROM PODCAST " +
                "JOIN PODCAST_HOST ON PODCAST.id = PODCAST_HOST.podcast_id " +
                "JOIN HOST ON HOST.id = PODCAST_HOST.host_id " +
                "CROSS JOIN (SELECT avg(rating) rating from RATES where podcast_id = ?) as RATING " +
                "CROSS JOIN (SELECT count(user_id) as subCount from PODCAST_SUBSCRIPTION where podcast_id = ?) AS SUBS " +
                "WHERE PODCAST.id = ?";
        String mapPodcastSponsorsQuery = "SELECT PS.sponsor_id, S.name " +
                "FROM PODCAST P " +
                "JOIN PODCAST_SPONSOR PS on P.id = PS.podcast_id " +
                "JOIN SPONSOR S on S.id = PS.sponsor_id " +
                "WHERE P.id = ?";
        String mapPodcastEpisodesQuery = "SELECT episode_num, title " +
                "FROM PODCAST P " +
                "JOIN EPISODE E on P.id = E.podcast_id " +
                "WHERE P.id = ?";
        String mapPodcastGenreQuery = "SELECT G.name genre " +
                "FROM PODCAST_GENRE " +
                "JOIN GENRE G on G.id = PODCAST_GENRE.genre_id " +
                "WHERE podcast_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.setLong(3, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            HashSet<Host> hosts = new HashSet<>();
            HashSet<Genre> genres = new HashSet<>();
            HashSet<Sponsor> sponsors = new HashSet<>();
            HashSet<Episode> episodes = new HashSet<>();

            PreparedStatement mapPodcastSponsorsStatement = connection.prepareStatement(mapPodcastSponsorsQuery);
            PreparedStatement mapPodcastEpisodesStatement = connection.prepareStatement(mapPodcastEpisodesQuery);
            PreparedStatement mapPodcastGenreStatement = connection.prepareStatement(mapPodcastGenreQuery);
            mapPodcastSponsorsStatement.setLong(1, id);
            mapPodcastEpisodesStatement.setLong(1, id);
            mapPodcastGenreStatement.setLong(1, id);

            ResultSet mapPodcastSponsorsResultSet = mapPodcastSponsorsStatement.executeQuery();
            ResultSet mapPodcastEpisodesResultSet = mapPodcastEpisodesStatement.executeQuery();
            ResultSet mapPodcastGenreResultSet = mapPodcastGenreStatement.executeQuery();

            while (resultSet.next()) {
                hosts.add(
                        new Host(
                                resultSet.getLong("HOST.id"),
                                resultSet.getString("HOST.first_name"),
                                resultSet.getString("HOST.first_name")
                        )
                );
            }
            while (mapPodcastSponsorsResultSet.next()){
                sponsors.add(new Sponsor(mapPodcastSponsorsResultSet.getLong("sponsor_id"),
                        mapPodcastSponsorsResultSet.getString("name")));
            }
            while (mapPodcastEpisodesResultSet.next()){
                episodes.add(new Episode(new Podcast(id), mapPodcastEpisodesResultSet.getLong("episode_num"),
                        mapPodcastEpisodesResultSet.getString("title")));
            }
            while (mapPodcastGenreResultSet.next()){
                genres.add(new Genre(mapPodcastGenreResultSet.getString("genre")));
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
                                resultSet.getDouble("rating"),
                                resultSet.getLong("subCount"),
                                hosts.stream().toList(),
                                sponsors.stream().toList(),
                                genres.stream().toList(),
                                episodes.stream().toList()
                        ));
            }
            return Optional.empty();
        }
    }

    public Optional<Episode> getEpisode(Connection connection, Long podcastID, Long episodeNum) throws SQLException {
        String query = "SELECT E.podcast_id, P.name, E.episode_num, E.title, E.release_date, E.duration, E.adv_count, E.bonus_rate, E.episode_id from EPISODE E, PODCAST P WHERE E.podcast_id = P.id AND E.podcast_id=? AND E.episode_num=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastID);
            statement.setLong(2, episodeNum);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return Optional.of(
                        new Episode(
                                new Podcast(resultSet.getLong("podcast_id"),resultSet.getString("name")),
                                resultSet.getLong("episode_num"),
                                resultSet.getString("title"),
                                resultSet.getDate("release_date"),
                                resultSet.getDouble("duration"),
                                resultSet.getInt("adv_count"),
                                resultSet.getDouble("bonus_rate"),
                                resultSet.getString("episode_id")
                        ));
            }
            return Optional.empty();
        }
    }

    public Optional<Guest> getGuestDetails(Connection connection, Long podcastID, Long episodeNum) throws SQLException {
        String query = "SELECT * from GUEST g where g.id = (Select EG.guest_id from EPISODE_GUEST EG where EG.podcast_id = ? and EG.episode_num = ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastID);
            statement.setLong(2, episodeNum);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return Optional.of(
                        new Guest(
                                resultSet.getString("name")
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
        String query = "SELECT E.podcast_id, P.name, E.episode_num, E.title, E.release_date, E.duration,  E.adv_count, E.bonus_rate, E.episode_id from EPISODE E, PODCAST P WHERE E.podcast_id = P.id AND E.podcast_id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);
        List<Episode> episodes = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            episodes.add(new Episode(
                            new Podcast(resultSet.getLong("podcast_id"), resultSet.getString("name")),
                            resultSet.getLong("episode_num"),
                            resultSet.getString("title"),
                            resultSet.getDate("release_date"),
                            resultSet.getDouble("duration"),
                            resultSet.getInt("adv_count"),
                            resultSet.getDouble("bonus_rate"),
                            resultSet.getString("episode_id")
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
    public List<Guest> getAllGuests(Connection connection) throws SQLException {
        String query = "SELECT id,name from GUEST";
        List<Guest> guests = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            guests.add(new Guest(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return guests;
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
        String query = "SELECT id,f_name, l_name, premium_status, monthly_premium_fees, phone, email, reg_date from USER";
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getLong("id"),
                    resultSet.getString("f_name"),
                    resultSet.getString("l_name"),
                    resultSet.getDate("reg_date"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getBoolean("premium_status"),
                    resultSet.getDouble("monthly_premium_fees")
                    )
            );
        }
        return users;
    }

    public Optional<User> getUserById(Connection connection, long id) throws SQLException {
        String query = "SELECT id,f_name, l_name, premium_status, monthly_premium_fees, reg_date, phone, email FROM USER WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(
                    new User(
                            resultSet.getLong("id"),
                            resultSet.getString("f_name"),
                            resultSet.getString("l_name"),
                            resultSet.getDate("reg_date"),
                            resultSet.getString("phone"),
                            resultSet.getString("email"),
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

    public Optional<Long> getMaxEpisodeNumForPodcast(Connection connection, long podcastId) throws SQLException {
        String query = "SELECT max(episode_num) as episodeNum From EPISODE WHERE podcast_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, podcastId);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of((resultSet.getLong("episodeNum")));
        }
        return Optional.empty();
    }

    public void displayRecordLabelById(Connection connection, long id) throws SQLException {
        String query = "SELECT ID,NAME FROM RECORD_LABEL WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        if(!rs.next()){
            System.out.println("Record Label Not Found");
        }
        RecordLabel recordLabel = new RecordLabel(rs.getLong("id"), rs.getString("name"));
        System.out.println("Record Label Details:");
        System.out.println(recordLabel);

        String ownsQuery = "SELECT S.id, S.title FROM SONG S, OWNS O WHERE S.id = O.song_id and O.record_label_id = ?";
        List<Song> ownedSongs = new ArrayList<>();
        PreparedStatement ownsStatement = connection.prepareStatement(ownsQuery);
        ownsStatement.setLong(1, id);
        ResultSet ownsRs = ownsStatement.executeQuery();
        while(ownsRs.next()){
            ownedSongs.add(
                    new Song(ownsRs.getLong("id"), ownsRs.getString("title"))
            );
        }
        System.out.println("Songs Owned:");
        ownedSongs.forEach(System.out::println);

        String signedArtistQuery = "SELECT A.id, A.name FROM ARTIST A, SIGNS S WHERE S.artist_id = A.id and S.record_label_id = ?";
        List<Artist> signedArtists = new ArrayList<>();
        PreparedStatement signsStatement = connection.prepareStatement(signedArtistQuery);
        signsStatement.setLong(1, id);
        ResultSet signsRs = signsStatement.executeQuery();
        while(signsRs.next()){
            signedArtists.add(
                    new Artist(signsRs.getLong("id"), signsRs.getString("name"))
            );
        }
        System.out.println("Artists Signed:");
        signedArtists.forEach(System.out::println);

        statement.close();
        ownsStatement.close();
        signsStatement.close();
    }
    public List<Sponsor> getAllSponsors(Connection connection) throws SQLException {
        String query = "SELECT id,name from SPONSOR";
        List<Sponsor> sponsors = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            sponsors.add(new Sponsor(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                    )
            );
        }
        return sponsors;
    }
    public List<Podcast> getPodcastsByHostId(Connection connection, long hostId) throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        String query = "SELECT P.id, P.name from PODCAST P, PODCAST_HOST PH WHERE P.id = PH.podcast_id AND PH.host_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, hostId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            podcasts.add(
                    new Podcast(rs.getLong("id"), rs.getString("name"))
            );
        }
        return podcasts;
    }

}
