package info;

import models.*;
import utils.DB;

import java.sql.*;
import java.util.Optional;

public class Create {
    private static Read read = new Read();

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
                String mapSongRLQuery = "INSERT INTO OWNS(song_id, record_label_id) VALUES (?,?)";
                PreparedStatement mapSongRLStatement = connection.prepareStatement(mapSongRLQuery);
                mapSongRLStatement.setLong(1, songId);
                mapSongRLStatement.setLong(2, song.getRecordLabel().getId());
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

    public long createUser(Connection connection, User user) throws SQLException {
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

    public long createGuest(Connection connection, Guest guest) throws SQLException {
        String query = "INSERT INTO GUEST(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, guest.getName());
    }

    public long createGenre(Connection connection, Genre genre) throws SQLException {
        String query = "INSERT INTO GENRE(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, genre.getName());
    }

    public long createSponsor(Connection connection, Sponsor sponsor) throws SQLException {
        String query = "INSERT INTO SPONSOR(name) VALUES (?)";
        return insertAndGetIdForSingleNameColumnTables(connection, query, sponsor.getName());
    }

    public long createRecordLabel(Connection connection, RecordLabel recordLabel) throws SQLException {
        String query = "insert into RECORD_LABEL (name) values (?);";
        return insertAndGetIdForSingleNameColumnTables(connection, query, recordLabel.getName());
    }

    public long createService(Connection connection, Service service) throws SQLException {
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

    public long createArtistType(Connection connection, ArtistType artistType) throws SQLException {
        String query = "INSERT INTO SPONSOR(name) VALUES (?)";
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

    public long createPodcast(Connection connection, Podcast podcast) throws SQLException {
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
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createEpisode(Connection connection, Episode episode) throws SQLException {
        String query = "INSERT INTO EPISODE(podcast_id, episode_num, title, release_date, duration, adv_count, " +
                "bonus_rate) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, episode.getPodcastId());
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
        String query = "insert into ARTIST (name, country, status) values (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getCountry());
            statement.setString(3, artist.getStatus().getStatus());
            long artistId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                artistId = rs.getInt(1);
            } else {
                artistId = 0L;
            }
            return artistId;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createAlbum(Connection connection, Album album) throws SQLException {
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
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
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
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public long createAssignArtisttoRecordLabel(Connection connection, Signs signs) throws SQLException {
        String query = "insert into SIGNS (artist_id, record_label_id, updated_at) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, signs.getArtistId());
            statement.setLong(2, signs.getRecordLabelId());
            statement.setDate(3, signs.getUpdatedAt());

            long signsId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                signsId = rs.getInt(1);
            } else {
                signsId = 0L;
            }
            return signsId;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public void createAssignArtisttoAlbum(Connection connection, Compiles compiles) throws SQLException {
        String query = "insert into COMPILES (artist_id, album_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, compiles.getArtistId());
            statement.setLong(2, compiles.getAlbumId());
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }


    public void createAssignSongtoRecordLabel(Connection connection, Owns owns) throws SQLException {
        String query = "insert into OWNS (record_label_id, song_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, owns.getRecordLabelId());
            statement.setLong(2, owns.getSongId());
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public void createAssignSongtoAlbum(Connection connection, SongAlbum songAlbum) throws SQLException {
        String query = "insert into SONG_ALBUM (song_id, album_id, track_num) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, songAlbum.getSong().getId());
            statement.setLong(2, songAlbum.getAlbum().getId());
            statement.setLong(3, songAlbum.getTrackNum());

            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
    }

    public long createSongListen(Connection connection, SongListen songListen) throws SQLException {
        String query = "insert into SONG_LISTEN (song_id, user_id) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, songListen.getSongId());
            statement.setLong(2, songListen.getUserId());

            long songListenId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                songListenId = rs.getInt(1);
            } else {
                songListenId = 0L;
            }
            return songListenId;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createPodcastListen(Connection connection, PodcastEpListen podcastEpListen) throws SQLException {
        String query = "insert into PODCAST_EP_LISTEN (user_id, podcast_id, episode_num) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, podcastEpListen.getUserId());
            statement.setLong(2, podcastEpListen.getPodcastId());
            statement.setLong(3, podcastEpListen.getEpisodeNum());

            long podcastEpListenId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                podcastEpListenId = rs.getInt(1);
            } else {
                podcastEpListenId = 0L;
            }
            return podcastEpListenId;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
        }
        return 0L;
    }

    public long createRates(Connection connection, PodcastEpListen podcastEpListen) throws SQLException {
        String query = "insert into PODCAST_EP_LISTEN (user_id, podcast_id, episode_num) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, podcastEpListen.getUserId());
            statement.setLong(2, podcastEpListen.getPodcastId());
            statement.setLong(3, podcastEpListen.getEpisodeNum());

            long podcastEpListenId;
            if (statement.executeUpdate() > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                podcastEpListenId = rs.getInt(1);
            } else {
                podcastEpListenId = 0L;
            }
            return podcastEpListenId;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            DB.rollBackTransaction(connection);
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
}
