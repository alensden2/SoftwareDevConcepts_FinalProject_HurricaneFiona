import SupportClass.Point;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] arg) {
        System.out.println("hello worked");
        /**
         * Sample run enviroment
         */
        PowerService powerService = new PowerService();
        powerService.addPostalCode("A3J2K9", 5522, 624424);

        Point p = new Point();
        p.setX(3);
        p.setY(5);

        Point p2 = new Point();
        p2.setX(5);
        p2.setY(5);
        Set<String> serviceAreas = new HashSet<>();
        serviceAreas.add("b3h425");
        serviceAreas.add("b3h498");
        serviceAreas.add("b3h2k9");
        serviceAreas.add("b6h5r7");
        serviceAreas.add("b9h63j");

        powerService.addDistributionHub("s3847hLIUb39", p, serviceAreas);

        powerService.addDistributionHub("uyd7", p2, serviceAreas);

        powerService.hubDamage("diuhbhifeeib", 23.2F);
    }
}
