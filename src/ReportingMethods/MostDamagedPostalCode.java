package ReportingMethods;

import SupportClass.DamagedPostalCodes;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MostDamagedPostalCode {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public MostDamagedPostalCode() {
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
     * Gets the most damaged postal codes
     * 
     * @param limit
     * @return
     */
    public List<DamagedPostalCodes> getMostDamagedPostalCodes(int limit) {
        Connection connect = null;
        Statement statement = null;
        List<DamagedPostalCodes> damagedPostalCodesList = new ArrayList<>();
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
            String stat = "select PostalCode.postalCode, sum(repairEstimate) as repairEstimate from PostalCode join \n" +
                    "HubServiceAreas on HubServiceAreas.postalCode = PostalCode.postalCode \n" +
                    "join HubDamage on HubDamage.hubIdentifier = HubServiceAreas.hubIdentifier \n" +
                    "group by PostalCode.postalCode order by repairEstimate Desc limit " + limit + ";";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                DamagedPostalCodes damagedPostalCodes = new DamagedPostalCodes();
                damagedPostalCodes.setPostalCode(resultSet.getString("postalCode"));
                damagedPostalCodes.setAmtofRepairsNeeded(Float.parseFloat(resultSet.getString("repairEstimate")));

                damagedPostalCodesList.add(damagedPostalCodes);
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return damagedPostalCodesList;

    }
}
