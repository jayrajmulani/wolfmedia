package utils;

public class Menu {
    public void displayMainMenu(){
        System.out.println("-------------------------");
        System.out.println("Welcome to WolfMedia!");
        System.out.println("-------------------------");
        System.out.println("1. Information Processing");
        System.out.println("2. Manage metadata and records");
        System.out.println("3. Manage payments:");
        System.out.println("4. Reports");
        System.out.println("0. Quit!");
        System.out.println("Enter your choice: ");
    }
    public void displayInfoProcessingMenu(){
        System.out.println("-------------------------");
        System.out.println("Information Processing");
        System.out.println("-------------------------");
        System.out.println("1. Song");
        System.out.println("2. Guest");
        System.out.println("3. Artist");
        System.out.println("4. Podcast");
        System.out.println("0. Go back!");
        System.out.print("Enter your choice: ");
    }
    public void displayCrudMenu(){
        System.out.println("1. Create");
        System.out.println("2. Get");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("0. Go back!");
        System.out.print("Enter your choice: ");
    }
}
