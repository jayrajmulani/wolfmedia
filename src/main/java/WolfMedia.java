import utils.CLI;

import java.sql.SQLException;
import java.text.ParseException;

public class WolfMedia {
    private static CLI cli = new CLI();
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        cli.run();
    }
}
