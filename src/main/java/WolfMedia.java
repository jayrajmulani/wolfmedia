import utils.CLI;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.NoSuchElementException;

public class WolfMedia {
    private static CLI cli = new CLI();
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException, IllegalArgumentException {
        try{
            cli.run();
        }
        catch (IllegalArgumentException illegalArgumentException){
            System.out.println(illegalArgumentException.getMessage());
        }
        catch (SQLException sqlException){
            System.out.println("SQL Exception!");
            sqlException.printStackTrace();
        }
        catch (NoSuchElementException noElementEx){
            System.out.println("Some value(s) seem to be missing from the database.");
            System.out.println(noElementEx.getMessage());
        }
        catch (Exception ex){
            System.out.println("Something went wrong!");
            ex.printStackTrace();
        }

    }
}
