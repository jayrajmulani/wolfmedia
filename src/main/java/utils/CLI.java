package utils;

import info.Create;
import info.Delete;
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
    private static final Delete delete = new Delete();
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
                                            long id = create.createSong(connection, inputData.getSongInput(connection));
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

                                            long id = inputData.getSongIdInput(connection, sc);
                                            Song song = inputData.getSongInputForUpdate(connection);
                                            song.setId(id);
                                            update.updateSong(connection, song);
                                            System.out.println("Song updated successfully");
                                        }
                                        case 4 -> {
                                            // Delete Song
                                            System.out.println("FYI, Deleting a Song will delete all the records of the song where SongId is a foreign key");
                                            delete.deleteSong(connection, inputData.getSongIdInput(connection, sc));
                                            System.out.println("Song successfully deleted");
                                        }
                                        default -> {
                                            // Default option if none of the above cases selected
                                            System.out.println("Please choose a valid input...");
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
                                            //Creating Guest
                                            long id = create.createGuest(connection, inputData.getGuestInput());
                                            System.out.println("Guest created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Getting Guest Details by Id
                                            System.out.println("Enter the id of the guest:");
                                            long id = sc.nextLong();
                                            Optional<Guest> resultGuest = read.getGuest(id, connection);
                                            resultGuest.ifPresentOrElse(System.out::println, () -> System.out.println("Guest not found!"));
                                        }
                                        case 3 -> {
                                            //Updating Guest details
                                            long id = inputData.getGuestIdInput(connection, sc);
                                            Guest guest = inputData.getGuestInput();
                                            guest.setId(id);
                                            update.updateGuest(connection, guest);
                                            System.out.println("Guest updated successfully");
                                        }
                                        case 4 -> {
                                            // Deleting Guest
                                            System.out.println("FYI, Deleting a Guest will delete all the records of the guest where guestId is a foreign key");
                                            delete.deleteGuest(connection, inputData.getGuestIdInput(connection, sc));
                                            System.out.println("Guest successfully deleted");
                                            break;
                                        }
                                        default -> {
                                            // Default option selected, if none of the above options are selected
                                            System.out.println("Please choose a valid input...");
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
                                            //Create Artist
                                            long id = create.createArtist(connection, inputData.getArtistInput(connection));
                                            System.out.println("Artist created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Get Artist details by Id
                                            long id = inputData.getArtistIdInput(connection, sc);
                                            Artist artist = read.getArtist(connection, id).orElseThrow();
                                            System.out.println(artist);
                                        }
                                        case 3 -> {
                                            //Update Artist details
                                            long id = inputData.getArtistIdInput(connection, sc);
                                            Artist artist = inputData.getArtistInputForUpdate();
                                            artist.setId(id);
                                            update.updateArtist(connection, artist);
                                        }
                                        case 4 -> {
                                            //Delete Artist details
                                            System.out.println("FYI, Deleting an Artist will delete all the records of the artist where artistId is a foreign key");
                                            delete.deleteArtist(connection, inputData.getArtistIdInput(connection, sc));
                                            System.out.println("Artist successfully deleted");
                                        }
                                        default -> {
                                            //Default option selcted if none of the above options selected
                                            System.out.println("Please choose a valid input...");
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
                                            // Create Podcast
                                            long id = create.createPodcast(connection, inputData.getPodcastInput(sc, connection));
                                            System.out.println("Podcast created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            // Get Podcast details by Id
                                            System.out.println("Enter the id of the podcast:");
                                            Long id = sc.nextLong();
                                            Optional<Podcast> resultPodcast = read.getPodcast(id, connection);

                                            if (resultPodcast.isPresent()) {
                                                System.out.println(resultPodcast.get());
                                                System.out.println("Episode count:" + resultPodcast.get().getEpisodes().size());
                                            } else {
                                                System.out.println("Podcast not found!");
                                            }
                                        }
                                        case 3 -> {
                                            long id = inputData.getPodcastIdInput(connection, sc);
                                            Podcast podcast = inputData.getPodcastInput(sc, connection);
                                            podcast.setId(id);
                                            update.updatePodcast(connection, podcast);
                                        }
                                        case 4 -> {
                                            // Delete Podcast
                                            System.out.println("FYI, Deleting a Podcast will delete all the records of the podcast where podcastId is a foreign key");
                                            delete.deletePodcast(connection, inputData.getPodcastIdInput(connection, sc));
                                            System.out.println("Podcast successfully deleted");
                                        }

                                        default -> {
                                            System.out.println("Please choose a valid input...");
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
                                            //Create Host
                                            long id = create.createHost(connection, inputData.getHostInput());
                                            System.out.println("Host created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Get Host details by Id
                                            System.out.println("Enter the id of the host:");
                                            Long id = sc.nextLong();
                                            Optional<Host> resultHost = read.getHost(id, connection);
                                            if (resultHost.isPresent()) {
                                                System.out.println(resultHost.get());
                                                System.out.println("Podcasts by this host:");
                                                read.getPodcastsByHostId(connection, id).forEach(System.out::println);
                                            }
                                        }
                                        case 3 -> {
                                            //Uodate Host
                                            long id = inputData.getHostIdInput(connection, sc);
                                            Host host = inputData.getHostInput();
                                            host.setId(id);
                                            update.updateHost(connection, host);
                                            System.out.println("Host updated successfully");
                                        }
                                        case 4 -> {
                                            //Delete Host
                                            System.out.println("FYI, Deleting a Host will delete all the records of the host where hostId is a foreign key");
                                            delete.deleteHost(connection, inputData.getHostIdInput(connection, sc));
                                            System.out.println("Podcast Host successfully deleted");
                                        }
                                        default -> {
                                            System.out.println("Please choose a valid input...");
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
                                            //Create User
                                            long id = create.createUser(connection, inputData.getUserInput(sc));
                                            System.out.println("User created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Get User details by Id
                                            System.out.println("Enter the id of the User:");
                                            long id = sc.nextLong();
                                            Optional<User> resultUser = read.getUserById(connection, id);
                                            resultUser.ifPresentOrElse(System.out::println, () -> System.out.println("Guest not found!"));
                                        }
                                        case 3 -> {
                                            //Update User details
                                            long id = inputData.getUserIdInput(connection, sc);
                                            User user = inputData.getUserInput(sc);
                                            user.setId(id);
                                            update.updateUser(connection, user);
                                            System.out.println("User updated successfully");
                                        }
                                        case 4 -> {
                                            //Delete User details
                                            System.out.println("FYI, Deleting a User will delete all the records of the user where userId is a foreign key");
                                            delete.deleteUser(connection, inputData.getUserIdInput(connection, sc));
                                            System.out.println("User successfully deleted");
                                        }
                                        default -> {
                                            //Default option choosen if none of the above cases selected
                                            System.out.println("Please choose a valid input...");
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
                                            //Create Record Label
                                            long id = create.createRecordLabel(connection, inputData.getRecordLabelInput(sc));
                                            System.out.println("Record Label created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Get Record label by Id
                                            long id = inputData.getRecordLabelIdInput(connection, sc);
                                            read.displayRecordLabelById(connection, id);
                                        }
                                        case 3 -> {
                                            //Update Record Label
                                            long id = inputData.getRecordLabelIdInput(connection, sc);
                                            RecordLabel recordLabel = inputData.getRecordLabelInput(sc);
                                            recordLabel.setId(id);
                                            update.updateRecordLabel(connection, recordLabel);
                                            System.out.println("Record Label updated successfully");
                                        }
                                        case 4 -> {
                                            //Delete Record Label
                                            System.out.println("FYI, Deleting a Record Label will delete all the records of the song where recordLabelId is a foreign key");
                                            delete.deleteRecordLabel(connection, inputData.getRecordLabelIdInput(connection, sc));
                                            System.out.println("Record Label successfully deleted");
                                        }
                                        default -> {
                                            System.out.println("Please choose a valid input...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            // Album
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
                                            //Create Album
                                            long id = create.createAlbum(connection, inputData.getAlbumInput(sc));
                                            System.out.println("Album created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            //Get Album details by Id
                                            long id = inputData.getAlbumIdInput(connection, sc);
                                            read.getAlbum(id, connection);
                                        }
                                        case 3 -> {
                                            //Update Album details
                                            long id = inputData.getAlbumIdInput(connection, sc);
                                            Album album = inputData.getAlbumInput(sc);
                                            album.setId(id);
                                            update.updateAlbum(connection, album);
                                        }
                                        case 4 -> {
                                            //Delete Album details
                                            System.out.println("FYI, Deleting an Album will delete all the records of the album where albumId is a foreign key");
                                            delete.deleteAlbum(connection, inputData.getAlbumIdInput(connection, sc));
                                            System.out.println("Album successfully deleted");
                                        }
                                        default -> {
                                            //default option choosen if none of the above options are selected
                                            System.out.println("Please choose a valid input...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            // Sponsor
                            case 9 -> {
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
                                            // Create Sponsor
                                            long id = create.createSponsor(connection, inputData.getSponsorInput(sc));
                                            System.out.println("Sponsor created successfully with id " + id);
                                        }
                                        case 2 -> {
                                            // Get Sponsor details by Id
                                            System.out.println("Enter the id of the sponsor:");
                                            Long id = sc.nextLong();
                                            Optional<Sponsor> relationSponsor = read.getSponsor(id, connection);
                                            relationSponsor.ifPresentOrElse(System.out::println, () -> System.out.println("Sponsor not found!"));
                                        }
                                        case 3 -> {
                                            //Update Sponsor details
                                            long id = inputData.getSponsorIdInput(connection, sc);
                                            Sponsor sponsor = inputData.getSponsorInput(sc);
                                            sponsor.setId(id);
                                            System.out.println(sponsor);
                                            update.updateSponsor(connection, sponsor);
                                        }
                                        case 4 -> {
                                            //Delete Sponsor details
                                            System.out.println("FYI, Deleting a Sponsor will delete all the records of the sponsor where sponsorId is a foreign key");
                                            delete.deleteSponsor(connection, inputData.getSponsorIdInput(connection, sc));
                                            System.out.println("Podcast Sponsor successfully deleted");
                                        }
                                        default -> {
                                            //default option choosen if none of the above options are selected
                                            System.out.println("Please choose a valid input...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            // Episode
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
                                            //Create Episode
                                            long podcastID = inputData.getPodcastIdInput(connection, sc);
                                            long id = create.createEpisode(connection, inputData.getEpisodeInput(connection, sc, podcastID));
                                            create.createEpisodeGuest(connection, inputData.getEpisodeGuestInput(connection, sc, podcastID, id));
                                            System.out.println("Episode created successfully with number " + id);
                                        }
                                        case 2 -> {
                                            //Get Episode details by Id
                                            long podcastID = inputData.getPodcastIdInput(connection, sc);
                                            Optional<Long> episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastID);
                                            if (episodeNum.isPresent()) {
                                                Optional<Episode> relationEpisode = read.getEpisode(connection, podcastID, episodeNum.get());
                                                relationEpisode.ifPresentOrElse(System.out::println, () -> System.out.println("Episode not found!"));
                                                Optional<Guest> guest = read.getGuestDetails(connection, podcastID, episodeNum.get());
                                                if (guest.isPresent()) {
                                                    System.out.println("The Guest associated with the Song is " + guest.get().getName());
                                                }
                                            } else {
                                                System.out.println("Podcast doesn't have any episodes");
                                            }
                                        }
                                        case 3 -> {
                                            //Update Episode details
                                            long podcastId = inputData.getPodcastIdInput(connection, sc);
                                            long episodeNum = inputData.getEpisodeNumberInput(connection, sc, podcastId).orElseThrow();
                                            Episode episode = inputData.getEpisodeInput(connection, sc, podcastId);
                                            episode.setEpisodeNum(episodeNum);
                                            update.updateEpisode(connection, episode);
                                            System.out.println("Episode updated successfully");

                                        }
                                        case 4 -> {
                                            //Delete Episode details
                                            long podcastId = inputData.getPodcastIdInput(connection, sc);
                                            long episode_num = inputData.getEpisodeNumberInput(connection, sc, podcastId).orElseThrow();
                                            delete.deleteEpisode(connection, podcastId, episode_num);
                                            System.out.println("Episode successfully deleted");
                                        }
                                        default -> {
                                            System.out.println("Please choose a valid input...");
                                            continue;
                                        }
                                    }
                                    if (goBackInner || quit) {
                                        break;
                                    }
                                }
                            }
                            // Assign Song to Artist
                            case 11 -> {
                                create.createAssignSongtoArtist(connection, inputData.getSongToArtistInput(connection, sc));
                                System.out.println("Above selected Song is assigned to selected Artist");
                            }
                            // Assign Artist to Album
                            case 12 -> {
                                create.createAssignArtisttoAlbum(connection, inputData.getArtisttoAlbumInput(connection, sc));
                                System.out.println("Above selected Artist is assigned to selected Album");
                            }
                            // Assign Artist to Record Label
                            case 13 -> {
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                create.createAssignArtisttoRecordLabel(connection, inputData.getArtisttoRecordLabelInput(connection, sc, artistId));
                                System.out.println("Above selected Artist is assigned to selected Record Label");
                            }
                            // Assign Song to Album
                            case 14 -> {
                                create.createAssignSongtoAlbum(connection, inputData.getSongtoAlbumInput(connection, sc).orElseThrow());
                                System.out.println("Above selected Song is assigned to selected Album");
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
                                    connection.setAutoCommit(false);
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
                                if (episodeNum.isEmpty()) {
                                    System.out.println("This Podcast has no Episodes");
                                    break;
                                }
                                List<PaymentInfo> hostPayInfo = podcastPayments.calculateHostPayAmount(connection, podcastId, episodeNum.get());
                                if (hostPayInfo.size() == 0) {
                                    System.out.println("No Payments to process..");
                                    break;
                                }
                                System.out.println("Are you sure you want to process all payments ?[0/1]");
                                int ch = sc.nextInt();
                                while (ch > 1 || ch < 0) {
                                    System.out.println("Please enter 0 or 1");
                                    ch = sc.nextInt();
                                }
                                if (ch == 0) {
                                    System.out.println("Okay, cancelling transaction.");
                                } else {
                                    hostPayInfo.forEach(paymentInfo -> {
                                        try {
                                            paymentUtils.processPayment(connection, paymentInfo);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                    System.out.println("Payments Recorded Successfully");
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
                                long artistId = inputData.getArtistIdInput(connection, sc);
                                List<Song> songs = reportUtils.getSongsByArtist(connection, artistId);
                                songs.forEach(System.out::println);
                            }
                            // Find Songs By Album
                            case 13 -> {
                                long albumId = inputData.getAlbumIdInput(connection, sc);
                                List<Song> songs = reportUtils.getSongsByAlbum(connection, albumId);
                                songs.forEach(System.out::println);
                            }
                            // Find Episodes By Podcast
                            case 14 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                read.getAllPodcastEpisodes(connection, podcastId).forEach(System.out::println);
                            }
                            // Flush Play Count for Song for current month
                            case 15 -> {
                                long songId = inputData.getSongIdInput(connection, sc);
                                reportUtils.flushSongPlayCountForCurrentMonth(connection, songId);
                            }
                            // Historical Play Count for Episode
                            case 16 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if (episodeNumber.isEmpty()) {
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                List<Stats> stats = reportUtils.getHistoricalEpisodePlayCountByMonth(connection, podcastId, episodeNumber.get());
                                stats.forEach(System.out::println);
                            }
                            //  Play Count for Episode for Current Month
                            case 17 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if (episodeNumber.isEmpty()) {
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                long playCountForCurrentMonth = reportUtils.getEpisodePlayCountForCurrentMonth(connection, podcastId, episodeNumber.get());
                                System.out.println("Play count for current month for episode with podcast id " + podcastId + "and episode number " + episodeNumber + " = " + playCountForCurrentMonth);

                            }
                            //  Flush Play Count for Episode for current month
                            case 18 -> {
                                long podcastId = inputData.getPodcastIdInput(connection, sc);
                                Optional<Long> episodeNumber = inputData.getEpisodeNumberInput(connection, sc, podcastId);
                                if (episodeNumber.isEmpty()) {
                                    System.out.println("This podcast doesnt have any episodes.");
                                }
                                reportUtils.flushEpisodePlayCountForCurrentMonth(connection, podcastId, episodeNumber.get());
                            }
                            case 19 -> {
                                long id = inputData.getArtistIdInput(connection, sc);
                                long count = read.getArtistMonthlyListener(connection, id);
                                System.out.println("Monthly Listeners for this artist is " + count);
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
