import SupportClass.DamagedPostalCodes;
import SupportClass.Point;
import database.DropTables;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] arg) {
        DropTables dropTables = new DropTables();
        dropTables.dropAllTables();
        System.out.println("hello worked");
        /**
         * Sample run enviroment
         */
        PowerService powerService = new PowerService();
        powerService.addPostalCode("p1", 3, 5);
        powerService.addPostalCode("p2", 4, 5);
        powerService.addPostalCode("p3", 9, 2);
        powerService.addPostalCode("p4", 6, 8);
        powerService.addPostalCode("p5", 20, 10);
        powerService.addPostalCode("p6", 50, 34);

        Point p = new Point();
        p.setX(3);
        p.setY(5);

        Point p2 = new Point();
        p2.setX(5);
        p2.setY(5);
        Set<String> serviceAreas = new HashSet<>();
        serviceAreas.add("p3");
        serviceAreas.add("p4");
        serviceAreas.add("p5");

        powerService.addDistributionHub("Main1", p, serviceAreas);

        powerService.addDistributionHub("Main2", p2, Collections.singleton("p3"));
        powerService.addDistributionHub("Main2", p2, Collections.singleton("p2"));
        powerService.addDistributionHub("Main3", p2, Collections.singleton("p1"));
        powerService.addDistributionHub("Main3", p2, Collections.singleton("p3"));
        powerService.addDistributionHub("Main4", p2, Collections.singleton("p2"));
        powerService.addDistributionHub("Main4", p2, Collections.singleton("p6"));



        powerService.hubDamage("Main1", 4);
        powerService.hubDamage("Main3", 50);
        powerService.hubDamage("Main2", 10);
        //powerService.hubDamage("Main4", 20);


        // powerService.hubDamage("ASN32", 21.3F);
        List<DamagedPostalCodes> a = powerService.mostDamagedPostalCodes(2);
        List<String> aa = powerService.underservedPostalByPopulation(3);
        System.out.println(); // powerService.hubRepair("main1", "Alen344", 4, true);
       // System.out.println(powerService.peopleOutOfService()); ;
        powerService.fixOrder(5);
        powerService.rateOfServiceRestoration(0.10F);
        List<DamagedPostalCodes> mostDamagedPostalCodes= powerService.mostDamagedPostalCodes(4);
        //powerService.hubRepair("main3", "alen", 40, true);
        // drop tables;
        System.out.println("END");
    }
}
