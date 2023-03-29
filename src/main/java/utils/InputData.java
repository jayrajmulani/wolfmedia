package utils;

import models.Genre;
import models.Guest;
import models.Song;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputData {
    public Song getSongInput(Scanner sc) throws ParseException {
        System.out.println("Enter the title of the song:");
        String title = sc.nextLine();
        System.out.println("TITLE " + title);
        sc.nextLine();
        System.out.println("Enter the release country of the song:");
        String releaseCountry = sc.next();
        System.out.println("Enter the language of the song:");
        String language = sc.next();
        System.out.println("Enter the duration of the song:");
        float duration = sc.nextFloat();
        System.out.println("Enter the royalty rate of the song:");
        float royaltyRate = sc.nextFloat();
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
}
