package utils;

import java.sql.*;

public class DB {
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/manand" ;
    private static final String user = "manand";
    private static final String password = "dbmsproject";
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, user, password);
    }
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void rollBackTransaction(Connection connection){
        try {
            System.err.print("Transaction is being rolled back");
            connection.rollback();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

}
