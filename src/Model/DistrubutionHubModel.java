/**
 * This is the model class for the distribution hub
 */
package Model;

import SupportClass.Point;
import database.CreateTables;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DistrubutionHubModel {
    private String postalCode;
    private int population;
    private int area;
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    /**
     * Constructor
     */
    public DistrubutionHubModel() {
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
     * Creates the table for the hub identifier
     */
    public void createHubsTable() {
        CreateTables createTables = new CreateTables();
        createTables.createHubsTable();
        createTables.createHubLocationTable();
    }

    /**
     * This add the data to the new hub table
     * 
     * @param hubIdentifier
     * @param x
     * @param y
     * @param postalCode
     * @return
     */
    public boolean addHubtoDataBase(String hubIdentifier, String postalCode) {
        Connection connect = null;
        Statement statement = null;
        int resultSet = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * Insert the hubs
             */
            String stat = "INSERT INTO HubServiceAreas (hubIdentifier, postalCode) VALUES (\"" + hubIdentifier + "\",\"" + postalCode + "\")";
            resultSet = statement.executeUpdate(stat);
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * adding to newly created table
     */
    public boolean addHubLocationtoDataBase(String hubIdentifier, int x, int y) {
        Connection connect = null;
        Statement statement = null;
        int resultSet = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * Insert the hubs
             */
            String stat = "INSERT INTO HubLocation (hubIdentifier, x, y) VALUES (\"" + hubIdentifier + "\"," + x
                    + "," + y + ")";
            resultSet = statement.executeUpdate(stat);
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }
}
