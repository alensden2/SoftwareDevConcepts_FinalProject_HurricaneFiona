import Controller.DistrubutionHubController;
import Controller.HubDamageController;
import Controller.PostalCodeController;
import Model.DistrubutionHubModel;
import Model.HubDamageModel;
import Model.PostalCodeModel;
import SupportClass.Point;
import View.DistrubutionHubView;
import View.HubDamageView;
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
     * @param hubIdentifier
     * @param repairEstimate
     */
    void hubDamage ( String hubIdentifier, float repairEstimate ){
        HubDamageModel hubDamageModel = new HubDamageModel();
        HubDamageView hubDamageView =new HubDamageView();
        HubDamageController hubDamageController = new HubDamageController(hubDamageView,hubDamageModel);

        hubDamageController.insertHubDamage(hubIdentifier,repairEstimate);
        hubDamageController.viewHubDamage(hubIdentifier,repairEstimate);
    }
}
