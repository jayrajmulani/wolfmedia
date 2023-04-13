package utils;

import info.Create;
import info.Read;
import models.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InputData {
    private static final Read read = new Read();
    private static final Create create = new Create();

    public SongAlbum getSongInput(Connection connection, boolean isCreateOp) throws ParseException, SQLException, IllegalArgumentException {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the title of the song:");
        String title = myObj.nextLine();
        System.out.println("Enter the language of the song:");
        String language = myObj.nextLine();
        System.out.println("Enter the release country of the song:");
        String releaseCountry = myObj.nextLine();
        System.out.println("Enter the duration of the song:");
        double duration = myObj.nextDouble();
        System.out.println("Enter the royalty rate of the song:");
        double royaltyRate = myObj.nextDouble();
        System.out.println("Enter the release date (mm/dd/yyyy) of the song:");
        Date releaseDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(myObj.next()).getTime());
        myObj.nextLine();
        System.out.println("Enter list of genres for this song (Separate multiple values by |):");
        System.out.println("E.g. LOVE | ROCK");
        String genresPipeSeparated = myObj.nextLine();
        List<Genre> genres = Arrays.stream(genresPipeSeparated.split("\\|")).map(genre -> new Genre(genre.strip())).toList();

        List<Artist> allArtists = read.getAllArtists(connection);
        allArtists.forEach(System.out::println);
        System.out.println("Enter list of artist IDs for this song (Separate multiple values by |):");
        System.out.println("The first artist would be considered as Primary Artist and song would be owned by its record label.");
        System.out.println("E.g. 1 | 4");
        String artistsPipeSeparated = myObj.nextLine();
        List<Artist> artists = Arrays.stream(artistsPipeSeparated.split("\\|")).map(artistID -> new Artist(Long.parseLong(artistID.trim()))).toList();
        System.out.println("Enter list of collaborator IDs for this song (Separate multiple values by |):");
        System.out.println("E.g. 5 | 2");
        String collaboratorsPipeSeparated = myObj.nextLine();
        List<Artist> collaborators = new ArrayList<>();
        if (!collaboratorsPipeSeparated.isEmpty()) {
            collaborators = Arrays.stream(collaboratorsPipeSeparated.split("\\|")).map(artistID -> new Artist(Long.parseLong(artistID))).toList();
        }

        if (!Collections.disjoint(artists, collaborators)) {
            throw new IllegalArgumentException("Artists and Collaborators have some common elements");
        }

        System.out.println("Do you want to assign this song to an album ? (y/n)");
        boolean doAddAlbum = myObj.nextLine().equalsIgnoreCase("y");

        if (doAddAlbum) {
            long albumID = this.getAlbumIdInput(connection, myObj);
            Album album = new Album(albumID);
            System.out.println("Enter the track number of the song");
            long trackNum = myObj.nextLong();
            Song s = new Song(title, releaseCountry, language, duration, royaltyRate, releaseDate,
                    false, genres, album, artists, collaborators, null);

            return new SongAlbum(s, album, trackNum);
        }
        return new SongAlbum(new Song(title, releaseCountry, language, duration, royaltyRate, releaseDate,
                false, genres, artists, collaborators, null));
    }

    public Host getHostInput(Scanner sc) {
        System.out.println("Enter the first name of the host:");
        String firstName = sc.nextLine();
        System.out.println("Enter the last name of the host:");
        String lastName = sc.nextLine();
        System.out.println("Enter the city of the host:");
        String city = sc.nextLine();
        System.out.println("Enter the email of the host:");
        String email = sc.nextLine();
        System.out.println("Enter the phone of the host:");
        String phone = sc.nextLine();
        return new Host(firstName, lastName, city, email, phone);
    }

    public Podcast getPodcastInput(Scanner sc) {
        System.out.println("Enter the name of the podcast:");
        String name = sc.nextLine();
        System.out.println("Enter the language of the podcast:");
        String language = sc.nextLine();
        System.out.println("Enter the country of the podcast:");
        String country = sc.nextLine();
        System.out.println("Enter the flat fee of the podcast:");
        double flatFee = sc.nextDouble();

        List<Host> hosts = new ArrayList<>();
        int crudChoice;
        boolean isFinishedHostInput = false;
        while (!isFinishedHostInput) {
            System.out.println("Hosts for Podcast " + name);
            System.out.println("1. Already present");
            System.out.println("2. Add new host");
            System.out.println("-1. Done adding");
            System.out.print("Enter your choice: ");
            crudChoice = sc.nextInt();
            switch (crudChoice) {
                case -1 -> {
                    if (hosts.size() == 0) {
                        System.out.println("Please add at least 1 host");
                        continue;
                    }
                    isFinishedHostInput = true;
                }
                case 1 -> {
                    System.out.println("Please enter the ID of the host");
                    int hostID = sc.nextInt();
                    hosts.add(new Host(hostID));
                }
                case 2 -> {
                    hosts.add(getHostInput(sc));
                }
                default -> {
                    System.out.println("Please choose a value between 1 and 2...");
                }
            }
        }

        return new Podcast(name, language, country, flatFee, hosts);
    }

    public Episode getEpisodeInput(Scanner sc, long podcastID) throws ParseException {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the number of the episode:");
        long episodeNum = myObj.nextLong();
        myObj.nextLine();
        System.out.println("Enter the title of the episode:");
        String title = myObj.nextLine();
        System.out.println("Enter the release date (mm/dd/yyyy) of the episode:");
        Date releaseDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(myObj.next()).getTime());
        System.out.println("Enter the duration of the episode in seconds:");
        double duration = myObj.nextDouble();
        System.out.println("Enter the number of advertisers in the episode:");
        int advCount = myObj.nextInt();
        System.out.println("Enter the bonus rate of the episode:");
        double bonusRate = myObj.nextDouble();

        return new Episode(new Podcast(podcastID), episodeNum, title, releaseDate, duration, advCount, bonusRate);
    }

    public long getArtistIdInput(Connection connection, Scanner sc) throws SQLException {
        List<Artist> artists = read.getAllArtists(connection);
        List<Long> artistIds = artists.stream().map(Artist::getId).toList();
        artists.forEach(System.out::println);
        System.out.println("Enter Artist ID:");
        long artistId = sc.nextLong();
        while (!artistIds.contains(artistId)) {
            System.out.println("Please Enter Valid Artist ID:");
            artistId = sc.nextLong();
        }
        return artistId;
    }

    public long getRecordLabelIdInput(Connection connection, Scanner sc) throws SQLException {
        List<RecordLabel> recordLabels = read.getAllRecordLabels(connection);
        List<Long> recordLabelIds = recordLabels.stream().map(RecordLabel::getId).toList();
        recordLabels.forEach(System.out::println);
        long recordLabelId = sc.nextLong();
        while (!recordLabelIds.contains(recordLabelId)) {
            System.out.println("Please Enter Valid Record Label ID:");
            recordLabelId = sc.nextLong();
        }
        return recordLabelId;
    }

    public long getHostIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Host> hosts = read.getAllHosts(connection);
        List<Long> hostIds = hosts.stream().map(Host::getId).toList();
        hosts.forEach(System.out::println);
        System.out.println("Enter Host ID:");
        long hostId = sc.nextLong();
        while (!hostIds.contains(hostId)) {
            System.out.println("Please Enter Valid Host ID:");
            hostId = sc.nextLong();
        }
        return hostId;
    }

    public long getServiceIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Service> services = read.getAllServices(connection);
        List<Long> serviceIds = services.stream().map(Service::getId).toList();
        services.forEach(System.out::println);
        System.out.println("Enter Service ID:");
        long serviceId = sc.nextLong();
        while (!serviceIds.contains(serviceId)) {
            System.out.println("Please Enter Valid Service ID:");
            serviceId = sc.nextLong();
        }
        return serviceId;
    }

    public long getUserIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<User> users = read.getAllUsers(connection);
        List<Long> userIds = users.stream().map(User::getId).toList();
        users.forEach(System.out::println);
        System.out.println("Enter User ID:");
        long userId = sc.nextLong();
        while (!userIds.contains(userId)) {
            System.out.println("Please Valid Service ID:");
            userId = sc.nextLong();
        }
        return userId;
    }

    public long getSongIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Song> songs = read.getAllSongs(connection);
        List<Long> songIds = songs.stream().map(Song::getId).toList();
        songs.forEach(System.out::println);
        System.out.println("Enter Song ID:");
        long songId = sc.nextLong();
        while (!songIds.contains(songId)) {
            System.out.println("Please Valid Song ID:");
            songId = sc.nextLong();
        }
        return songId;
    }

    public long getAlbumIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Album> albums = read.getAllAlbums(connection);
        List<Long> albumIds = albums.stream().map(Album::getId).toList();
        albums.forEach(System.out::println);
        System.out.println("Enter Album ID:");
        long albumId = sc.nextLong();
        while (!albumIds.contains(albumId)) {
            System.out.println("Please Valid Album ID:");
            albumId = sc.nextLong();
        }
        return albumId;
    }
    public long getGuestIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Guest> guests = read.getAllGuests(connection);
        List<Long> ids = guests.stream().map(Guest::getId).toList();
        guests.forEach(System.out::println);
        System.out.println("Enter Guest ID:");
        long id = sc.nextLong();
        while (!ids.contains(id)) {
            System.out.println("Please Valid Guest ID:");
            id = sc.nextLong();
        }
        return id;
    }
    public long getSponsorIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Sponsor> sponsors = read.getAllSponsors(connection);
        List<Long> ids = sponsors.stream().map(Sponsor::getId).toList();
        sponsors.forEach(System.out::println);
        System.out.println("Enter Sponsor ID:");
        long id = sc.nextLong();
        while (!ids.contains(id)) {
            System.out.println("Please Valid Sponsor ID:");
            id = sc.nextLong();
        }
        return id;
    }

    public long getPodcastIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Podcast> podcasts = read.getAllPodcasts(connection);
        List<Long> podcastIds = podcasts.stream().map(Podcast::getId).toList();
        podcasts.forEach(System.out::println);
        System.out.println("Enter Podcast ID:");
        long podcastId = sc.nextLong();
        while (!podcastIds.contains(podcastId)) {
            System.out.println("Please Valid Podcast ID:");
            podcastId = sc.nextLong();
        }
        return podcastId;
    }

    public Optional<Long> getEpisodeNumberInput(Connection connection, Scanner sc, long podcastId) throws SQLException {
        List<Episode> episodes = read.getAllPodcastEpisodes(connection, podcastId);
        if (episodes.size() == 0) {
            return Optional.empty();
        }
        episodes.forEach(System.out::println);
        System.out.println("Enter Episode Number:");
        List<Long> episodeNumbers = episodes.stream().map(Episode::getEpisodeNum).toList();

        long episodeNum = sc.nextLong();
        while (!episodeNumbers.contains(episodeNum)) {
            System.out.println("Please Valid Episode Number:");
            episodeNum = sc.nextLong();
        }
        return Optional.of(episodeNum);
    }

    public Guest getGuestInput() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the Guest name: ");
        String name = myObj.nextLine();
        return new Guest(name);
    }

    public Sponsor getSponsorInput(Scanner sc) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the Sponsor name: ");
        String name = myObj.nextLine();
        return new Sponsor(name);
    }

    public Service getServiceInput(Scanner sc) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the Service name: ");
        String name = myObj.nextLine();
        System.out.println("Enter the current balance of the Service: ");
        double balance = myObj.nextDouble();
        return new Service(name, balance);
    }

    public User getUserInput(Scanner sc) throws ParseException {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the first name of the User: ");
        String fName = myObj.nextLine();

        System.out.println("Enter the last name of the User: ");
        String lName = myObj.nextLine();

        System.out.println("Enter the registration date of the User: ");
        Date regDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());

        System.out.println("Enter the phone number of the User: ");
        String phone = myObj.nextLine();

        System.out.println("Enter the email id of the User: ");
        String email = myObj.nextLine();

        System.out.println("Enter the premium status of the User: ");
        Boolean premiumStatus = myObj.nextBoolean();

        System.out.println("Enter the monthly premium fees of the User: ");
        double premiumMonthlyStatus = myObj.nextDouble();

        return new User(fName, lName, regDate, phone, email, premiumStatus, premiumMonthlyStatus);
    }

    public RecordLabel getRecordLabelInput(Scanner sc) {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the name of the Record Label name: ");
        String name = myObj.nextLine();

        return new RecordLabel(name);
    }

    public Artist getArtistInput(Scanner sc) {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the name of the Artist: ");
        String name = myObj.nextLine();

        System.out.println("Enter the country of the Artist: ");
        String country = myObj.nextLine();

        System.out.println("Enter the status of the Artist: ");
        Artist.ArtistStatus status = Artist.ArtistStatus.valueOf(myObj.nextLine());


        return new Artist(name, country, status);
    }

    public Album getAlbumInput(Scanner sc) throws ParseException {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the name of the Album: ");
        String name = myObj.nextLine();

        System.out.println("Enter the release date of the Album: ");
        Date release_date = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());

        System.out.println("Enter the edition of the Album: ");
        int edition = myObj.nextInt();

        return new Album(name, release_date, edition);
    }

    public Creates getSongToArtistInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Songs");
        List<Song> allSongs = read.getAllSongs(connection);
        allSongs.forEach(System.out::println);

        System.out.println("Enter Song ID:");
        long songId = sc.nextLong();

        System.out.println("Here is the List of all Artists");
        List<Artist> allArtists = read.getAllArtists(connection);
        allArtists.forEach(System.out::println);

        System.out.println("Enter Artist ID:");
        long artistId = sc.nextLong();

        System.out.println("For this song is the artist a collabarator? ");
        boolean isCollabarator = sc.nextBoolean();

        return new Creates(songId, artistId, isCollabarator);
    }

    public Optional<Owns> getSongtoRecordLabelInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Songs (Not yet assigned to a Record Label)");
        List<Song> allSongs = read.getAllSongs(connection);

        List<Owns> allOwns = read.getAllOwns(connection);
        Set<Long> songsInOwns = new HashSet<>();
        allOwns.forEach(allOwn -> songsInOwns.add(allOwn.getSongId()));
        if (songsInOwns.size() == allSongs.size()) {
            System.out.println("All Songs already assigned to Record Label, Insert a song first");
            return Optional.empty();
        }

        allSongs.forEach(allSong -> {
            if (!songsInOwns.contains(allSong.getId()))
                System.out.println(allSong);
        });

        long songId;
        while (true) {
            System.out.println("Enter Song ID:");
            songId = sc.nextLong();
            if (songsInOwns.contains(songId))
                System.out.println("Song already in owns");
            else
                break;
        }
        System.out.println("Here is the List of all Record Labels");
        List<RecordLabel> allRecordLabels = read.getAllRecordLabels(connection);
        allRecordLabels.forEach(System.out::println);

        System.out.println("Enter Record Label ID:");
        long recordLabelId = sc.nextLong();

        return Optional.of(new Owns(recordLabelId, songId));
    }

    public Compiles getArtisttoAlbumInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Artists");
        List<Artist> allArtists = read.getAllArtists(connection);
        allArtists.forEach(System.out::println);

        System.out.println("Enter Artist ID:");
        long artistId = sc.nextLong();

        System.out.println("Here is the List of all Albums");
        List<Album> allAlbums = read.getAllAlbums(connection);
        allAlbums.forEach(System.out::println);

        System.out.println("Enter Album ID:");
        long albumId = sc.nextLong();

        return new Compiles(artistId, albumId);
    }

    public Signs getArtisttoRecordLabelInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Artists");
        List<Artist> allArtists = read.getAllArtists(connection);
        allArtists.forEach(System.out::println);

        System.out.println("Enter Artist ID:");
        long artistId = sc.nextLong();

        System.out.println("Here is the List of all Record Labels");
        List<RecordLabel> allRecordLabels = read.getAllRecordLabels(connection);
        allRecordLabels.forEach(System.out::println);

        System.out.println("Enter Record Label ID:");
        long recordLabelId = sc.nextLong();

        System.out.println("Enter the registration date of the User: ");
        Date updatedAt = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());


        return new Signs(artistId, recordLabelId, updatedAt);
    }

    public Optional<SongAlbum> getSongtoAlbumInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Songs (Not yet assigned to an Album)");
        List<Song> allSongs = read.getAllSongs(connection);

        List<SongAlbum> allSongAlbums = read.getAllSongAlbum(connection);
        Set<Long> songsInSongAlbum = new HashSet<>();
        allSongAlbums.forEach(allSongAlbum -> songsInSongAlbum.add(allSongAlbum.getSong().getId()));
        if (songsInSongAlbum.size() == allSongs.size()) {
            System.out.println("All Songs already assigned to Album, Insert a song first");
            return Optional.empty();
        }

        allSongs.forEach(allSong -> {
            if (!songsInSongAlbum.contains(allSong.getId()))
                System.out.println(allSong);
        });

        long songId;
        while (true) {
            System.out.println("Enter Song ID:");
            songId = sc.nextLong();
            if (songsInSongAlbum.contains(songId))
                System.out.println("Song already in SongAlbum");
            else
                break;
        }

        System.out.println("Here is the List of all Albums");
        List<Album> allAlbums = read.getAllAlbums(connection);
        allAlbums.forEach(System.out::println);

        System.out.println("Enter Album ID:");
        long albumId = sc.nextLong();

        Set<Long> allTracksinGivenRL = new HashSet<>();
        allSongAlbums.forEach(allSongAlbum -> {
            if (allSongAlbum.getAlbum().getId() == albumId) {
                allTracksinGivenRL.add(allSongAlbum.getTrackNum());
            }
        });
        long trackNum;
        while (true) {
            System.out.println("Enter track number within Album: ");
            trackNum = sc.nextLong();
            if (allTracksinGivenRL.contains(trackNum))
                System.out.println("Song already in TrackNum");
            else
                break;
        }
        return Optional.of(new SongAlbum(new Song(songId), new Album(albumId), trackNum));
    }

    public PaymentReportInput getPaymentReportInputForHost(Connection connection, Scanner sc) throws SQLException, ParseException {
        long hostId = getHostIdInput(connection, sc);
        System.out.println("Enter the start date (mm/dd/yyyy):");
        Date startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        System.out.println("Enter the end date (mm/dd/yyyy):");
        Date endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        while (startDate.after(endDate)) {
            System.out.println("Start date must be before end date.");
            System.out.println("Enter the start date (mm/dd/yyyy):");
            startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
            System.out.println("Enter the end date (mm/dd/yyyy):");
            endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        }
        return new PaymentReportInput(startDate, endDate, hostId, PaymentUtils.Stakeholder.PODCAST_HOST);
    }

    public SongListen getSongListenInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Songs");
        List<Song> allSongs = read.getAllSongs(connection);
        allSongs.forEach(System.out::println);

        System.out.println("Enter Song ID:");
        long songId = sc.nextLong();

        System.out.println("Here is the List of all Users");
        List<User> allUsers = read.getAllUsers(connection);
        allUsers.forEach(System.out::println);

        System.out.println("Enter User ID:");
        long userId = sc.nextLong();

        return new SongListen(songId, userId);
    }

    public PodcastEpListen getPodcastEpListenInput(Connection connection, Scanner sc) throws ParseException, SQLException {

        System.out.println("Here is the List of all Podcasts");
        List<Podcast> allPodcasts = read.getAllPodcasts(connection);
        allPodcasts.forEach(System.out::println);

        System.out.println("Enter Podcast ID:");
        long podcastId = sc.nextLong();

        System.out.println("Here is the List of all Users");
        List<User> allUsers = read.getAllUsers(connection);
        allUsers.forEach(System.out::println);

        System.out.println("Enter User ID:");
        long userId = sc.nextLong();

        System.out.println("Here is the List of all Episodes (In the podcast " + podcastId + ")");
        List<Episode> allPodcastEpisodes = read.getAllPodcastEpisodes(connection, podcastId);
        allPodcastEpisodes.forEach(System.out::println);

        long episodeId;

        while (true) {
            System.out.println("Enter Episode ID:");
            episodeId = sc.nextLong();

            if (!allPodcastEpisodes.contains(episodeId))
                System.out.println("The Episode ID does not exist with the above selected podcast");
            else
                break;
        }
        return new PodcastEpListen(podcastId, userId, episodeId);
    }

    public void getRates(Connection connection, Scanner sc) throws SQLException {
        long podcastId = this.getPodcastIdInput(connection, sc);
        long userId = this.getUserIdInput(connection, sc);
        System.out.println("Enter the rating");
        double rating = sc.nextDouble();
        Date updated_at = new java.sql.Date(System.currentTimeMillis());
        Optional<Double> currentRating = read.getRatingByPodcastIdUserId(connection, userId, podcastId);
        if (!currentRating.isEmpty()) {
            create.deleteRates(connection, new Rates(userId, podcastId, rating, updated_at));
        }
        create.createRates(connection, new Rates(userId, podcastId, rating, updated_at));
    }

    public void increaseSongPlayCount(Connection connection, Scanner sc) throws ParseException, SQLException {
        long songId = getSongIdInput(connection, sc);
        List<User> users = read.getAllUsers(connection);
        List<Long> userIds = users.stream().map(User::getId).toList();
        if(userIds.size() == 0) {
            System.out.println("There must be at least one user in the system. No users found.");
        }
        System.out.println("By how many counts do you want to increase the play count?");
        int subs = sc.nextInt();
        for (int i = 1; i <= subs; i++) {
            create.createSongListen(connection, new SongListen(songId, userIds.get(new Random().nextInt(userIds.size()))));
        }
    }

    public void decreaseSongPlayCount(Connection connection, Scanner sc) throws SQLException {
        long songId = getSongIdInput(connection, sc);
        System.out.println("By how many counts do you want to decrease the play count of the song?");
        int subs = sc.nextInt();
        create.deleteSongListen(connection, songId, subs);
    }

    public void increasePodcastPlayCount(Connection connection, Scanner sc) throws ParseException, SQLException {
        long podcastId = getPodcastIdInput(connection, sc);
        Optional<Long> episodeNum = getEpisodeNumberInput(connection, sc, podcastId);
        if(episodeNum.isEmpty()){
            System.out.println("This podcast doesn't have any episode.");
            return;
        }
        System.out.println("By how many counts do you want to increase the play count?");
        int subs = sc.nextInt();
        List<User> users = read.getAllUsers(connection);
        List<Long> userIds = users.stream().map(User::getId).toList();
        for (int i = 1; i <= subs; i++) {
            create.createPodcastListen(connection, new PodcastEpListen(podcastId, userIds.get(new Random().nextInt(userIds.size())), episodeNum.get()));
        }
    }
    public void decreasePodcastPlayCount(Connection connection, Scanner sc) throws  SQLException {
        long podcastId = getPodcastIdInput(connection, sc);
        Optional<Long> episodeNum = getEpisodeNumberInput(connection, sc, podcastId);
        if(episodeNum.isEmpty()){
            System.out.println("This podcast doesn't have any episode.");
            return;
        }
        System.out.println("By how many counts do you want to decrease the play count of the episode?");
        int subs = sc.nextInt();
        create.deletePodcastListen(connection, podcastId, episodeNum.get(), subs);
    }

    public void increasePodcastSubscription(Connection connection, Scanner sc) throws  SQLException {
        long podcastId = this.getPodcastIdInput(connection, sc);
        System.out.println("By how many counts do you want to increase the subscriptions?");
        int subs = sc.nextInt();

        for (int i = 1; i <= subs; i++) {
            long id = create.createUser(connection, new User("", "", new java.sql.Date(System.currentTimeMillis()), "", "", false, 0.0));
            create.createPodcastSubscription(connection, new PodcastSubscription(podcastId, id, new Timestamp(System.currentTimeMillis()), 0));
        }
    }

    public void decreasePodcastSubscription(Connection connection, Scanner sc) throws ParseException, SQLException {
        long podcastId = this.getPodcastIdInput(connection, sc);
        System.out.println("By how many counts do you want to increase the subscriptions?");
        int subs = sc.nextInt();
        create.deletePodcastSubscription(connection, podcastId, subs);
    }

    public Optional<Long> getAverageRating(Connection connection, Scanner sc) throws ParseException, SQLException {
        System.out.println("Here is the List of all Podcasts (That have latest 1 Rating)");
        List<Podcast> allPodcasts = read.getAllPodcasts(connection);
        List<Rates> allRates = read.getAllRates(connection);
        Set<Long> podcastsInRates = new HashSet<>();
        allRates.forEach(allRate -> podcastsInRates.add(allRate.getPodcastId()));
        if (podcastsInRates.size() == 0) {
            System.out.println("No Podcast has a Rating, Pls add a rating to any podcast");
            return Optional.empty();
        }
        allPodcasts.forEach(allPodcast -> {
            if (podcastsInRates.contains(allPodcast.getId()))
                System.out.println(allPodcast);
        });
        long podcastId;
        while (true) {
            System.out.println("Enter Podcast ID:");
            podcastId = sc.nextLong();
            if (!podcastsInRates.contains(podcastId))
                System.out.println("Podcast not yet rated");
            else
                break;
        }
        return Optional.of(podcastId);
    }

    public PaymentReportInput getPaymentReportInputForArtist(Connection connection, Scanner sc) throws SQLException, ParseException {
        long artistId = getArtistIdInput(connection, sc);
        System.out.println("Enter the start date (mm/dd/yyyy):");
        Date startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        System.out.println("Enter the end date (mm/dd/yyyy):");
        Date endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        while (startDate.after(endDate)) {
            System.out.println("Start date must be before end date.");
            System.out.println("Enter the start date (mm/dd/yyyy):");
            startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
            System.out.println("Enter the end date (mm/dd/yyyy):");
            endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        }
        return new PaymentReportInput(startDate, endDate, artistId, PaymentUtils.Stakeholder.ARTIST);
    }

    public PaymentReportInput getPaymentReportInputForRecordLabel(Connection connection, Scanner sc) throws SQLException, ParseException {
        long rlId = getRecordLabelIdInput(connection, sc);
        System.out.println("Enter the start date (mm/dd/yyyy):");
        Date startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        System.out.println("Enter the end date (mm/dd/yyyy):");
        Date endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        while (startDate.after(endDate)) {
            System.out.println("Start date must be before end date.");
            System.out.println("Enter the start date (mm/dd/yyyy):");
            startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
            System.out.println("Enter the end date (mm/dd/yyyy):");
            endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        }
        return new PaymentReportInput(startDate, endDate, rlId, PaymentUtils.Stakeholder.RECORD_LABEL);
    }
}
