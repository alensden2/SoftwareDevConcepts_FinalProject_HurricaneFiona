package database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class CreateTables {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "dbConfig.prop";

    /**
     * Creates the required tables*/
    public CreateTables(){
        /**
         * Reference from lab work
         */
        try {
            InputStream stream = new FileInputStream(propertyFilename);
            identity.load(stream);
            username = identity.getProperty("username");
            password = identity.getProperty("password");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Creates the postal code tables
     */
    public boolean CreatePostalCodeTable(){
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /*
             * Stored procedure for getting the customer list
             */
            String stat = "CREATE TABLE IF NOT EXISTS PostalCode(PostalCodeId int NOT NULL AUTO_INCREMENT, postalCode VARCHAR(6), population int NOT NULL, area int NOT NULL, PRIMARY KEY (PostalCodeId)";
            resultSet = statement.executeQuery(stat);

            resultSet.close();
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }
}
