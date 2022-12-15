package ReportingMethods;

import SupportClass.HubImpact;
import SupportClass.Point;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class RepairPlan {
    // to find the distance between 2 points in this plain we are using manhattan
    // distance
    // |x2-x1| + |y2-y1|
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";

    public RepairPlan() {
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

    // gets the hubid and the coordinates of the hub that have repairs pending
    public Map<String, Point> getHubCoordinates() {
        Connection connect = null;
        Statement statement = null;
        Map<String, Point> hubsCoordinates = new HashMap<>();
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
            String stat = "select HubDamage.hubIdentifier,x,y from HubLocation join HubDamage on \n" +
                    "HubLocation.hubIdentifier = HubDamage.hubIdentifier where repairEstimate>0;";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {
                Point hubCoordinates = new Point();
                hubCoordinates.setX(Integer.parseInt(resultSet.getString("x")));
                hubCoordinates.setY(Integer.parseInt(resultSet.getString("y")));
                hubsCoordinates.put(resultSet.getString("hubIdentifier"), hubCoordinates);
            }
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return hubsCoordinates;

    }

    public String getEndHub(String startHub, int maxDistance, List<HubImpact> hubImpact,
            Map<String, Point> hubsCoordinates) {

        // setting the start hub coordinates
        Point startHubPoint = hubsCoordinates.get(startHub);
        int distanceBetweenStartAndEnd = 0;

        Map<String, Float> hubWithInMaxDistanceWithImpactValue = new HashMap<>();
        Map<String, Float> hubImpactMap = new HashMap<>();

        // changing hubImpact from list to map
        for (int i = 0; i < hubImpact.size(); i++) {
            hubImpactMap.put(hubImpact.get(i).hubIdentifier, hubImpact.get(i).impactValue);
        }

        // looping and adding the
        for (Map.Entry<String, Point> entry : hubsCoordinates.entrySet()) {
            distanceBetweenStartAndEnd = manhattanDistance(startHubPoint, hubsCoordinates.get(entry.getKey()));

            // if distance greater than zero we pass, if the distance is zero then same hub
            // is in the current iteration
            if (distanceBetweenStartAndEnd < maxDistance && distanceBetweenStartAndEnd != 0) {
                hubWithInMaxDistanceWithImpactValue.put(entry.getKey(), hubImpactMap.get(entry.getKey()));
            }
        }
        // Ref -
        // https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
        // Dec 15, 1737Hrs
        String endHub = Collections.max(hubWithInMaxDistanceWithImpactValue.entrySet(), Map.Entry.comparingByValue())
                .getKey();

        return endHub;
    }

    int manhattanDistance(Point hub1, Point hub2) {
        int x1 = hub1.getX();
        int y1 = hub1.getY();
        // for hub2
        int x2 = hub2.getX();
        int y2 = hub2.getY();
        // computing distance
        int manhattanDistance = 0;
        float part1 = Math.abs(x2 - x1);
        float part2 = Math.abs(y2 - y1);

        manhattanDistance = Math.round(part1) + Math.round(part2);
        return manhattanDistance;
    }
}
