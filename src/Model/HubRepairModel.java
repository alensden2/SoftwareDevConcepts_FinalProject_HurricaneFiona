package Model;

import database.CreateTables;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class HubRepairModel {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public HubRepairModel() {
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
     * Creating the 2 tables for
     * storing the hub repair details and the service status
     */
    public void createHubRepairTables() {
        CreateTables createTables = new CreateTables();
        // creating the two required tables for storing the data
        createTables.createHubRepairTable();
    }

    /**
     * Add the details to the repair table
     *
     * @param hubIdentifier
     * @param employeeID
     * @param repairTime
     * @return
     */
    public boolean addHubRepairToDataBase(String hubIdentifier, String employeeID, float repairTime,
            boolean inService) {
        Connection connect = null;
        Statement statement = null;
        String inServiceDB = null;
        int resultSet = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * Insert the repair details to the table
             */
            String stat = "INSERT INTO HubRepair (hubIdentifier, employeeID, repairTime, inService) VALUES (\""
                    + hubIdentifier + "\",\"" + employeeID + "\"," + repairTime + ", " + inService + ")";
            /*
             * We check if the boolean is true then we have to reset the time in the hub
             * damage to zero
             * And also hub service status to true
             * the time req to repair and the time repaired is set to zero
             *
             * IN CASE THE BOOLEAN IS FALSE (That is a partial repair)
             * The time worked on the hub is recorded and then this is used to subtract the
             * required repair time in the hub damage
             */

            /*
             * Case 1 - When inService is true
             * This is handled in power service as model can only have interactions
             */

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
