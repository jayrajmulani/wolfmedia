package utils;

public class Menu {
    public void displayRoleMenu(){
        System.out.println("-------------------------");
        System.out.println("Please choose your role");
        System.out.println("-------------------------");
        System.out.println("1. Admin");
        System.out.println("2. Artist");
        System.out.println("3. Podcast Host:");
        System.out.println("4. Record Label");
        System.out.println("-1. Quit!");
        System.out.println("Enter your choice: ");
    }
    public void displayMainMenu(){
        System.out.println("-------------------------");
        System.out.println("Welcome to WolfMedia!");
        System.out.println("-------------------------");
        System.out.println("1. Information Processing");
        System.out.println("2. Manage metadata and records");
        System.out.println("3. Manage payments:");
        System.out.println("4. Reports");
        System.out.println("-1. Quit!");
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
        System.out.println("5. Host");
        System.out.println("6. User");
        System.out.println("7. Record Label");
        System.out.println("8. Service");
        System.out.println("9. Artist");
        System.out.println("10. Album");
        System.out.println("11. Assign Song to Artist");
        System.out.println("11. Sponsor");
        System.out.println("12. Episode");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.print("Enter your choice: ");
    }
    public void displayCrudMenu(){
        System.out.println("-------------------------");
        System.out.println("Choose Operation");
        System.out.println("-------------------------");
        System.out.println("1. Create");
        System.out.println("2. Get");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.print("Enter your choice: ");
    }
    public void displayPaymentsMenu(){
        System.out.println("-------------------------");
        System.out.println("Maintain/Manage Payments");
        System.out.println("-------------------------");
        System.out.println("1. Make Royalty Payment for a song for the current month");
        System.out.println("2. Get Record Label's Payment History");
        System.out.println("3. Make Payment to Artist for a song for the current month");
        System.out.println("4. Get Artist's Payment History");
        System.out.println("5. Make Payment to Podcast Host for current month");
        System.out.println("6. Get Payments History for Podcast Host");
        System.out.println("7. Record Payment received from user for current month");
        System.out.println("8. Get Balance for Service");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.print("Enter your choice: ");
    }
}
