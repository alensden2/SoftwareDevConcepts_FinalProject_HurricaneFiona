package database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class can be used to drop tables after testing
 */
public class DropTables {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public DropTables() {
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

    public boolean dropAllTables() {
        Connection connect = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /*
             * Stored procedure for getting the customer list
             */
            String stat1 = "Drop Table if exists HubDamage";
            String stat2 = "Drop Table if exists HubLocation";
            String stat3 = "Drop Table if exists HubServiceAreas";
            String stat4 = "Drop Table if exists PostalCode";
            String stat5 = "Drop Table if exists HubRepair";

            statement.executeUpdate(stat1);
            statement.executeUpdate(stat2);
            statement.executeUpdate(stat3);
            statement.executeUpdate(stat4);
            statement.executeUpdate(stat5);

            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }
}
