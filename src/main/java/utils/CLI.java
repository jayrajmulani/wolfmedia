package utils;

import info.Create;
import info.Read;
import models.Guest;
import models.Song;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Scanner;

public class CLI {
    private static final Menu menu = new Menu();
    private static final Create create = new Create();
    private static final Read read = new Read();

    public void run() throws SQLException, ClassNotFoundException, ParseException {
        Scanner sc = new Scanner(System.in);
        int choice;
        while(true){
            boolean quit = false;
            menu.displayMainMenu();
            choice = sc.nextInt();
            switch (choice) {
                case -1 -> quit = true;
                case 1 -> {
                    int crudChoice;
                    while (true) {
                        quit =false;
                        menu.displayInfoProcessingMenu();
                        boolean goBack = false;
                        int infoChoice = sc.nextInt();
                        switch (infoChoice) {
                            case -1 -> quit = true;
                            case 0 -> {
                                menu.displayMainMenu();
                                goBack = true;
                            }
                            case 1 -> {
                                quit = false;
                                while(true){
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    Connection connection;
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the title of the song:");
                                            String title = sc.next();
                                            System.out.println("Enter the release country of the song:");
                                            String releaseCountry = sc.next();
                                            System.out.println("Enter the language of the song:");
                                            String language = sc.next();
                                            System.out.println("Enter the duration of the song:");
                                            float duration = sc.nextFloat();
                                            System.out.println("Enter the royalty rate of the song:");
                                            float royaltyRate = sc.nextFloat();
                                            System.out.println("Enter the release date (mm/dd/yyyy) of the song:");
                                            Date releaseDate = (Date) new SimpleDateFormat("MM/dd/yyyy").parse(sc.next());
                                            Song song = new Song(title,releaseCountry, language, duration, royaltyRate, releaseDate, false);
                                            create.createSong(connection, song);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the song:");
                                            Long id = sc.nextLong();
                                            Optional<Song> resultSong = read.getSong(id, connection);
                                            resultSong.ifPresentOrElse(System.out::println,()->System.out.println("Song not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if(goBackInner || quit){
                                        break;
                                    }
                                }
                            }
                            case 2 -> {
                                quit =false;
                                while(true){
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    Connection connection;
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the name of the guest:");
                                            String name = sc.next();
                                            Guest guest = new Guest(name);
                                            create.createGuest(connection, guest);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the guest:");
                                            Long id = sc.nextLong();
                                            Optional<Guest> resultGuest = read.getGuest(id, connection);
                                            resultGuest.ifPresentOrElse(System.out::println,()->System.out.println("Guest not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if(goBackInner || quit){
                                        break;
                                    }
                                }
                            }
                            default -> {
                                System.out.println("Please choose a value between 0 and 10...");
                                menu.displayInfoProcessingMenu();
                                continue;
                            }
                        }
                        if (goBack || quit) {
                            break;
                        }
                    }
                }
                case 2 -> System.out.println("Manage metadata and records");
                default -> {
                    System.out.println("Please choose a value between 0 and 4..");
                    menu.displayMainMenu();
                }
            }
            if(quit){
                System.out.println("Hope you had a great experience..");
                break;
            }
        }
    }
}
