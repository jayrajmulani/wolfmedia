package utils;

import info.Create;
import info.Read;
import models.Guest;
import models.Host;
import models.Podcast;
import models.Song;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.Scanner;

public class CLI {
    private static final Menu menu = new Menu();
    private static final Create create = new Create();
    private static final Read read = new Read();
    private static final InputData inputData = new InputData();

    public void run() throws SQLException, ClassNotFoundException, ParseException {
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true) {
            boolean quit = false;
            menu.displayMainMenu();
            choice = sc.nextInt();
            switch (choice) {
                case -1 -> quit = true;
                case 1 -> {
                    // Information processing menu
                    int crudChoice;
                    while (true) {
                        quit = false;
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
                                // Song Menu
                                quit = false;
                                while (true) {
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
                                            // Create Song
                                            connection = DB.getConnection();
                                            long id = create.createSong(connection, inputData.getSongInput(sc));
                                            System.out.println("Song created successfully with id " + id);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            // Get Song
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the song:");
                                            Long id = sc.nextLong();
                                            Optional<Song> resultSong = read.getSong(id, connection);
                                            resultSong.ifPresentOrElse(System.out::println, () -> System.out.println("Song not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            case 2 -> {
                                quit = false;
                                while (true) {
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
                                            long id = create.createGuest(connection, inputData.getGuestInput(sc));
                                            System.out.println("Guest created successfully with id " + id);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the guest:");
                                            Long id = sc.nextLong();
                                            Optional<Guest> resultGuest = read.getGuest(id, connection);
                                            resultGuest.ifPresentOrElse(System.out::println, () -> System.out.println("Guest not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            case 4 -> {
                                quit = false;
                                while (true) {
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
                                            long id = create.createPodcast(connection, inputData.getPodcastInput(sc));
                                            System.out.println("Podcast created successfully with id " + id);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the podcast:");
                                            Long id = sc.nextLong();
                                            Optional<Podcast> resultPodcast = read.getPodcast(id, connection);
                                            resultPodcast.ifPresentOrElse(System.out::println, () -> System.out.println("Podcast not found!"));
                                            DB.closeConnection(connection);
                                        }
//                                        TODO Update and Delete
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            case 5 -> {
                                quit = false;
                                while (true) {
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
                                            long id = create.createHost(connection, inputData.getHostInput(sc));
                                            System.out.println("Host created successfully with id " + id);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            System.out.println("Enter the id of the host:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            case 6 -> {
                                quit = false;
                                while (true) {
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
                                            long id = create.createUser(connection, inputData.getUserInput(sc));
                                            System.out.println("User created successfully with id " + id);
                                            DB.closeConnection(connection);
                                        }
                                        case 2 -> {
                                            connection = DB.getConnection();
                                            //TODO for User
                                            System.out.println("Enter the id of the User:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
                                            DB.closeConnection(connection);
                                        }
                                        default -> {
                                            System.out.println("Please choose a value between 0 and 4...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
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
            if (quit) {
                sc.close();
                System.out.println("Hope you had a great experience..");
                break;
            }
        }
    }
}
