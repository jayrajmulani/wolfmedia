package utils;

import info.Create;
import info.Read;
import models.*;
import org.checkerframework.checker.units.qual.C;
import payments.PodcastPayments;
import payments.SongPayments;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
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
    private static final PaymentUtils paymentUtils = new PaymentUtils();
    private static final Constants constants = new Constants();
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
                                // Get Record Label's Payment History
                                long recordLabelId= inputData.getRecordLabelIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = songPayments.getRecordLabelPaymentHistory(connection, recordLabelId);
                                paymentHistory.forEach(System.out::println);
                            }
                            case 3 -> {
                                //  Make Payment to Artists for a song for the current month
                                long songId = inputData.getSongIdInput(connection, sc);
                                List<PaymentInfo> royaltyInfoForArtists = songPayments.calculateRoyaltyAmountsForArtist(connection, songId);
                                PaymentInfo royaltyInfo = songPayments.calculateRoyaltyAmount(connection, songId).orElseThrow();
                                royaltyInfoForArtists.forEach(System.out::println);
                                System.out.println("Are you sure you want to pay " + royaltyInfo.getAmount() * constants.ARTIST_ROYALTY_SHARE
                                        + " to artists ? [0/1]");
                                int ch = sc.nextInt();
                                while(ch > 1 || ch < 0){
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if(ch == 0){
                                    System.out.println("Okay, cancelling transaction.");
                                }
                                else{
                                    royaltyInfoForArtists.forEach(paymentInfo -> {
                                        try {
                                            paymentUtils.processPayment(connection, paymentInfo);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                    System.out.println("Payments Recorded Successfully");
                                }
                            }
                            case 4 -> {
                                // Get Artist's Payment History
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = songPayments.getArtistPaymentHistory(connection, artistId);
                                paymentHistory.forEach(System.out::println);
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
                                // Get Payments History for Podcast Host
                                long hostId= inputData.getHostIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = podcastPayments.getHostPaymentHistory(connection, hostId);
                                paymentHistory.forEach(System.out::println);
                            }
                            case 7 -> {
                                // Record Payment received from user for current month
                                long userId = inputData.getUserIdInput(connection, sc);
                                long serviceId = inputData.getServiceIdInput(connection, sc);
                                User user = read.getUserById(connection, userId).orElseThrow();
                                if(!user.getPremiumStatus()){
                                    System.out.println("This user is not a premium member. Can't process payment");
                                    break;
                                }
                                PaymentInfo paymentInfo = new PaymentInfo(
                                        userId,
                                        serviceId,
                                        user.getMonthlyPremiumFees(),
                                        PaymentUtils.Stakeholder.USER,
                                        PaymentUtils.Stakeholder.SERVICE
                                );
                                System.out.println("Are you sure you want to process " + user.getMonthlyPremiumFees()
                                        + " from user " + user.getId() + "? [0/1]");
                                int ch = sc.nextInt();
                                while(ch > 1 || ch < 0){
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if(ch == 0){
                                    System.out.println("Okay, cancelling transaction.");
                                }
                                else{
                                    paymentUtils.processPayment(connection, paymentInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }

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
