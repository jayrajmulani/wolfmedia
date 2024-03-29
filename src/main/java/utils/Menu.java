package utils;

public class Menu {
    public void displayRoleMenu(){
        System.out.println("-------------------------");
        System.out.println("Please choose your role");
        System.out.println("-------------------------");
        System.out.println("1. Admin");
        System.out.println("2. Artist");
        System.out.println("3. Podcast Host");
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
        System.out.println("3. Manage payments");
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
        System.out.println("8. Album");
        System.out.println("9. Sponsor");
        System.out.println("10. Episode");
        System.out.println("11. Assign Song to Artist");
        System.out.println("12. Assign Artist to Album");
        System.out.println("13. Assign Artist to Record Label");
        System.out.println("14. Assign Song to Album");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.println("Enter your choice: ");
    }

    public void displayMetaDataAndRecordsMenu(){
        System.out.println("-------------------------");
        System.out.println("Maintaining Metadata and Records");
        System.out.println("-------------------------");
        System.out.println("1. Increase Song Play Count for current month");
        System.out.println("2. Decrease Song Play Count for current month");
        System.out.println("3. Find total Play Count for a Song");
        System.out.println("4. Increase Podcast Play Count for current month");
        System.out.println("5. Decrease Podcast Play Count for current month");
        System.out.println("6. Find total Play Count for a Podcast Episode");
        System.out.println("7. Enter Rating for Podcast");
        System.out.println("8. Average Rating for a Podcast");
        System.out.println("9. Increase Podcast Subscriptions");
        System.out.println("10. Decrease Podcast Subscriptions");
        System.out.println("11. Total Subscribers for a Podcast");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.println("Enter your choice: ");
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
        System.out.println("Enter your choice: ");
    }
    public void displayPaymentsMenu(){
        System.out.println("-------------------------");
        System.out.println("Maintain/Manage Payments");
        System.out.println("-------------------------");
        System.out.println("1. Make Royalty Payment for a song for the current month");
        System.out.println("2. Get Record Label's Payment History");
        System.out.println("3. Make Payment to Artists for a song for the current month");
        System.out.println("4. Get Artist's Payment History");
        System.out.println("5. Make Payment to Podcast Host for current month");
        System.out.println("6. Get Payments History for Podcast Host");
        System.out.println("7. Record Payment received from user for current month");
        System.out.println("8. Get Balance for Service");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.println("Enter your choice: ");
    }
    public void displayReportsMenu(){
        System.out.println("-------------------------");
        System.out.println("Generate Reports");
        System.out.println("-------------------------");
        System.out.println("1. Historical Play Count for Song");
        System.out.println("2. Play Count for Song for Current Month");
        System.out.println("3. Historical Play Count for Album");
        System.out.println("4. Play Count for Album for Current Month");
        System.out.println("5. Historical Play Count for Artist");
        System.out.println("6. Play Count for Artist for Current Month");
        System.out.println("7. Total Payments Made to Host");
        System.out.println("8. Total Payments Made to Artist");
        System.out.println("9. Total Payments Made to Record Label");
        System.out.println("10. Monthly Revenue for Service");
        System.out.println("11. Yearly Revenue for Service");
        System.out.println("12. Find Songs By Artist");
        System.out.println("13. Find Songs By Album");
        System.out.println("14. Find Episodes By Podcast");
        System.out.println("15. Flush Play Count for Song for current month");
        System.out.println("16. Historical Play Count for Episode");
        System.out.println("17. Play Count for Episode for Current Month");
        System.out.println("18. Flush Play Count for Episode for current month");
        System.out.println("19. Monthly Listeners for Artist");
        System.out.println("0. Go back!");
        System.out.println("-1. Quit!");
        System.out.println("Enter your choice: ");
    }
}
