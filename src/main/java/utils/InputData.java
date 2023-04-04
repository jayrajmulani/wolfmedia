package utils;

import models.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputData {
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
    public Host getHostInput(Scanner sc) throws ParseException {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the first name of the host:");
        String firstName = myObj.nextLine();
        System.out.println("Enter the last name of the host:");
        String lastName = myObj.nextLine();
        System.out.println("Enter the city of the host:");
        String city = myObj.nextLine();
        System.out.println("Enter the email of the host:");
        String email = myObj.nextLine();
        System.out.println("Enter the phone of the host:");
        String phone = myObj.nextLine();
        return new Host(firstName, lastName, city, email, phone);
    }

    public Podcast getPodcastInput(Scanner sc) throws ParseException {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Enter the name of the podcast:");
        String name = myObj.nextLine();
        System.out.println("Enter the language of the podcast:");
        String language = myObj.nextLine();
        System.out.println("Enter the country of the podcast:");
        String country = myObj.nextLine();
        System.out.println("Enter the flat fee of the podcast:");
        double flatFee = myObj.nextDouble();

        List<Host> hosts = new ArrayList<>();
        int crudChoice;
        boolean isFinishedHostInput = false;
        while (!isFinishedHostInput) {
            System.out.println("Hosts for Podcast " + name);
            System.out.println("1. Already present");
            System.out.println("2. Add new host");
            System.out.println("-1. Done adding");
            System.out.print("Enter your choice: ");
            crudChoice = myObj.nextInt();
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
                    int hostID = myObj.nextInt();
                    hosts.add(new Host(hostID));
                }
                case 2 -> {
                    hosts.add(getHostInput(myObj));
                }
                default -> {
                    System.out.println("Please choose a value between 1 and 2...");
                }
            }
        }

        return new Podcast(name, language, country, flatFee, hosts);
    }
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

}
