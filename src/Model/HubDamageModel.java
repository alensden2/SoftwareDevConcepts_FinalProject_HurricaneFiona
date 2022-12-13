package Model;

import DTOs.PostalCodeDTO;
import database.CreateTables;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class HubDamageModel {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public HubDamageModel() {
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
     * Creates table
     */
    public void createHubDamageTable() {
        CreateTables createTables = new CreateTables();
        createTables.createHubDamage();
    }

    /**
     * add hub damage
     * 
     * @param hubIdentifier
     * @param repairTimeEstimate
     * @return
     */
    public boolean addHubDamageToDataBase(String hubIdentifier, float repairTimeEstimate) {
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
             * Insert the postal Code
             */
            String stat = "INSERT INTO HubDamage (hubIdentifier, repairEstimate) VALUES (\"" + hubIdentifier + "\","
                    + repairTimeEstimate + ")";
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
     * add hub damage
     *
     * @param hubIdentifier
     * @param repairTimeEstimate
     * @return
     */
    public boolean updateHubDamageToDataBase(String hubIdentifier, float repairTimeEstimate) {
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
             * Insert the postal Code
             */
            String stat = "UPDATE HubDamage SET hubIdentifier = \"" + hubIdentifier + "\", repairEstimate = "
                    + repairTimeEstimate + " where hubIdentifier = \"" + hubIdentifier + "\"";
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
     * get the repair float time
     * 
     * @param hubIdentifier
     * @return
     */
    public String fetchRepairTimeFromDataBase(String hubIdentifier) {
        Connection connect = null;
        Statement statement = null;
        String repairTime = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * Insert the postal Code
             */
            String stat = "SELECT repairEstimate FROM HubDamage where hubIdentifier=\"" + hubIdentifier + "\";";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                repairTime = resultSet.getString("repairEstimate");
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return repairTime;
    }
}
