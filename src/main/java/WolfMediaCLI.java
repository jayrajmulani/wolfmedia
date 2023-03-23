import info.Create;
import info.Read;
import models.Guest;
import utils.DB;
import  utils.Menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class WolfMediaCLI {
    private static Menu menu = new Menu();
    private static DB db = new DB();
    private static Create create = new Create();
    private static Read read = new Read();
//    private static Create.Type type;
    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("Hello");
        menu.displayMainMenu();
        while(true){
            choice = sc.nextInt();
            if(choice < 0 || choice > 4){
                System.out.println("Please choose a value between 0 and 4..");
                menu.displayMainMenu();
                continue;
            }
            if(choice == 0){
                System.out.println("Hope you had a great experience..");
                break;
            }
            Connection connection;
            switch(choice){

                case 1:
                    int crudChoice;
                    menu.displayInfoProcessingMenu();
                    while(true){
                        boolean goBack = false;
                        int infoChoice = sc.nextInt();
                        switch(infoChoice){
                            case 0:
                                menu.displayMainMenu();
                                goBack = true;
                                break;
                            case 2:
                                menu.displayCrudMenu();
                                crudChoice = sc.nextInt();
                                if(crudChoice < 0 || crudChoice > 10){
                                    System.out.println("Please choose a value between 0 and 4...");
                                    menu.displayInfoProcessingMenu();
                                    continue;
                                }
                                if(crudChoice == 0){
                                    menu.displayInfoProcessingMenu();
                                    break;
                                }
                                switch (crudChoice){
                                    case 1:
                                        connection = db.getConnection();
                                        System.out.println("Enter the name of the guest:");
                                        String name = sc.next();
                                        Guest guest = new Guest(name);
                                        create.createGuest(connection, guest);
                                        db.closeConnection(connection);
                                        break;
                                    case 2:
                                        connection = db.getConnection();
                                        System.out.println("Enter the id of the guest:");
                                        Long id = sc.nextLong();
                                        Optional<Guest> resultGuest = read.getGuest(id, connection);
                                        if(resultGuest.isPresent()){
                                            System.out.println(resultGuest.get());
                                        }
                                        else {
                                            System.out.println("Guest not found!");
                                        }
                                        db.closeConnection(connection);
                                        break;
                                }
                                break;
                            default:
                                System.out.println("Please choose a value between 0 and 10...");
                                menu.displayInfoProcessingMenu();
                        }
                        if(goBack){
                            break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Manage metadata and records");
                    break;
            }

        }

    }
}
