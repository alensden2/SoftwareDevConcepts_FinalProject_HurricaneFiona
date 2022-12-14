package ReportingMethods;

import DTOs.PeopleOutOfServiceDTO;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class PeopleOutOfService {

    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    /**
     * reference from lab
     */
    public PeopleOutOfService() {
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
     * Fetch postal codes and the population
     * 
     * @return
     */
    public List<PeopleOutOfServiceDTO> fetchPostalCodesDetails() {
        Connection connect = null;
        Statement statement = null;

        List<PeopleOutOfServiceDTO> peopleOutOfServiceListDTO = new ArrayList<>();

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
            String stat = "SELECT Distinct PostalCode.population, PostalCode.postalCode, HubServiceAreas.hubIdentifier FROM alen.PostalCode join HubServiceAreas on HubServiceAreas.postalCode = PostalCode.postalCode;\n";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                PeopleOutOfServiceDTO peopleOutOfServiceDTO = new PeopleOutOfServiceDTO(
                        resultSet.getString("postalCode"), resultSet.getString("population"),
                        resultSet.getString("hubIdentifier"));
                peopleOutOfServiceListDTO.add(peopleOutOfServiceDTO);
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return peopleOutOfServiceListDTO;

    }

    /**
     * Fetches the time required to repair per hub
     * 
     * @return
     */
    public Map<String, Float> fetchPostalCodesRepairTime() {
        Connection connect = null;
        Statement statement = null;

        Map<String, Float> repairTimeForPostalCode = new HashMap<>();

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
            String stat = "SELECT Distinct PostalCode.postalCode, repairEstimate FROM alen.PostalCode \n" +
                    "join HubServiceAreas on \n" +
                    "HubServiceAreas.postalCode = PostalCode.postalCode\n" +
                    "join HubDamage on HubDamage.hubIdentifier = HubServiceAreas.hubIdentifier;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {

                repairTimeForPostalCode.put(resultSet.getString("postalCode"),
                        Float.valueOf(resultSet.getString("repairEstimate")));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return repairTimeForPostalCode;

    }

    /**
     * gets the populalation per hub
     * 
     * @param fetchPostalCodesDetail
     * @return
     */
    public Map<String, Integer> getHubsPopulationPerPopulation(List<PeopleOutOfServiceDTO> fetchPostalCodesDetail) {
        Map<String, Integer> HubsPerPostalCode = new HashMap<>();
        int newValue = 0;
        for (int i = 0; i < fetchPostalCodesDetail.size(); i++) {
            if (HubsPerPostalCode.containsKey(fetchPostalCodesDetail.get(i).postalCode) == true) {
                // already present pincode that it is served by atleast one hub already
                // we add one to currently present hub
                newValue = HubsPerPostalCode.get(fetchPostalCodesDetail.get(i).postalCode);
                newValue += 1;
                HubsPerPostalCode.put(fetchPostalCodesDetail.get(i).postalCode, newValue);
            } else {
                HubsPerPostalCode.put(fetchPostalCodesDetail.get(i).postalCode, 1);
            }
        }

        return HubsPerPostalCode;
    }

    /**
     * We have to check which hub is active and which one has been repaired for this
     * we can see the repair time required in the hub damage
     * We dont check for the inService as the population method may be called
     * sometimes before the hub repair and in this case the hub repair table doest
     * no exist
     * and we get an error
     */

    /**
     * Returns the population not served
     * 
     * @param fetchPostalPopulationCodesDetail
     * @param HubsPerPostalCode
     * @param repairTimePerPostalCode
     * @return
     */
    public int getPopulationAffected(List<PeopleOutOfServiceDTO> fetchPostalPopulationCodesDetail,
            Map<String, Integer> HubsPerPostalCode, Map<String, Float> repairTimePerPostalCode) {
        Map<String, Integer> populationPerPostalCode = new HashMap<>();

        float populationAffected = 0;
        for (int i = 0; i < fetchPostalPopulationCodesDetail.size(); i++) {
            // population per hub
            populationPerPostalCode.put(fetchPostalPopulationCodesDetail.get(i).postalCode,
                    Integer.valueOf(fetchPostalPopulationCodesDetail.get(i).population));
        }

        for (Map.Entry<String, Integer> entry : HubsPerPostalCode.entrySet()) {
            // hubs per postal code @HubsPerPostalCode
            // time taken to repair per postal code @repairTimePerPostlCode

            // iteration reference -
            // https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap

            // 1st condition

            if ((repairTimePerPostalCode.containsKey(entry.getKey()))
                    && (repairTimePerPostalCode.get(entry.getKey()) > 0)) {
                // this case when the hub is actually damaged since only then it will be present
                // in the list
                // also greater than 0 since then it is still out of service
                float impactedPopulationInCaseMultipleHub = 0;
                impactedPopulationInCaseMultipleHub = (float) (populationPerPostalCode.get(entry.getKey()))
                        / (float) (HubsPerPostalCode.get(entry.getKey()));
                populationAffected += impactedPopulationInCaseMultipleHub;
            }
        }
        // conversion to int as contraint by method
        return (int) populationAffected;
    }
}
