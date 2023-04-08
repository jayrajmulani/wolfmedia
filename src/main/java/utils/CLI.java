package utils;

import info.Create;
import info.Read;
import models.*;
import payments.PodcastPayments;
import payments.SongPayments;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class CLI {
    private static final Menu menu = new Menu();
    private static final Create create = new Create();
    private static final Read read = new Read();
    private static final InputData inputData = new InputData();
    private static final SongPayments songPayments = new SongPayments();
    private static final PodcastPayments podcastPayments = new PodcastPayments();
    private static PaymentUtils paymentUtils = new PaymentUtils();


    public void run() throws SQLException, ClassNotFoundException, ParseException, IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        Connection connection  = DB.getConnection();
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
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            // Create Song
                                            ;
                                            long id = create.createSong(connection, inputData.getSongInput(sc));
                                            System.out.println("Song created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            // Get Song
                                            System.out.println("Enter the id of the song:");
                                            Long id = sc.nextLong();
                                            Optional<Song> resultSong = read.getSong(id, connection);
                                            resultSong.ifPresentOrElse(System.out::println, () -> System.out.println("Song not found!"));
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
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createGuest(connection, inputData.getGuestInput(sc));
                                            System.out.println("Guest created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            System.out.println("Enter the id of the guest:");
                                            Long id = sc.nextLong();
                                            Optional<Guest> resultGuest = read.getGuest(id, connection);
                                            resultGuest.ifPresentOrElse(System.out::println, () -> System.out.println("Guest not found!"));
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
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createPodcast(connection, inputData.getPodcastInput(sc));
                                            System.out.println("Podcast created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            System.out.println("Enter the id of the podcast:");
                                            Long id = sc.nextLong();
                                            Optional<Podcast> resultPodcast = read.getPodcast(id, connection);
                                            resultPodcast.ifPresentOrElse(System.out::println, () -> System.out.println("Podcast not found!"));
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
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createHost(connection, inputData.getHostInput(sc));
                                            System.out.println("Host created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            System.out.println("Enter the id of the host:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createUser(connection, inputData.getUserInput(sc));
                                            System.out.println("User created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for User
                                            System.out.println("Enter the id of the User:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                            case 7 -> {
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createRecordLabel(connection, inputData.getRecordLabelInput(sc));
                                            System.out.println("Record Label created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for Record Label
                                            System.out.println("Enter the id of the Record Label:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                            case 8 -> {
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createService(connection, inputData.getServiceInput(sc));
                                            System.out.println("Service created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for Service
                                            System.out.println("Enter the id of the Service:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                            case 9 -> {
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createArtist(connection, inputData.getArtistInput(sc));
                                            System.out.println("Artist created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for Artist
                                            System.out.println("Enter the id of the Artist:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                            case 10 -> {
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createAlbum(connection, inputData.getAlbumInput(sc));
                                            System.out.println("Album created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for Album
                                            System.out.println("Enter the id of the Album:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                            case 11 -> {
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> {
                                            quit = true;
                                        }
                                        case 0 -> {
                                            goBackInner = true;
                                        }
                                        case 1 -> {
                                            long id = create.createAlbum(connection, inputData.getAlbumInput(sc));
                                            System.out.println("Album created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //TODO for Album
                                            System.out.println("Enter the id of the Album:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            resultHost.ifPresentOrElse(System.out::println, () -> System.out.println("Host not found!"));
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
                case 2 -> {
                    //TODO: Create Menu Items and develop logic for handling operations
                    System.out.println("Manage metadata and records");
                }
                case 3 -> {
                    // Maintain Payments Menu
                    int paymentChoice;
                    while(true){
                        menu.displayPaymentsMenu();
                        paymentChoice = sc.nextInt();
                        switch (paymentChoice){
                            case 1 -> {
                                // Make Royalty Payment for a song for the current month
                                long songId = inputData.getSongIdInput(connection, sc);
                                PaymentInfo royaltyInfo = songPayments.calculateRoyaltyAmount(connection, songId).orElseThrow();
                                System.out.println("Are you sure you want to pay " + royaltyInfo.getAmount()
                                        + " to record label with ID " + royaltyInfo.getReceiverId() + "? [0/1]");
                                int ch = sc.nextInt();
                                while(ch > 1 || ch < 0){
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if(ch == 0){
                                    System.out.println("Okay, cancelling transaction.");
                                }
                                else{
                                    paymentUtils.processPayment(connection, royaltyInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }
                            }
                            case 2 -> {
                                // TODO: Get Record Label's Payment History
                            }
                            case 3 -> {
                                // TODO: Make Payment to Artist for a song for the current month
                            }
                            case 4 -> {
                                // TODO: Get Artist's Payment History
                            }
                            case 5 -> {
                                // Make Payment to Podcast Host for current month
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                long episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                PaymentInfo hostPayInfo = podcastPayments.calculateHostPayAmount(connection, podcastId, episodeNum).orElseThrow();
                                System.out.println("Are you sure you want to pay " + hostPayInfo.getAmount()
                                        + " to host with ID " + hostPayInfo.getReceiverId() + "? [0/1]");
                                int ch = sc.nextInt();
                                while(ch > 1 || ch < 0){
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if(ch == 0){
                                    System.out.println("Okay, cancelling transaction.");
                                }
                                else{
                                    paymentUtils.processPayment(connection, hostPayInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }
                            }
                            case 6 -> {
                                // TODO: Get Payments History for Podcast Host
                            }
                            case 7 -> {
                                // TODO: Record Payment received from user for current month
                            }
                            case 8 -> {
                                // Get Balance for Service
                                double balance = paymentUtils.getBalanceForService(connection);
                                System.out.println("Current available balance for WolfMedia is $" + balance);
                            }
                        }
                    }

                }
                default -> {
                    System.out.println("Please choose a value between 0 and 4..");
                    menu.displayMainMenu();
                }
            }
            if (quit) {
                //Close the connection and scanner objects and quit.
                DB.closeConnection(connection);
                sc.close();
                System.out.println("Hope you had a great experience..");
                break;
            }
        }
    }
}
