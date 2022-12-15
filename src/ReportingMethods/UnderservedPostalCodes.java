package ReportingMethods;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Map.Entry;
import java.util.*;

public class UnderservedPostalCodes {
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public UnderservedPostalCodes() {
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

    public Map<String, Integer> getHubsPerPostalCode() {
        Connection connect = null;
        Statement statement = null;
        Map<String, Integer> hubsPerPostal = new HashMap<>();
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
            String stat = "SELECT Distinct count(HubServiceAreas.hubIdentifier) as hubs, PostalCode.postalCode FROM alen.PostalCode join HubServiceAreas \n"
                    +
                    "on HubServiceAreas.postalCode = PostalCode.postalCode group by PostalCode.postalCode;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                hubsPerPostal.put(resultSet.getString("postalCode"), Integer.valueOf(resultSet.getString("hubs")));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return hubsPerPostal;

    }

    public Map<String, Integer> getPopulationPerPostalCode() {
        Connection connect = null;
        Statement statement = null;
        Map<String, Integer> populationPerPostal = new HashMap<>();
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
            String stat = "select postalCode, population from PostalCode;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                populationPerPostal.put(resultSet.getString("postalCode"),
                        Integer.valueOf(resultSet.getString("population")));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return populationPerPostal;

    }

    public Map<String, Integer> getAreaPerPostalCode() {
        Connection connect = null;
        Statement statement = null;
        Map<String, Integer> areaPerPostal = new HashMap<>();
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
            String stat = "select postalCode, area from PostalCode;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                areaPerPostal.put(resultSet.getString("postalCode"), Integer.valueOf(resultSet.getString("area")));
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return areaPerPostal;

    }

    public List<String> underservedPostalPopulation(Map<String, Integer> hubsPerPostal,
            Map<String, Integer> populationPerPostal) {
        List<String> underservedPostalsByPopulation = new ArrayList<>();
        Map<String, Float> underservedPostalsByPopulationMap = new LinkedHashMap<>();
        float underservedByPopulation = 0;

        for (Map.Entry<String, Integer> entry : populationPerPostal.entrySet()) {
            int populationInOnePostalCode = populationPerPostal.get(entry.getKey());
            int hubInOnePostalCode = hubsPerPostal.get(entry.getKey());

            underservedByPopulation = (float) hubInOnePostalCode / (float) populationInOnePostalCode;

            underservedPostalsByPopulationMap.put(entry.getKey(), underservedByPopulation);
        }


        // Sorting HashMap reference -
        // https://www.benchresources.net/java-how-to-sort-linkedhashmap-by-its-values/
        // dec 15 02:19 am

        Set<Map.Entry<String, Float>> underservedPostalsByPopulationMapSet = underservedPostalsByPopulationMap
                .entrySet();

        List<Map.Entry<String, Float>> underservedPostalsByPopulationMapSetListEntry = new ArrayList<Map.Entry<String, Float>>(
                underservedPostalsByPopulationMapSet);

        Collections.sort(underservedPostalsByPopulationMapSetListEntry,
                new Comparator<Map.Entry<String, Float>>() {

                    @Override
                    public int compare(Entry<String, Float> es1,
                            Entry<String, Float> es2) {
                        return es1.getValue().compareTo(es2.getValue());
                    }
                });
        underservedPostalsByPopulationMap.clear();
        for(Map.Entry<String, Float> map : underservedPostalsByPopulationMapSetListEntry){
            underservedPostalsByPopulationMap.put(map.getKey(), map.getValue());
        }

        System.out.println("here");
        for(Map.Entry<String, Float> lhmap :
                underservedPostalsByPopulationMap.entrySet()){
            underservedPostalsByPopulation.add(lhmap.getKey());
        }
        return underservedPostalsByPopulation;
    }

}
