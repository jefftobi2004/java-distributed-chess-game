import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class establishes the connection to the database for the Chess Game.
 * The database is hosted on the site freesqldatabase.com and managed using MySQL Workbench
 */

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7786790";
    private static final String DB_user = "sql7786790";
    private static final String DB_password = "u9iSxgxyQx";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_user, DB_password);
        }

        return connection;
    }

    public void closeConnection() {
        try {
            if(connection != null || !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
