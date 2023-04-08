package utils;

import info.Read;
import models.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InputData {
    private static final Read read = new Read();
    public Song getSongInput(Scanner sc) throws ParseException {

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
        Date releaseDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());
        sc.nextLine();
        System.out.println("Enter list of genres for this song (Separate multiple values by |):");
        System.out.println("E.g. LOVE | ROCK");
        String genresPipeSeparated = sc.nextLine();
        List<Genre> genres = Arrays.stream(genresPipeSeparated.split("\\|")).map(genre -> new Genre(genre.strip())).toList();
        return new Song(title, releaseCountry, language, duration, royaltyRate, releaseDate, false, genres);
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
    public long getArtistIdInput(Connection connection, Scanner sc) throws SQLException {
        List<Artist> artists = read.getAllArtists(connection);
        artists.forEach(System.out::println);
        System.out.println("Enter Artist ID:");
        long artistId = sc.nextLong();
        if(!artists.stream().anyMatch(artist -> artist.getId() == artistId)){
            throw new IllegalArgumentException("Invalid Artist ID");
        }
        return artistId;
    }
    public long getRecordLabelIdInput(Connection connection, Scanner sc) throws SQLException {
        List<RecordLabel> recordLabels = read.getAllRecordLabels(connection);
        recordLabels.forEach(System.out::println);
        long recordLabelId = sc.nextLong();
        if(!recordLabels.stream().anyMatch(recordLabel -> recordLabel.getId() == recordLabelId)){
            throw new IllegalArgumentException("Invalid Host ID");
        }
        return recordLabelId;
    }
    public long getHostIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Host> hosts = read.getAllHosts(connection);
        hosts.forEach(System.out::println);
        System.out.println("Enter Host ID:");
        long hostId = sc.nextLong();
        if(!hosts.stream().anyMatch(host -> host.getId() == hostId)){
            throw new IllegalArgumentException("Invalid Host ID");
        }
        return hostId;
    }
    public long getServiceIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Service> services = read.getAllServices(connection);
        services.forEach(System.out::println);
        System.out.println("Enter Service ID:");
        long serviceId = sc.nextLong();
        if(!services.stream().anyMatch(service -> service.getId() == serviceId)){
            throw new IllegalArgumentException("Invalid Service ID");
        }
        return serviceId;
    }
    public long getUserIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<User> users = read.getAllUsers(connection);
        users.forEach(System.out::println);
        System.out.println("Enter User ID:");
        long userId = sc.nextLong();
        if(!users.stream().anyMatch(user -> user.getId() == userId)){
            throw new IllegalArgumentException("Invalid User ID");
        }
        return userId;
    }
    public long getSongIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Song> songs = read.getAllSongs(connection);
        songs.forEach(System.out::println);
        System.out.println("Enter Song ID:");
        final long songId = sc.nextLong();
        if(!songs.stream().anyMatch(host -> host.getId() == songId)){
            throw  new IllegalArgumentException("Please enter a valid Song ID.");
        }
        return songId;
    }
    public long getAlbumIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Album> albums = read.getAllAlbums(connection);
        albums.forEach(System.out::println);
        System.out.println("Enter Song ID:");
        final long albumId = sc.nextLong();
        if(!albums.stream().anyMatch(album -> album.getId() == albumId)){
            throw  new IllegalArgumentException("Please enter a valid Song ID.");
        }
        return albumId;
    }
    public long getPodcastIdInput(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Podcast> podcasts = read.getAllPodcasts(connection);
        podcasts.forEach(System.out::println);
        System.out.println("Enter Podcast ID:");
        final long podcastId = sc.nextLong();
        if(!podcasts.stream().anyMatch(podcast -> podcast.getId() == podcastId)){
            throw  new IllegalArgumentException("Please enter a valid Podcast ID.");
        }
        return podcastId;
    }
    public long getEpisodeNumberInput(Connection connection, Scanner sc, long podcastId) throws SQLException {
        List<Episode> episodes = read.getAllPodcastEpisodes(connection, podcastId);
        if(episodes.size() == 0){
            throw new IllegalArgumentException("No episodes found for this podcast");
        }
        episodes.forEach(System.out::println);
        System.out.println("Enter Episode Number:");
        final long episodeNum = sc.nextLong();
        while(!episodes.stream().anyMatch(host -> host.getEpisodeNum() == episodeNum)){
            throw  new IllegalArgumentException("Please enter a valid Episode Number.");
        }
        return episodeNum;
    }
    //FIXME: Remove myObj and use the passed sc Scanner object
    public Guest getGuestInput(Scanner sc){

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the Guest name: ");
        String name = myObj.nextLine();

        return new Guest(name);
    }
    public Service getServiceInput (Scanner sc) {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the Service name: ");
        String name = myObj.nextLine();

        System.out.println("Enter the current balance of the Service: ");
        double balance = myObj.nextDouble();

        return new Service(name, balance);
    }
    public User getUserInput (Scanner sc) throws ParseException {

        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the first name of the User: ");
        String fName = myObj.nextLine();

        System.out.println("Enter the last name of the User: ");
        String lName = myObj.nextLine();

        System.out.println("Enter the registration date of the User: ");
        Date regDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(sc.next()).getTime());

        System.out.println("Enter the phone number of the User: ");
        String phone  = myObj.nextLine();

        System.out.println("Enter the email id of the User: ");
        String email = myObj.nextLine();

        System.out.println("Enter the premium status of the User: ");
        Boolean premiumStatus = myObj.nextBoolean();

        System.out.println("Enter the monthly premium fees of the User: ");
        double premiumMonthlyStatus = myObj.nextDouble();

        return new User(fName, lName, regDate, phone, email, premiumStatus, premiumMonthlyStatus);
    }

    public RecordLabel getRecordLabelInput (Scanner sc) {

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
    public Album getAlbumInput(Scanner sc) throws ParseException{

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
        if(songsInOwns.size()==allSongs.size())
        {
            System.out.println("All Songs already assigned to Record Label, Insert a song first");
            return Optional.empty();
        }

        allSongs.forEach(allSong -> {
            if (!songsInOwns.contains(allSong.getId()))
                System.out.println(allSong);
        });

        long songId;
        while(true)
        {
            System.out.println("Enter Song ID:");
            songId = sc.nextLong();
            if(songsInOwns.contains(songId))
                System.out.println("Song already in owns");
            else
                break;
        }
        System.out.println("Here is the List of all Record Labels");
        List<RecordLabel> allRecordLabels = read.getAllRecordLabels(connection);
        allRecordLabels.forEach(System.out::println);

        System.out.println("Enter Record Label ID:");
        long recordLabelId = sc.nextLong();

        return Optional.of( new Owns(recordLabelId, songId));
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

        System.out.println("Enter Artist ID:");
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
        allSongAlbums.forEach(allSongAlbum -> songsInSongAlbum.add(allSongAlbum.getSongId()));
        if(songsInSongAlbum.size()==allSongs.size())
        {
            System.out.println("All Songs already assigned to Album, Insert a song first");
            return Optional.empty();
        }

        allSongs.forEach(allSong -> {
            if (!songsInSongAlbum.contains(allSong.getId()))
                System.out.println(allSong);
        });

        long songId;
        while(true)
        {
            System.out.println("Enter Song ID:");
            songId = sc.nextLong();
            if(songsInSongAlbum.contains(songId))
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
            if(allSongAlbum.getAlbumId() == albumId)
            {
                allTracksinGivenRL.add(allSongAlbum.getTrackNum());
            }
        });
        long trackNum;
        while(true)
        {
            System.out.println("Enter track number within Album: ");
            trackNum = sc.nextLong();
            if(allTracksinGivenRL.contains(trackNum))
                System.out.println("Song already in TrackNum");
            else
                break;
        }
        return Optional.of(new SongAlbum(songId, albumId, trackNum));
    }


}
