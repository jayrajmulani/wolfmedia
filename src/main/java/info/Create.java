package info;

import models.*;
import utils.DB;
import utils.ReportUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class Create {
    private static final Read read = new Read();
    private static final ReportUtils reportUtils = new ReportUtils();

    public long createSong(Connection connection, SongAlbum songAlbum) throws SQLException {
        // Start a Transaction
        connection.setAutoCommit(false);
        Song song = songAlbum.getSong();

        String query = "insert into SONG (title, release_country, language, duration, royalty_rate, release_date, royalty_paid) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, song.getTitle());
            statement.setString(2, song.getReleaseCountry());
            statement.setString(3, song.getLanguage());
            statement.setDouble(4, song.getDuration());
            statement.setDouble(5, song.getRoyaltyRate());
            statement.setDate(6, song.getReleaseDate());
            statement.setBoolean(7, song.isRoyaltyPaid());
            long songId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                songId = rs.getLong(1);
                if (songAlbum.getAlbum() != null) {
                    String mapSongAlbumQuery = "INSERT INTO SONG_ALBUM(song_id, album_id, track_num) values (?,?,?)";

                    try {
                        PreparedStatement mapSongAlbumStatement = connection.prepareStatement(mapSongAlbumQuery);
                        mapSongAlbumStatement.setLong(1, songId);
                        mapSongAlbumStatement.setLong(2, songAlbum.getAlbum().getId());
                        mapSongAlbumStatement.setLong(3, songAlbum.getTrackNum());
                        mapSongAlbumStatement.executeQuery();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                song.getGenres().forEach(inputGenre -> {
                    try {
                        Optional<Genre> genre = read.getGenreByName(connection, inputGenre.getName());
                        long genreId = genre.isPresent() ? genre.get().getId() : createGenre(connection, inputGenre);
                        String mapSongGenreQuery = "INSERT INTO SONG_GENRE(song_id, genre_id) values (?,?)";
                        PreparedStatement mapSongGenreStatement = connection.prepareStatement(mapSongGenreQuery);
                        mapSongGenreStatement.setLong(1, songId);
                        mapSongGenreStatement.setLong(2, genreId);
                        mapSongGenreStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                song.getArtists().forEach(artist -> {
                    try {
                        String mapSongArtistQuery = "INSERT INTO CREATES(song_id, artist_id) values (?,?)";
                        PreparedStatement mapSongArtistStatement = connection.prepareStatement(mapSongArtistQuery);
                        mapSongArtistStatement.setLong(1, songId);
                        mapSongArtistStatement.setLong(2, artist.getId());
                        mapSongArtistStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                song.getCollaborators().forEach(artist -> {
                    try {
                        String mapSongArtistQuery = "INSERT INTO CREATES(song_id, artist_id, is_collaborator) values (?,?,1)";
                        PreparedStatement mapSongArtistStatement = connection.prepareStatement(mapSongArtistQuery);
                        mapSongArtistStatement.setLong(1, songId);
                        mapSongArtistStatement.setLong(2, artist.getId());
                        mapSongArtistStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                String mapSongRLQuery = "INSERT INTO OWNS(song_id, record_label_id) " +
                        "SELECT ? AS song_id, record_label_id " +
                        "from SIGNS " +
                        "WHERE artist_id = ?" ;
                PreparedStatement mapSongRLStatement = connection.prepareStatement(mapSongRLQuery);
                mapSongRLStatement.setLong(1, songId);
                mapSongRLStatement.setLong(2, song.getArtists().get(0).getId());
                mapSongRLStatement.executeUpdate();
                connection.commit();
            } else {
                songId = 0L;
            }
            return songId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } finally {
            connection.setAutoCommit(true);
        }
        return 0L;
    }

    public long createUser(Connection connection, User user) {
        String query = "insert into USER (f_name, l_name, phone, email, reg_date, premium_status, monthly_premium_fees) values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFName());
            statement.setString(2, user.getLName());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getEmail());
            statement.setDate(5, (Date) user.getRegDate());
            statement.setBoolean(6, user.getPremiumStatus());
            statement.setDouble(7, user.getMonthlyPremiumFees());

            long userId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                userId = rs.getLong(1);
            } else
                userId = 0L;
            return userId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createGuest(Connection connection, Guest guest) {
        String query = "INSERT INTO GUEST(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, guest.getName());
    }

    public long createGenre(Connection connection, Genre genre) {
        String query = "INSERT INTO GENRE(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, genre.getName());
    }

    public long createSponsor(Connection connection, Sponsor sponsor) {
        String query = "INSERT INTO SPONSOR(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, sponsor.getName());
    }

    public long createRecordLabel(Connection connection, RecordLabel recordLabel) {
        String query = "insert into RECORD_LABEL (name) values (?);";
        return insertAndGetIdForSingleNameColumnTables(connection, query, recordLabel.getName());
    }

    public long createService(Connection connection, Service service)  {
        String query = "insert into SERVICE(name, balance) values (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, service.getName());
            statement.setDouble(2, service.getBalance());

            long userId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                userId = rs.getLong(1);
            } else
                userId = 0L;
            return userId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createArtistType(Connection connection, ArtistType artistType) {
        String query = "INSERT INTO ARTIST_TYPE(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, artistType.getName());
    }

    public long createHost(Connection connection, Host host) throws SQLException {
        String query = "INSERT INTO HOST(first_name, last_name, city, email, phone) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, host.getFirstName());
            statement.setString(2, host.getLastName());
            statement.setString(3, host.getCity());
            statement.setString(4, host.getEmail());
            statement.setString(5, host.getPhone());
            long hostID;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                hostID = rs.getInt(1);
            } else {
                hostID = 0L;
            }
            return hostID;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createPodcast(Connection connection, Podcast podcast) {
        String query = "INSERT INTO PODCAST(name, country, language, flat_fee) VALUES (?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, podcast.getName());
            statement.setString(2, podcast.getCountry());
            statement.setString(3, podcast.getLanguage());
            statement.setDouble(4, podcast.getFlatFee());
            long podcastID;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                podcastID = rs.getInt(1);
                podcast.getHosts().forEach(inputHost -> {
                    try {
                        Optional<Host> host = read.getHost(inputHost.getId(), connection);
                        long hostID = host.isPresent() ? host.get().getId() : createHost(connection, inputHost);
                        String mapPodcastHost = "INSERT INTO PODCAST_HOST(podcast_id, host_id) values (?,?)";
                        PreparedStatement mapPodcastHostStatement = connection.prepareStatement(mapPodcastHost);
                        mapPodcastHostStatement.setLong(1, podcastID);
                        mapPodcastHostStatement.setLong(2, hostID);
                        mapPodcastHostStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                podcastID = 0L;
            }
            return podcastID;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createEpisode(Connection connection, Episode episode){
        String query = "INSERT INTO EPISODE(podcast_id, episode_num, title, release_date, duration, adv_count, " +
                "bonus_rate) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, episode.getPodcast().getId());
            statement.setLong(2, episode.getEpisodeNum());
            statement.setString(3, episode.getTitle());
            statement.setDate(4, episode.getReleaseDate());
            statement.setDouble(5, episode.getDuration());
            statement.setInt(6, episode.getAdvCount());
            statement.setDouble(7, episode.getBonusRate());
            if (statement.executeUpdate() > 0) {
                return episode.getEpisodeNum();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createArtist(Connection connection, Artist artist) throws SQLException {
        // Start a Transaction
        connection.setAutoCommit(false);

//        types, genre, recordlabel

        String query = "insert into ARTIST (name, country, status) values (?, ?, ?)";
        String mapArtistGenreQuery = "INSERT INTO PRIMARY_GENRE(artist_id, genre_id) values (?,?)";
        String mapArtistRLQuery = "INSERT INTO SIGNS(artist_id, record_label_id, updated_at) " +
                "VALUES (?,?,CURRENT_TIMESTAMP)";
        String mapArtistISQuery = "INSERT INTO ARTIST_IS(artist_id, artist_type_id) VALUES (?,?)";


        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getCountry());
            statement.setString(3, artist.getStatus().getStatus());
            long artistId;

            Optional<Genre> genre = read.getGenreByName(connection, artist.getPrimaryGenre().getName());
            long genreId = genre.map(Genre::getId).orElseGet(() -> createGenre(connection, artist.getPrimaryGenre()));

            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                artistId = rs.getInt(1);

                artist.getTypes().forEach(artistType -> {
                    try {
                        Optional<ArtistType> artistTypeOptional = read.getArtistTypeByName(connection, artistType.getName());
                        long artistTypeID = artistTypeOptional.isPresent() ? artistTypeOptional.get().getId() : createArtistType(connection, artistType);
                        PreparedStatement mapSongGenreStatement = connection.prepareStatement(mapArtistISQuery);
                        mapSongGenreStatement.setLong(1, artistId);
                        mapSongGenreStatement.setLong(2, artistTypeID);
                        mapSongGenreStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                PreparedStatement mapArtistRLStatement = connection.prepareStatement(mapArtistRLQuery);
                mapArtistRLStatement.setLong(1, artistId);
                mapArtistRLStatement.setLong(2, artist.getRecordLabel().getId());
                mapArtistRLStatement.executeUpdate();

                PreparedStatement mapArtistGenreStatement = connection.prepareStatement(mapArtistGenreQuery);
                mapArtistGenreStatement.setLong(1, artistId);
                mapArtistGenreStatement.setLong(2, genreId);
                mapArtistGenreStatement.executeUpdate();

            } else {
                artistId = 0L;
            }
            connection.commit();
            return artistId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        finally {
            connection.setAutoCommit(true);
        }
        return 0L;
    }

    public long createAlbum(Connection connection, Album album) {
        String query = "insert into ALBUM (name, release_date, edition) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, album.getName());
            statement.setDate(2, (Date) album.getRelease_date());
            statement.setInt(3, album.getEdition());
            long albumId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                albumId = rs.getInt(1);
            } else {
                albumId = 0L;
            }
            return albumId;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public void createAssignSongtoArtist(Connection connection, Creates creates) throws SQLException {
        String query = "INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, creates.getSongId());
            statement.setLong(2, creates.getArtistId());
            statement.setBoolean(3, creates.getIsCollabarator());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void createAssignArtisttoRecordLabel(Connection connection, Signs signs) {
        String query = "insert into SIGNS (artist_id, record_label_id, updated_at) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, signs.getArtistId());
            statement.setLong(2, signs.getRecordLabelId());
            statement.setDate(3, signs.getUpdatedAt());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void createAssignArtisttoAlbum(Connection connection, Compiles compiles) {
        String query = "insert into COMPILES (artist_id, album_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, compiles.getArtistId());
            statement.setLong(2, compiles.getAlbumId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
    public void createAssignSongtoAlbum(Connection connection, SongAlbum songAlbum) {
        String query = "insert into SONG_ALBUM (song_id, album_id, track_num) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, songAlbum.getSong().getId());
            statement.setLong(2, songAlbum.getAlbum().getId());
            statement.setLong(3, songAlbum.getTrackNum());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
    public void createSongListen(Connection connection, SongListen songListen) {
        String query = "insert into SONG_LISTEN (song_id, user_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, songListen.getSongId());
            statement.setLong(2, songListen.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }
    public void deleteSongListen(Connection connection, long songId, int limit) {
        String query = "delete from SONG_LISTEN where song_id = ? order by id desc Limit ?";
        executeSingleParameterQuery(connection, songId, limit, query);
    }

    public void createPodcastListen(Connection connection, PodcastEpListen podcastEpListen) {
        String query = "insert into PODCAST_EP_LISTEN (user_id, podcast_id, episode_num) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastEpListen.getUserId());
            statement.setLong(2, podcastEpListen.getPodcastId());
            statement.setLong(3, podcastEpListen.getEpisodeNum());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deletePodcastListen(Connection connection, long podcastId, long episodeNum, int subs) {
        String query = "delete from PODCAST_EP_LISTEN where podcast_id = ? AND episode_num = ? order by id desc Limit ?";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, podcastId);
            statement.setLong(2, episodeNum);
            statement.setInt(3, subs);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void createRates(Connection connection, Rates rates) {
        String query = "insert into RATES (user_id, podcast_id, updated_at, rating) values (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, rates.getUserId());
            statement.setLong(2, rates.getPodcastId());
            statement.setDate(3, rates.getUpdatedAt());
            statement.setDouble(4, rates.getRating());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public long deleteRates(Connection connection, Rates rates) {
        String query = "delete from RATES where user_id = ? and podcast_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, rates.getUserId());
            statement.setLong(2, rates.getPodcastId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public double getAvgRating(Connection connection, long podcastId) {
        String query = "Select avg(rating) rating from RATES where podcast_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("rating");
            }
            return 0D;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    private long insertAndGetIdForSingleNameColumnTables(Connection connection, String query, String name) {
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public void createPodcastSubscription(Connection connection, PodcastSubscription podcastSubscription) {
        String query = "insert into PODCAST_SUBSCRIPTION (podcast_id, user_id, updated_at, subscription_status) values (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastSubscription.getPodcastId());
            statement.setLong(2, podcastSubscription.getUserId());
            statement.setTimestamp(3, podcastSubscription.getUpdated_at());
            statement.setDouble(4, podcastSubscription.getSubscriptionStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void deletePodcastSubscription(Connection connection, long podcastId, int limit) {
        String query = "delete from PODCAST_SUBSCRIPTION where podcast_id = ? order by id desc Limit ?";
        executeSingleParameterQuery(connection, podcastId, limit, query);
    }

    private void executeSingleParameterQuery(Connection connection, long podcastId, int limit, String query) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            statement.setLong(2, limit);
            statement.executeUpdate();
        }  catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public long getPodcastSubscriptionById(Connection connection, long podcastId) {
        String query = "Select count(user_id) as subCount from PODCAST_SUBSCRIPTION where podcast_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, podcastId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("subCount");
            }
            return 0L;
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long getSongPlayCountById(Connection connection, long songId) throws SQLException {
        long playCountForCurrentMonth = reportUtils.getSongPlayCountForCurrentMonth(connection, songId);
        List<Stats> statsList = reportUtils.getHistoricalSongPlayCountByMonth(connection,songId);
        long historicalPlayCount = statsList.stream().map(Stats::getCount).reduce(0L, Long::sum);
        return playCountForCurrentMonth + historicalPlayCount;
    }
    public long getPodcastPlayCountById(Connection connection, long podcastId, long episodeNum) throws SQLException {
        long playCountForCurrentMonth = reportUtils.getEpisodePlayCountForCurrentMonth(connection, podcastId, episodeNum);
        List<Stats> statsList = reportUtils.getHistoricalEpisodePlayCountByMonth(connection,podcastId, episodeNum);
        long historicalPlayCount = statsList.stream().map(Stats::getCount).reduce(0L, Long::sum);
        return playCountForCurrentMonth + historicalPlayCount;
    }

}
