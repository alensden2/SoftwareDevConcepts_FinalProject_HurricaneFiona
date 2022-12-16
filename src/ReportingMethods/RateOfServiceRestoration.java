package ReportingMethods;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RateOfServiceRestoration {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public RateOfServiceRestoration() {
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
     * This gets the total population
     * 
     * @return
     */
    public int getTotalPopulation() {
        Connection connect = null;
        Statement statement = null;

        int totalPopulationInTheUniverse = 0;

        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * get the postal codes
             */
            String stat = "select sum(population) as sumPopulation from PostalCode;\n";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                totalPopulationInTheUniverse = Integer.parseInt(resultSet.getString("sumPopulation"));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return totalPopulationInTheUniverse;

    }

    /**
     * The total time to repair the hubs
     * 
     * @return
     */
    public float getTotalRepairTime() {
        Connection connect = null;
        Statement statement = null;

        float totalRepairTimeForHubs = 0;

        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * get the total time for repair
             */
            String stat = "select sum(repairEstimate) as sumPopulation from HubDamage;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                totalRepairTimeForHubs = Float.parseFloat(resultSet.getString("sumPopulation"));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return totalRepairTimeForHubs;

    }
}
