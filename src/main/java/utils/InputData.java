package utils;

import models.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return new Song(title,releaseCountry, language, duration, royaltyRate, releaseDate, false, genres);
    }
    public Guest getGuestInput(Scanner sc){
        System.out.println("Enter the name of the guest:");
        String name = sc.next();
        return new Guest(name);
    }

    public Host getHostInput(Scanner sc){
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
}
