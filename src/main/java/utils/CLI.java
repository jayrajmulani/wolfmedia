package utils;

import info.Create;
import info.Read;
import info.Update;
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
    private static final Update update = new Update();
    private static final InputData inputData = new InputData();
    private static final SongPayments songPayments = new SongPayments();
    private static final PodcastPayments podcastPayments = new PodcastPayments();
    private static final PaymentUtils paymentUtils = new PaymentUtils();
    private static final Constants constants = new Constants();
    private static final ReportUtils reportUtils = new ReportUtils();

    public void run() throws SQLException, ClassNotFoundException, ParseException, IllegalArgumentException {
        Scanner sc = new Scanner(System.in);
        Connection connection = DB.getConnection();
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
                            // Song
                            case 1 -> {
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
                                            long id = create.createSong(connection, inputData.getSongInput(sc, connection, true));
                                            if (id == 0) {
                                                System.out.println("Could not create song");
                                            } else {
                                                System.out.println("Song created successfully with id " + id);
                                            }
                                        }
                                        case 2 -> {
                                            // Get Song
                                            System.out.println("Enter the id of the song:");
                                            Long id = sc.nextLong();
                                            Optional<Song> resultSong = read.getSong(id, connection);
                                            resultSong.ifPresentOrElse(System.out::println, () -> System.out.println("Song not found!"));
                                        }
                                        case 3 -> {
                                            // Update Song
                                            long songID = inputData.getSongIdInput(connection, sc);
                                            update.updateSong(connection, inputData.getSongInput(sc, connection, false), songID);
                                        }
                                        case 4 -> {
                                            // TODO: Delete Song
                                            break;
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
                            // Guest
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
                                        case 3 -> {
                                            // TODO: Update Guest
                                            break;
                                        }
                                        case 4 -> {
                                            // TODO: Delete Guest
                                            break;
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
                            // Artist
                            case 3 -> {
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
                                            // TODO: Read Artist
                                            break;
                                        }
                                        case 3 -> {
                                            // TODO: Update Artist
                                            break;
                                        }
                                        case 4 -> {
                                            // TODO: Delete Artist
                                            break;
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
                            // Podcast
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
                                        case 3 -> {
                                            // TODO: Update Podcast
                                            break;
                                        }
                                        case 4 -> {
                                            // TODO: Delete Podcast
                                            break;
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
                            // Podcast Host
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
                                        case 3 -> {
                                            // TODO: Update Host
                                            break;
                                        }
                                        case 4 -> {
                                            // TODO: Delete Host
                                            break;
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
                            // User
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
                                            //TODO: Get User
                                            break;
                                        }
                                        case 3 -> {
                                            //TODO: Update User
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete User
                                            break;
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
                            // Record Label
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
                                            // TODO: Read Record Label
                                            break;
                                        }
                                        case 3 -> {
                                            //TODO: Update Record Label
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete Record Label
                                            break;
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
                            // Service
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
                                            // TODO: Read Service
                                            break;
                                        }
                                        case 3 -> {
                                            //TODO: Update Service
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete Service
                                            break;
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
                            // Album
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
                                            long id = create.createAlbum(connection, inputData.getAlbumInput(sc));
                                            System.out.println("Album created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            // TODO: Read Album
                                            break;
                                        }
                                        case 3 -> {
                                            //TODO: Update Album
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete Album
                                            break;
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
                            // Sponsor
                            case 10 -> {
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
                                            // Create
                                            long id = create.createSponsor(connection, inputData.getSponsorInput(sc));
                                            System.out.println("Sponsor created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            // Read
                                            System.out.println("Enter the id of the sponsor:");
                                            Long id = sc.nextLong();
                                            Optional<Sponsor> relationSponsor = read.getSponsor(id, connection);
                                            relationSponsor.ifPresentOrElse(System.out::println, () -> System.out.println("Sponsor not found!"));
                                        }
                                        case 3 -> {
                                            //TODO: Update Sponsor
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete Sponsor
                                            break;
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
                            // Episode
                            case 11 -> {
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
                                        case 3 -> {
                                            //TODO: Update Episode
                                            break;
                                        }
                                        case 4 -> {
                                            //TODO: Delete Episode
                                            break;
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
                            // Assign Song to Artist
                            // TODO: Refactor -> Do we need a CRUD Menu?
                            case 12 -> {
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
                            // Assign Artist to Album
                            // TODO: Refactor -> Do we need a CRUD Menu?
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
                                // Assign Song to Record Label
                            // TODO: We dont really need this operation at all. It should happen automatically
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
                            // Assign Artist to Record Label
                            // TODO: Refactor -> Do we need a CRUD Menu?
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
                                            long id = create.createAssignArtisttoRecordLabel(connection, inputData.getArtisttoRecordLabelInput(connection, sc));
                                            System.out.println("Above selected Artist is assigned to selected Record Label with id: " + id);
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
                            // Assign Song to Album
                            // TODO: Refactor -> Do we need a CRUD Menu?
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
                            // Handle Invalid Input
                            default -> {
                                System.out.println("Please choose a valid input...");
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
                            // Increase Play Count of a Song
                            case 1 -> {
                                inputData.increaseSongPlayCount(connection, sc);
                                System.out.println("Song Play Count increased successfully");
                            }
                            // Decrease Play Count of a Song
                            case 2 -> {
                                inputData.decreaseSongPlayCount(connection, sc);
                                System.out.println("Song Play Count decreased successfully");
                            }
                            // Get Play Count of a Song
                            case 3 -> {
                                long count = create.getSongPlayCountById(connection, inputData.getSongIdInput(connection, sc));
                                System.out.println("The Play Count for the song is: " + count);
                            }
                            // Increase Play Count of a Podcast
                            case 4 -> {
                                inputData.increasePodcastPlayCount(connection, sc);
                                System.out.println("Podcast Play Count increased successfully");
                            }
                            // Decrease Play Count of a Podcast
                            case 5 -> {
                                inputData.decreasePodcastPlayCount(connection, sc);
                                System.out.println("Podcast Play Count decreased successfully");
                            }
                            // Get Play Count of a Podcast
                            case 6 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                long episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastId).orElseThrow();
                                long count = create.getPodcastPlayCountById(connection, podcastId, episodeNum);
                                System.out.println("The Play Count for the Podcast is: " + count);
                            }
                            // Create/Update Record in Ratings Table
                            case 7 -> {
                                inputData.getRates(connection, sc);
                                System.out.println("Rating successfully added/updated");
                            }
                            // Get Average Rating for a Podcast
                            case 8 -> {
                                double rating = create.getAvgRating(connection, inputData.getAverageRating(connection, sc).orElseThrow());
                                System.out.println("Average Rating is: " + rating);
                            }
                            // Create Record in PodcastListen Table
                            case 9 -> {
                                inputData.increasePodcastSubscription(connection, sc);
                                System.out.println("Successfully Subscribed to the Podcast");
                            }
                            // Decrease Podcast subscriptions count
                            case 10 -> {
                                inputData.decreasePodcastSubscription(connection, sc);
                                System.out.println("Podcast Subscription Decreased");
                            }
                            // Get Subscribers count for a Podcast
                            case 11 -> {
                                long subsCount = create.getPodcastSubscriptionById(connection, inputData.getPodcastIdInput(connection, sc));
                                System.out.println("The Subscriber count is: " + subsCount);
                            }
                            // Handle Invalid input
                            default -> {
                                System.out.println("Please choose a valid input...");
                                menu.displayInfoProcessingMenu();
                                continue;
                            }
                        }
                        // Go Back / Quit
                        if (goBack || quit) {
                            break;
                        }
                    }
                }
                case 3 -> {
                    // Maintain Payments Menu
                    int paymentChoice;
                    while (true) {
                        menu.displayPaymentsMenu();
                        paymentChoice = sc.nextInt();
                        boolean goBack = false;
                        switch (paymentChoice) {
                            case -1 -> quit = true;
                            case 0 -> goBack = true;
                            // Make Royalty Payment for a song for the current month
                            case 1 -> {
                                long songId = inputData.getSongIdInput(connection, sc);
                                PaymentInfo royaltyInfo = songPayments.calculateRoyaltyAmount(connection, songId).orElseThrow();
                                System.out.println("Are you sure you want to pay " + royaltyInfo.getAmount()
                                        + " to record label with ID " + royaltyInfo.getReceiverId() + "? [0/1]");
                                int ch = sc.nextInt();
                                while (ch > 1 || ch < 0) {
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if (ch == 0) {
                                    System.out.println("Okay, cancelling transaction.");
                                } else {
                                    paymentUtils.processPayment(connection, royaltyInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }
                            }
                            // Get Record Label's Payment History
                            case 2 -> {
                                long recordLabelId = inputData.getRecordLabelIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = songPayments.getRecordLabelPaymentHistory(connection, recordLabelId);
                                paymentHistory.forEach(System.out::println);
                            }
                            //  Make Payment to Artists for a song for the current month
                            case 3 -> {
                                long songId = inputData.getSongIdInput(connection, sc);
                                List<PaymentInfo> royaltyInfoForArtists = songPayments.calculateRoyaltyAmountsForArtist(connection, songId);
                                PaymentInfo royaltyInfo = songPayments.calculateRoyaltyAmount(connection, songId).orElseThrow();
                                royaltyInfoForArtists.forEach(System.out::println);
                                System.out.println("Are you sure you want to pay " + royaltyInfo.getAmount() * constants.ARTIST_ROYALTY_SHARE
                                        + " to artists ? [0/1]");
                                int ch = sc.nextInt();
                                while (ch > 1 || ch < 0) {
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if (ch == 0) {
                                    System.out.println("Okay, cancelling transaction.");
                                } else {
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
                            // Get Artist's Payment History
                            case 4 -> {
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = songPayments.getArtistPaymentHistory(connection, artistId);
                                paymentHistory.forEach(System.out::println);
                            }
                            // Make Payment to Podcast Host for current month
                            case 5 -> {
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
                                while (ch > 1 || ch < 0) {
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if (ch == 0) {
                                    System.out.println("Okay, cancelling transaction.");
                                } else {
                                    paymentUtils.processPayment(connection, hostPayInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }
                            }
                            // Get Payments History for Podcast Host
                            case 6 -> {
                                long hostId = inputData.getHostIdInput(connection, sc);
                                List<PaymentHistoryItem> paymentHistory = podcastPayments.getHostPaymentHistory(connection, hostId);
                                paymentHistory.forEach(System.out::println);
                            }
                            // Record Payment received from user for current month
                            case 7 -> {
                                long userId = inputData.getUserIdInput(connection, sc);
                                long serviceId = inputData.getServiceIdInput(connection, sc);
                                User user = read.getUserById(connection, userId).orElseThrow();
                                if (!user.getPremiumStatus()) {
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
                                while (ch > 1 || ch < 0) {
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if (ch == 0) {
                                    System.out.println("Okay, cancelling transaction.");
                                } else {
                                    paymentUtils.processPayment(connection, paymentInfo);
                                    System.out.println("Payment Recorded Successfully");
                                }
                            }
                            // Get Balance for Service
                            case 8 -> {
                                double balance = paymentUtils.getBalanceForService(connection);
                                System.out.println("Current available balance for WolfMedia is $" + balance);
                            }
                            // Handle Invalid Input
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
                    while (true) {
                        boolean goBack = false;
                        menu.displayReportsMenu();
                        reportsChoice = sc.nextInt();
                        switch (reportsChoice) {
                            case -1 -> quit = true;
                            case 0 -> goBack = true;
                            // Historical Monthly Play Count for Songs
                            case 1 -> {
                                long songId = inputData.getSongIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getHistoricalSongPlayCountByMonth(connection, songId);
                                stats.forEach(System.out::println);
                            }
                            // Monthly Play Count for Song for current month
                            case 2 -> {
                                long songId = inputData.getSongIdInput(connection, sc);
                                long playCountForCurrentMonth = reportUtils.getSongPlayCountForCurrentMonth(connection, songId);
                                System.out.println("Play count for current month for song with id " + songId + " = " + playCountForCurrentMonth);
                            }
                            // Historical Monthly Play Count for Album
                            case 3 -> {
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getHistoricalAlbumPlayCount(connection, albumId);
                                stats.forEach(System.out::println);
                            }
                            // Monthly Play Count for Album for Current Month
                            case 4 -> {
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                long playCountForCurrentMonth = reportUtils.getAlbumPlayCountForCurrentMonth(connection, albumId);
                                System.out.println("Play count for current month for album with id " + albumId + " = " + playCountForCurrentMonth);
                            }
                            // Historical Monthly Play Count for Artist
                            case 5 -> {
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                List<Stats> stats = reportUtils.getHistoricalArtistPlayCount(connection, artistId);
                                stats.forEach(System.out::println);
                            }
                            // Monthly Play Count for Artist for Current Month
                            case 6 -> {
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                long playCountForCurrentMonth = reportUtils.getArtistPlayCountForCurrentMonth(connection, artistId);
                                System.out.println("Play count for current month for artist with id " + artistId + " = " + playCountForCurrentMonth);
                            }
                            // Total Payments Made to Host
                            case 7 -> {
                                PaymentReportInput input = inputData.getPaymentReportInputForHost(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Host with ID " + input.getId() + " was paid  $" + amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            //  Total Payments Made to Artist
                            case 8 -> {
                                PaymentReportInput input = inputData.getPaymentReportInputForArtist(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Artist with ID " + input.getId() + " was paid  $" + amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            // Total Payments Made to Record Label
                            case 9 -> {
                                PaymentReportInput input = inputData.getPaymentReportInputForRecordLabel(connection, sc);
                                double amount = reportUtils.generatePaymentReport(connection, input).orElseThrow();
                                System.out.println("Record Label with ID " + input.getId() + " was paid  $" + amount + " from " + input.getStartDate() + " to " + input.getEndDate());
                            }
                            // Monthly Revenue for Service
                            case 10 -> {
                                long id = inputData.getServiceIdInput(connection, sc);
                                reportUtils.getMonthlyRevenueForService(connection, id).forEach(System.out::println);
                            }
                            // Yearly Revenue for Service
                            case 11 -> {
                                long id = inputData.getServiceIdInput(connection, sc);
                                reportUtils.getYearlyRevenueForService(connection, id).forEach(System.out::println);
                            }
                            // Find Songs By Artist
                            case 12 -> {
                                long artistId = inputData.getArtistIdInput(connection,sc);
                                List<Song> songs = reportUtils.getSongsByArtist(connection,artistId);
                                songs.forEach(System.out::println);
                            }
                            // Find Songs By Album
                            case 13 -> {
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                List<Song> songs = reportUtils.getSongsByAlbum(connection,albumId);
                                songs.forEach(System.out::println);
                            }
                            // Find Episodes By Podcast
                            case 14 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                read.getAllPodcastEpisodes(connection, podcastId).forEach(System.out::println);
                            }
                            // Flush Play Count for Song for current month
                            case 15 -> {
                                long songId = inputData.getSongIdInput(connection,sc);
                                reportUtils.flushSongPlayCountForCurrentMonth(connection,songId);
                            }
                            // Historical Play Count for Episode
                            case 16 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if(episodeNumber.isEmpty()){
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                List<Stats> stats = reportUtils.getHistoricalEpisodePlayCountByMonth(connection, podcastId, episodeNumber.get());
                                stats.forEach(System.out::println);
                            }
                            //  Play Count for Episode for Current Month
                            case 17 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if(episodeNumber.isEmpty()){
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                long playCountForCurrentMonth = reportUtils.getEpisodePlayCountForCurrentMonth(connection, podcastId, episodeNumber.get());
                                System.out.println("Play count for current month for episode with podcast id " + podcastId + "and episode number " + episodeNumber + " = " + playCountForCurrentMonth);

                            }
                            //  Flush Play Count for Episode for current month
                            case 18 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if(episodeNumber.isEmpty()){
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                reportUtils.flushEpisodePlayCountForCurrentMonth(connection,podcastId, episodeNumber.get());
                            }
                            // Handle Invalid inputs
                            default -> {
                                System.out.println("Please choose a valid input...");
                                continue;
                            }
                        }
                        // Go Back / Quit
                        if (goBack || quit) {
                            break;
                        }

                    }
                }
                // Handle Invalid Inputs
                default -> {
                    System.out.println("Please choose a valid input...");
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
