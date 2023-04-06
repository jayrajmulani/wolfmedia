package utils;

import info.Read;
import models.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        System.out.println(duration);
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

    public Guest getGuestInput(Scanner sc) {
        System.out.println("Enter the name of the guest:");
        String name = sc.next();
        return new Guest(name);
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
    public long getArtistIdInputForRoyaltyPayment(){
        return 0L;
    }
    public long getHostIdInputForPayment(Connection connection, Scanner sc) throws SQLException, IllegalArgumentException {
        List<Host> hosts = read.getAllHosts(connection);
        hosts.forEach(System.out::println);
        System.out.println("Enter Host ID:");
        long hostId = sc.nextLong();
        if(!hosts.stream().anyMatch(host -> host.getId() == hostId)){
            throw new IllegalArgumentException("Invalid Host ID");
        }
        return hostId;
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
}
