package utils;

import info.Create;
import info.Read;
import models.*;
import payments.PodcastPayments;
import payments.SongPayments;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
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
    private static final ReportUtils reportUtils = new ReportUtils();
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
                            case 0 -> goBack = true;
                            case 1 -> {
                                // Song Menu
                                quit = false;
                                while (true) {
                                    boolean goBackInner = false;
                                    menu.displayCrudMenu();
                                    crudChoice = sc.nextInt();
                                    switch (crudChoice) {
                                        case -1 -> quit = true;
                                        case 0 -> goBackInner = true;
                                        case 1 -> {
                                            // Create Song
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
                                        case -1 -> quit = true;
                                        case 0 -> goBackInner = true;
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
                                        case -1 -> quit = true;
                                        case 0 -> goBackInner = true;
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
                                //Sponsor
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
                                            //Create
                                            long id = create.createSponsor(connection, inputData.getSponsorInput(sc));
                                            System.out.println("Sponsor created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Read
                                            System.out.println("Enter the id of the sponsor:");
                                            Long id = sc.nextLong();
                                            Optional<Sponsor> relationSponsor = read.getSponsor(id, connection);
                                            relationSponsor.ifPresentOrElse(System.out::println, () -> System.out.println("Sponsor not found!"));
                                        }
                                        //TODO
//                                        case 3 ->{
//                                            //Update
//                                            System.out.println("Enter id of the podcast:");
//                                            Long id = sc.nextLong();
//                                            Optional<Podcast> resultPodcast = read.getPodcast(id, connection);
//
//                                        }
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
                            case 12 -> {
                                //Episode
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
                                            //Create
                                            long podcastID = inputData.getPodcastIdInput(connection, sc);
                                            long id = create.createEpisode(connection, inputData.getEpisodeInput(sc, podcastID));
                                            System.out.println("Episode created successfully with number " + id);
                                        }
                                        case 2 -> {
                                            //Read
                                            long podcastID = inputData.getPodcastIdInput(connection, sc);
                                            Optional<Long> episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastID);
                                            if(episodeNum.isPresent()){
                                                Optional<Episode> relationEpisode = read.getEpisode(connection, podcastID, episodeNum.get());
                                                relationEpisode.ifPresentOrElse(System.out::println, () -> System.out.println("Episode not found!"));
                                            }
                                            else{
                                                System.out.println("Podcast doesn't have any episodes");
                                            }
                                        }
                                        //TODO
//                                        case 3 ->{
//                                            //Update
//                                            System.out.println("Enter id of the podcast:");
//                                            Long id = sc.nextLong();
//                                            Optional<Podcast> resultPodcast = read.getPodcast(id, connection);
//
//                                        }
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
                            case 13 -> {
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
                                            create.createAssignSongtoArtist(connection, inputData.getSongToArtistInput(connection, sc));
                                            System.out.println("Above selected Song is assigned to selected Artist");
                                        }
                                        case 2 -> {
                                            System.out.println("Enter the id of the SongListen table:");
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
                            case 14 -> {
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
                                            create.createAssignArtisttoAlbum(connection, inputData.getArtisttoAlbumInput(connection, sc));
                                            System.out.println("Above selected Artist is assigned to selected Album");
                                        }
                                        case 2 -> {
                                            //TODO for Song_Listen
                                            System.out.println("Enter the id of the SongListen table:");
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
                            case 15 -> {
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
                                            create.createAssignSongtoRecordLabel(connection, inputData.getSongtoRecordLabelInput(connection, sc).orElseThrow());
                                            System.out.println("Above selected Song is assigned to selected Record Label");
                                        }
                                        case 2 -> {
                                            //TODO for Owns
                                            System.out.println("Enter the id of the SongListen table:");
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
                            case 16 -> {
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
                                            long id = create.createAssignArtisttoRecordLabel(connection, inputData.getArtisttoRecordLabelInput(connection, sc));
                                            System.out.println("Above selected Artist is assigned to selected Record Label with id: "+ id);
                                        }
                                        case 2 -> {
                                            //TODO for Signs
                                            System.out.println("Enter the id of the SongListen table:");
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
                            case 17 -> {
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
                                            create.createAssignSongtoAlbum(connection, inputData.getSongtoAlbumInput(connection, sc).orElseThrow());
                                            System.out.println("Above selected Song is assigned to selected Record Label");
                                        }
                                        case 2 -> {
                                            //TODO for Signs
                                            System.out.println("Enter the id of the SongListen table:");
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
                    // Maintaining metadata and records
                    while (true) {
                        quit = false;
                        menu.displayMetaDataAndRecordsMenu();
                        boolean goBack = false;
                        int infoChoice = sc.nextInt();
                        switch (infoChoice) {
                            case -1 -> quit = true;
                            case 0 -> goBack = true;
                            case 1 -> {
                                // Increase Playcount of a Song
                                inputData.increaseSongPlayCount(connection, sc);
                                System.out.println("Song Playcount increased");
                            }
                            case 2 -> {
                                // Decrease Playcount of a Song
                                inputData.decreaseSongPlaycount(connection, sc);
                                System.out.println("Song Playcount decreased");
                            }
                            case 3 -> {
                                // Get Playcount of a Song
                                long count = create.getSongPlayCountById(connection, inputData.getSongIdInput(connection, sc));
                                System.out.println("The Playcount for the song is" + count);
                            }
                            case 4 -> {
                                // Increase playcount of a Podcast
                                inputData.increasePodcastPlayCount(connection, sc);
                                System.out.println("Podcast Playcount increased");
                            }
                            case 5 -> {
                                // Decrease playcount of a Podcast
                                inputData.decreasePodcastPlaycount(connection, sc);
                                System.out.println("Podcast Playcount decreased");
                            }
                            case 6 -> {
                                // // Get Playcount of a Podcast
                                long count = create.getPodcastPlayCountById(connection, inputData.getPodcastIdInput(connection, sc));
                                System.out.println("The Playcount for the song is" + count);
                            }
                            case 7 -> {
                                // Get Average Rating for a Podcast
                                double rating = create.getAvgRating(connection, inputData.getAverageRating(connection, sc).orElseThrow());
                                System.out.println("Average Rating is: " + rating);
                            }
                            case 8 -> {
                                // Create Record in PodcastListen Table
                                inputData.increasePodcastSubscription(connection, sc);
                                System.out.println("Successfully Subscribed to the Podcast");
                            }
                            case 9 -> {
                                // Delete Podcast subscriptions count
                                inputData.decreasePodcastSubscription(connection, sc);
                                System.out.println("Podcast Subscription Decreased");
                            }
                            case 10 -> {
                                // Get Subscribers count for a Podcast
                                long subsCount = create.getPodcastSubscriptionById(connection, inputData.getPodcastIdInput(connection, sc));
                                System.out.println("The Subscriber count is: " + subsCount);
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
                case 3 -> {
                    // Maintain Payments Menu
                    int paymentChoice;
                    while(true){
                        menu.displayPaymentsMenu();
                        paymentChoice = sc.nextInt();
                        boolean goBack = false;
                        switch (paymentChoice){
                            case -1 -> quit = true;
                            case 0 -> goBack = true;
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
                                Optional<Long> episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if(episodeNum.isEmpty()){
                                    System.out.println("This Podcast has no Episodes");
                                    break;
                                }
                                PaymentInfo hostPayInfo = podcastPayments.calculateHostPayAmount(connection, podcastId, episodeNum.get()).orElseThrow();
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
                            default -> {
                                System.out.println("Please choose a valid input...");
                                continue;
                            }
                        }
                        if (goBack || quit) {
                            break;
                        }
                    }
                }
                case 4 -> {
                    int reportsChoice;
                    while (true){
                        boolean goBack = false;
                        menu.displayReportsMenu();
                        reportsChoice = sc.nextInt();
                        switch (reportsChoice){
                            case -1 -> quit = true;
                            case 0 -> goBack = true;
                            case 1 -> {
                                // Monthly Play Count for Songs
                                long songId = inputData.getSongIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getSongPlayCountByMonth(connection, songId);
                                stats.forEach(System.out::println);
                            }
                            case 2 -> {
                                // Monthly Play Count for Album
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getAlbumPlayCountByMonth(connection, albumId);
                                stats.forEach(System.out::println);
                            }
                            case 3 -> {
                                // Monthly Play Count for Artist
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getArtistPlayCountByMonth(connection, artistId);
                                stats.forEach(System.out::println);
                            }
                            case 4 -> {
                                // Total Payments Made to Host
                                PaymentReportInput input = inputData.getPaymentReportInputForHost(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Host with ID " + input.getId() + " was paid  $"+ amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            case 5 -> {
                                //  Total Payments Made to Artist
                                PaymentReportInput input = inputData.getPaymentReportInputForArtist(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Artist with ID " + input.getId() + " was paid  $"+ amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            case 6 -> {
                                // Total Payments Made to Record Label
                                PaymentReportInput input = inputData.getPaymentReportInputForRecordLabel(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Record Label with ID " + input.getId() + " was paid  $"+ amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            case 7 -> {
                                // Monthly Revenue for Service
                                long id = inputData.getServiceIdInput(connection, sc);
                                reportUtils.getMonthlyRevenueForService(connection, id).forEach(System.out::println);
                            }
                            case 8 -> {
                                // Yearly Revenue for Service
                                long id = inputData.getServiceIdInput(connection, sc);
                                reportUtils.getYearlyRevenueForService(connection, id).forEach(System.out::println);
                            }
                            case 9 -> {
                                // Find Songs By Artist
                                long artistId = inputData.getArtistIdInput(connection,sc);
                                List<Song> songs = reportUtils.getSongsByArtist(connection,artistId);
                                songs.forEach(System.out::println);
                            }
                            case 10 -> {
                                // Find Songs By Album
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                List<Song> songs = reportUtils.getSongsByAlbum(connection,albumId);
                                songs.forEach(System.out::println);
                            }
                            case 11 -> {
                                // Find Episodes By Podcast
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                read.getAllPodcastEpisodes(connection, podcastId).forEach(System.out::println);
                            }
                            case 12 -> {
                                long songId = inputData.getSongIdInput(connection,sc);
                                reportUtils.flushSongPlayCountForCurrentMonth(connection,songId);
                            }
                            case 13 -> {
                                // TODO: Flush Play Count for Episode for current month
                                break;
                            }
                            default -> {
                                System.out.println("Please choose a valid input...");
                                continue;
                            }
                        }
                        if (goBack || quit) {
                            break;
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
