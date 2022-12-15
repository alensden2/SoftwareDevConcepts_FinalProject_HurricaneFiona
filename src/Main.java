import SupportClass.DamagedPostalCodes;
import SupportClass.HubImpact;
import SupportClass.Point;
import database.DropTables;

import java.util.*;

public class Main {
    public static void main(String[] arg) {
        DropTables dropTables = new DropTables();
        dropTables.dropAllTables();
//        System.out.println("hello worked");
        /**
         * Sample run enviroment
         */
//        PowerService powerService = new PowerService();
//        powerService.addPostalCode("p1", 3, 5);
//        powerService.addPostalCode("p2", 4, 5);
//        powerService.addPostalCode("p3", 9, 2);
//        powerService.addPostalCode("p4", 6, 8);
//        powerService.addPostalCode("p5", 20, 10);
//        powerService.addPostalCode("p6", 50, 34);
//
//        Point p = new Point();
//        p.setX(3);
//        p.setY(5);
//
//        Point p2 = new Point();
//        p2.setX(5);
//        p2.setY(5);
//        Set<String> serviceAreas = new HashSet<>();
//        serviceAreas.add("p3");
//        serviceAreas.add("p4");
//        serviceAreas.add("p5");
//
//        powerService.addDistributionHub("Main1", p, serviceAreas);
//
//        powerService.addDistributionHub("Main2", p2, Collections.singleton("p3"));
//        powerService.addDistributionHub("Main2", p2, Collections.singleton("p2"));
//        powerService.addDistributionHub("Main3", p2, Collections.singleton("p1"));
//        powerService.addDistributionHub("Main3", p2, Collections.singleton("p3"));
//        powerService.addDistributionHub("Main4", p2, Collections.singleton("p2"));
//        powerService.addDistributionHub("Main4", p2, Collections.singleton("p6"));
//
//
//
//        powerService.hubDamage("Main1", 4);
//        powerService.hubDamage("Main3", 50);
//        powerService.hubDamage("Main2", 10);
//        //powerService.hubDamage("Main4", 20);
//
//
//        // powerService.hubDamage("ASN32", 21.3F);
//        List<DamagedPostalCodes> a = powerService.mostDamagedPostalCodes(2);
//        List<String> aa = powerService.underservedPostalByPopulation(3);
//        System.out.println(); // powerService.hubRepair("main1", "Alen344", 4, true);
//       // System.out.println(powerService.peopleOutOfService()); ;
//        powerService.fixOrder(5);
//        powerService.rateOfServiceRestoration(0.10F);
//        List<DamagedPostalCodes> mostDamagedPostalCodes= powerService.mostDamagedPostalCodes(4);
//        //powerService.hubRepair("main3", "alen", 40, true);
//        // drop tables;
//        System.out.println("END");.

        PowerService ps = new PowerService();
        ps.addPostalCode("B3J2K9", 100, 30);
        ps.addPostalCode("A5F3J9", 200, 30);
        ps.addPostalCode("CK3J20", 150, 30);
        ps.addPostalCode("D3K2I2", 300, 30);
        ps.addPostalCode("E3J2M9", 800, 30);
        ps.addPostalCode("F3H2D2", 1000, 30);

        Point p1 = new Point();
        p1.setX(5);
        p1.setY(5);
        Set<String> set1 = new HashSet<String>() {{
            add("A5F3J9");
            add("B3J2K9");
            add("CK3J20");
            add("D3K2I2");
            add("E3J2M9");
            add("F3H2D2");
        }};
        ;
        Point p2 = new Point();
        p2.setY(5);
        p2.setX(2);
        Set<String> set2 = new HashSet<String>() {{
            add("A5F3J9");
            add("B3J2K9");
            add("CK3J20");
        }};
        ;
        Point p3 = new Point();
        p2.setX(5);
        p2.setY(5);
        Set<String> set3 = new HashSet<String>() {{
            add("A5F3J9");
        }};
        ;

        ps.addDistributionHub("HUB001", p1, set1);
        ps.addDistributionHub("HUB002", p2, set2);
        ps.addDistributionHub("HUB003", p3, set3);

        ps.hubDamage("HUB001", 10);
        ps.hubDamage("HUB002", 20);
        ps.hubDamage("HUB003", 30);


        ps.hubRepair("HUB001", "EMP001", 5, false);
        ps.hubRepair("HUB002", "EMP001", 5, false);
        ps.hubRepair("HUB003", "EMP001", 5, false);


        int n = ps.peopleOutOfService();
// will only return top 2 assumption even if the lower no is same
        List<DamagedPostalCodes> l5 = new ArrayList<>();
        l5 = ps.mostDamagedPostalCodes(2);

        List<HubImpact> l0 = new ArrayList<>();
        l0 = ps.fixOrder(1);

        // correct
        List<Integer> l1 = new ArrayList<>();
        l1=ps.rateOfServiceRestoration((float) 0.05);

        List<String> l2 = new ArrayList<>();
        l2 = ps.underservedPostalByPopulation(6);

        List<String> l3 = new ArrayList<>();
        l3 = ps.underservedPostalByArea(6);

    }
}
