import Controller.DistrubutionHubController;
import Controller.HubDamageController;
import Controller.HubRepairController;
import Controller.PostalCodeController;
import Model.DistrubutionHubModel;
import Model.HubDamageModel;
import Model.HubRepairModel;
import Model.PostalCodeModel;
import ReportingMethods.MostDamagedPostalCode;
import ReportingMethods.PopulationUnderserved;
import SupportClass.DamagedPostalCodes;
import SupportClass.Point;
import View.DistrubutionHubView;
import View.HubDamageView;
import View.HubRepairView;
import View.PostalCodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PowerService {
    Point utmCoordinates = new Point();

    /**
     * Adds the postal code
     * 
     * @param postalCode
     * @param population
     * @param area
     * @return
     */
    boolean addPostalCode(String postalCode, int population, int area) {
        /**
         * Setting instance of view and model
         * They are accessed through the controller
         */
        PostalCodeModel postalCodeModel = new PostalCodeModel();
        PostalCodeView postalCodeView = new PostalCodeView();
        PostalCodeController postalCodeController = new PostalCodeController(postalCodeModel, postalCodeView);

        /**
         * Inserting values
         */
        postalCodeController.insertPostalCode(postalCode, population, area);
        return true;
    }

    /**
     * Adds the distribution hubs
     * 
     * @param hubIdentifier
     * @param location
     * @param servicedAreas
     * @return
     */
    boolean addDistributionHub(String hubIdentifier, Point location, Set<String> servicedAreas) {
        /**
         * Setting instance of view and model
         * They are accessed through the controller
         */
        DistrubutionHubView distrubutionHubView = new DistrubutionHubView();
        DistrubutionHubModel distrubutionHubModel = new DistrubutionHubModel();
        DistrubutionHubController distrubutionHubController = new DistrubutionHubController(distrubutionHubModel,
                distrubutionHubView);

        /**
         * Inserting the values
         * Values from the set of serviced areas is broken into multiple elements
         * This is to follow the first normalization rule no multivalued column
         * The table still has redundant data this can be avoided by creating a separate
         * table to store the repeating elements
         */
        // List to store the set value
        List<String> servicedAreasList = new ArrayList<>();
        servicedAreasList.addAll(servicedAreas);
        for (int i = 0; i < servicedAreas.size(); i++) {
            distrubutionHubController.insertHubServiceArea(hubIdentifier, servicedAreasList.get(i));
        }

        // storing the pincode
        distrubutionHubController.insertHubLocation(hubIdentifier, location.getX(), location.getY());
        return true;
    }

    /**
     * For Hub Damage
     * 
     * @param hubIdentifier
     * @param repairEstimate
     */
    void hubDamage(String hubIdentifier, float repairEstimate) {
        HubDamageModel hubDamageModel = new HubDamageModel();
        HubDamageView hubDamageView = new HubDamageView();
        HubDamageController hubDamageController = new HubDamageController(hubDamageView, hubDamageModel);

        hubDamageController.insertHubDamage(hubIdentifier, repairEstimate);
        // hubDamageController.viewHubDamage(hubIdentifier,repairEstimate);
    }

    /**
     * Updates the repair task done
     * Has tackled the partial repair scenerio
     * 
     * @param hubIdentifier
     * @param employeeId
     * @param repairTime
     * @param inService
     */
    void hubRepair(String hubIdentifier, String employeeId, float repairTime, boolean inService) {
        if (inService == true) {
            // hub is in service that is repaired
            // so we can update the time required to repair in the hub damage to 0
            HubRepairModel hubRepairModel = new HubRepairModel();
            HubRepairView hubRepairView = new HubRepairView();
            HubRepairController hubRepairController = new HubRepairController(hubRepairView, hubRepairModel);

            hubRepairController.insertHubRepair(hubIdentifier, employeeId, repairTime, inService);
            // hubRepairController.viewInsertedHub(hubIdentifier,employeeId,repairTime,inService);

            // updating the hubdamage to zero
            HubDamageModel hubDamageModel = new HubDamageModel();
            HubDamageView hubDamageView = new HubDamageView();
            HubDamageController hubDamageController = new HubDamageController(hubDamageView, hubDamageModel);
            hubDamageController.updateHubDamage(hubIdentifier, 0);

        } else if (inService == false) {
            float repairTimeRequired;
            // storing the repair task done
            HubRepairModel hubRepairModel = new HubRepairModel();
            HubRepairView hubRepairView = new HubRepairView();
            HubRepairController hubRepairController = new HubRepairController(hubRepairView, hubRepairModel);

            hubRepairController.insertHubRepair(hubIdentifier, employeeId, repairTime, inService);

            // fetching the required repair time value
            HubDamageModel hubDamageModel = new HubDamageModel();
            HubDamageView hubDamageView = new HubDamageView();
            HubDamageController hubDamageController = new HubDamageController(hubDamageView, hubDamageModel);
            repairTimeRequired = Float.parseFloat(hubDamageController.fetchRepairEstimate(hubIdentifier));

            // updating the remaining work to be done
            // the new repair time would be the subtraction of the hours worked and the req
            // repair time

            hubDamageController.updateHubDamage(hubIdentifier, Math.abs(repairTime - repairTimeRequired));

        }
    }

    int peopleOutOfService() {

        return 0;
    }

    /**
     * returns the most damaged postal codes
     * 
     * @param limit
     * @return
     */
    List<DamagedPostalCodes> mostDamagedPostalCodes(int limit) {
        // assumption - in the case of multiple postal codes requiring the same amount
        // of time, the top 4 is returned
        MostDamagedPostalCode mostDamagedPostalCode = new MostDamagedPostalCode();
        return mostDamagedPostalCode.getMostDamagedPostalCodes(limit);
    }

    // List<HubImpact> fixOrder ( int limit )
    List<Integer> rateOfServiceRestoration(float increment) {

        return null;
    }

    // List<HubImpact> repairPlan ( String startHub, int maxDistance, float maxTime
    // ){
    //
    // return null;
    // }

    /**
     * Gets the postal code of the underserved population
     * 
     * @param limit
     * @return
     */
    List<String> underservedPostalByPopulation(int limit) {
        PopulationUnderserved populationUnderserved = new PopulationUnderserved();
        return populationUnderserved.getPostalCodeBasedOnPopulation(limit);
    }

    List<String> underservedPostalByArea(int limit) {

        return null;
    }
}
