package ReportingMethods;

import DTOs.PeopleOutOfServiceDTO;
import DTOs.TotalPopulationServedByHubDTO;
import SupportClass.HubImpact;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class FixOrder {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public FixOrder() {
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
     * Gets the total population served by one hub
     * 
     * @return
     */
    public Map<String, TotalPopulationServedByHubDTO> getTotalPopulationServedByHub() {
        Connection connect = null;
        Statement statement = null;

        Map<String, TotalPopulationServedByHubDTO> totalPopulationServedByHubList = new HashMap<>();

        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * get the total population served by hubs, postalcode, hub identifier
             */
            String stat = "SELECT Distinct sum(PostalCode.population) as totalPopulationServedByHub, PostalCode.postalCode, \n"
                    +
                    "HubServiceAreas.hubIdentifier FROM alen.PostalCode join HubServiceAreas \n" +
                    "on HubServiceAreas.postalCode = PostalCode.postalCode \n" +
                    "group by HubServiceAreas.hubIdentifier;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                TotalPopulationServedByHubDTO totalPopulationServedByHubDTO = new TotalPopulationServedByHubDTO(
                        resultSet.getString("totalPopulationServedByHub"),
                        resultSet.getString("postalCode"),
                        resultSet.getString("hubIdentifier"));
                totalPopulationServedByHubList.put(resultSet.getString("hubIdentifier"), totalPopulationServedByHubDTO);
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return totalPopulationServedByHubList;

    }

    /**
     * Getting the repair time for all hubs
     * 
     * @return
     */
    public Map<String, Float> getRepairTimeForAllHubs() {
        Map<String, Float> repairTimeForAllHubs = new HashMap<>();
        Connection connect = null;
        Statement statement = null;

        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * get the hubId and the time required to repair it
             */
            String stat = "select hubIdentifier,repairEstimate from HubDamage;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                repairTimeForAllHubs.put(resultSet.getString("hubIdentifier"),
                        Float.valueOf(resultSet.getString("repairEstimate")));

            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return repairTimeForAllHubs;
    }

    /**
     * gets the fix order for all the hubs
     * 
     * @param repairTimeForAllHubs
     * @param totalPopulationServedByHubList
     * @param hubPerPostalCode
     * @param effectivePopulationServedInOneHub
     * @param hubServingPostals
     * @return
     */
    public List<HubImpact> getTheFixOrderHubs(Map<String, Float> repairTimeForAllHubs,
            Map<String, TotalPopulationServedByHubDTO> totalPopulationServedByHubList,
            Map<String, Integer> hubPerPostalCode, Map<String, Integer> effectivePopulationServedInOneHub,
            Map<String, Set<String>> hubServingPostals) {
        List<HubImpact> impactedHubs = new ArrayList<>();

        for (Map.Entry<String, Float> entry : repairTimeForAllHubs.entrySet()) {
            if ((repairTimeForAllHubs.containsKey(entry.getKey()))
                    && (repairTimeForAllHubs.get(entry.getKey()) > 0)) {
                // this case when the hub is actually damaged since only then it will be present
                // in the list
                // also greater than 0 since then it is still out of service
                float hubFloatImpactValue = 0;

                // getting the total population served by hub
                // float totalPopServedByHub =
                // Float.parseFloat(totalPopulationServedByHubList.get(entry.getKey()).totalPopulationServedByHub);

                // getting the no of hubs serving
                // float postalsServedByOneHub = (float) hubPerPostalCode.get(entry.getKey());
                int populationForOneHubIncludingMultipleHubScenerio = getPopulationPerHub(
                        effectivePopulationServedInOneHub, hubServingPostals, entry.getKey());

                // getting the repair time
                float hubRepairTime = repairTimeForAllHubs.get(entry.getKey());

                hubFloatImpactValue = (populationForOneHubIncludingMultipleHubScenerio) * (1 / hubRepairTime);

                // creating the hub impact object
                HubImpact impactedHub = new HubImpact();
                impactedHub.setHubIdentifier(entry.getKey());
                impactedHub.setImpactValue(hubFloatImpactValue);

                // adding to the impactedhubList
                impactedHubs.add(impactedHub);

            }
        }
        return impactedHubs;
    }

    /**
     * no of postal codes used
     * 
     * @return
     */
    public Map<String, Integer> getNoOfPostalCodeServedByHub() {
        Map<String, Integer> hubPerPostalCode = new HashMap<>();

        Connection connect = null;
        Statement statement = null;

        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * get the hubId and the no of postal codes it serves
             */
            String stat = "select count(PostalCode.postalCode) as noOfPostalUnderHub, HubServiceAreas.hubIdentifier from PostalCode \n"
                    +
                    "join HubServiceAreas on PostalCode.postalCode = HubServiceAreas.postalCode group by HubServiceAreas.hubIdentifier;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                hubPerPostalCode.put(resultSet.getString("hubIdentifier"),
                        Integer.valueOf(resultSet.getString("noOfPostalUnderHub")));

            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }

        return hubPerPostalCode;
    }

    /**
     * Gets the total postal codes served by one hub in a map
     * 
     * @param postalCodeDetails
     * @return
     */
    public Map<String, Set<String>> hubServingPostalCodeMap(List<PeopleOutOfServiceDTO> postalCodeDetails) {
        Map<String, Set<String>> hubServingPostals = new HashMap<>();

        for (int i = 0; i < postalCodeDetails.size(); i++) {
            if (hubServingPostals.containsKey(postalCodeDetails.get(i).hubIdentifier)) {
                Set<String> newSet = new HashSet<>();
                String newPostalCode = postalCodeDetails.get(i).postalCode;
                newSet = hubServingPostals.get(postalCodeDetails.get(i).hubIdentifier);
                newSet.add(newPostalCode);
                hubServingPostals.put(postalCodeDetails.get(i).hubIdentifier, newSet);

            } else {
                Set<String> value = new HashSet<>();
                value.add(postalCodeDetails.get(i).postalCode);
                hubServingPostals.put(postalCodeDetails.get(i).hubIdentifier, value);
            }
        }

        return hubServingPostals;
    }

    /**
     * Summation of all the population in one hub
     * The population per postal has already taken care of multiple hub scenerio in
     * the int peopleOutofService helper method
     * 
     * @param effectivePopulationServedInOneHub
     * @param hubServingPostals
     * @param hubId
     * @return
     */
    public int getPopulationPerHub(Map<String, Integer> effectivePopulationServedInOneHub,
            Map<String, Set<String>> hubServingPostals, String hubId) {
        // effectivePopulationServedInOneHub;
        // hubServingPostals;

        // https://stackoverflow.com/questions/16246821/how-to-get-values-and-keys-from-hashmap

        Set<String> requiredPopulationOfPostals = new HashSet<>();
        requiredPopulationOfPostals = hubServingPostals.get(hubId);
        List<String> requiredPopulationOfPostalsList = new ArrayList<>();
        requiredPopulationOfPostalsList.addAll(requiredPopulationOfPostals);
        int requiredPopulation = 0;

        for (int i = 0; i < requiredPopulationOfPostalsList.size(); i++) {
            int populationPerHub = effectivePopulationServedInOneHub.get(requiredPopulationOfPostalsList.get(i));
            requiredPopulation += populationPerHub;
        }

        return requiredPopulation;
    }
}
