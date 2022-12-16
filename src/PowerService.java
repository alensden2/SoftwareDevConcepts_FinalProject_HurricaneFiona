import Controller.DistrubutionHubController;
import Controller.HubDamageController;
import Controller.HubRepairController;
import Controller.PostalCodeController;
import DTOs.PeopleOutOfServiceDTO;
import DTOs.TotalPopulationServedByHubDTO;
import Model.DistrubutionHubModel;
import Model.HubDamageModel;
import Model.HubRepairModel;
import Model.PostalCodeModel;
import ReportingMethods.*;
import SupportClass.DamagedPostalCodes;
import SupportClass.HubImpact;
import SupportClass.Point;
import View.DistrubutionHubView;
import View.HubDamageView;
import View.HubRepairView;
import View.PostalCodeView;

import java.util.*;

public class PowerService {
    Point utmCoordinates = new Point();
    Map<String, Integer> effectivePopulationServedInOneHub = new HashMap<>();
    Map<String, Float> repairTimeForAllHub = new HashMap<>();
    Map<String, Integer> hubPerPostalCode = new HashMap<>();
    Map<String, Set<String>> hubServingPostals = new HashMap<>();
    Map<String, TotalPopulationServedByHubDTO> totalPopulationServedByHubList = new HashMap<>();
    List<HubImpact> hubImapact = new ArrayList<>();

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

        if (postalCode == null || postalCode == "" || population < 0 || area < 0) {
            return false;
        } else {
            postalCode = postalCode.toUpperCase();
            PostalCodeModel postalCodeModel = new PostalCodeModel();
            PostalCodeView postalCodeView = new PostalCodeView();
            PostalCodeController postalCodeController = new PostalCodeController(postalCodeModel, postalCodeView);

            /**
             * Inserting values
             */
            postalCodeController.insertPostalCode(postalCode, population, area);
            return true;
        }
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
        if (hubIdentifier == null || hubIdentifier == "" || servicedAreas.size() == 0 || servicedAreas == null) {
            return false;
        } else {/**
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

    }

    /**
     * For Hub Damage
     *
     * @param hubIdentifier
     * @param repairEstimate
     */
    void hubDamage(String hubIdentifier, float repairEstimate) {
        try {
            if (repairEstimate < 0) {
                throw new RuntimeException("repairEstimate cannot be negative");
            } else {
                HubDamageModel hubDamageModel = new HubDamageModel();
                HubDamageView hubDamageView = new HubDamageView();
                HubDamageController hubDamageController = new HubDamageController(hubDamageView, hubDamageModel);

                hubDamageController.insertHubDamage(hubIdentifier, repairEstimate);
                // hubDamageController.viewHubDamage(hubIdentifier,repairEstimate);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong please try again : " + e.getMessage());
        }
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
        try {
            if (repairTime < 0) {
                throw new RuntimeException("repair time cannot be negative");
            } else {
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
        } catch (Exception e) {
            System.out.println("Something went wrong please try again : " + e.getMessage());
        }

    }

    /**
     * gives the no of people out of service
     *
     * @return
     */
    int peopleOutOfService() {
        PeopleOutOfService peopleOutOfService = new PeopleOutOfService();
        List<PeopleOutOfServiceDTO> postalCodeDetails = peopleOutOfService.fetchPostalCodesDetails(); // gives the
        // population
        Map<String, Integer> hubPerPopulation = peopleOutOfService.getHubsPopulationPerPopulation(postalCodeDetails); // hubs
        // per
        // population
        Map<String, Float> repairTimePerPostal = peopleOutOfService.fetchPostalCodesRepairTime(); // time for the hubs
        // repair
        int affectedPopulation = peopleOutOfService.getPopulationAffected(postalCodeDetails, hubPerPopulation,
                repairTimePerPostal);
        Map<String, Integer> effectivePopulationServedInOneHub = peopleOutOfService.PopulationServedInOneHub();
        this.effectivePopulationServedInOneHub = effectivePopulationServedInOneHub;
        // calculations are rounded off
        return affectedPopulation;

    }

    /**
     * returns the most damaged postal codes
     *
     * @param limit
     * @return
     */
    List<DamagedPostalCodes> mostDamagedPostalCodes(int limit) {
        if (limit < 0) {
            return null;
        } else {
            // assumption - in the case of multiple postal codes requiring the same amount
            // of time, the top 4 is returned
            List<DamagedPostalCodes> damagedCodesList = new ArrayList<>();
            MostDamagedPostalCode mostDamagedPostalCode = new MostDamagedPostalCode();
            try {
                damagedCodesList = mostDamagedPostalCode.getMostDamagedPostalCodes(limit);
            } catch (Exception e) {
                System.out.println("please check the table or the value of Limit (may exceed the total)");
            }
            return damagedCodesList;
        }
    }

    /**
     * Gets the most imp to least imp hubs according to the impact
     *
     * @param limit
     * @return
     */
    List<HubImpact> fixOrder(int limit) {
        if (limit < 0) {
            return null;
        } else {
            FixOrder fixOrder = new FixOrder();
            List<HubImpact> hubImapact = new ArrayList<>();
            List<HubImpact> hubImapactSorted = new ArrayList<>();

            PeopleOutOfService peopleOutOfService = new PeopleOutOfService();
            List<PeopleOutOfServiceDTO> postalCodeDetails = peopleOutOfService.fetchPostalCodesDetails();
            Map<String, Set<String>> hubServingPostals = fixOrder.hubServingPostalCodeMap(postalCodeDetails);
            peopleOutOfService(); // to refresh the map of effective population

            // gets all the repairTimeForAllHubs
            Map<String, Float> repairTimeForAllHub = fixOrder.getRepairTimeForAllHubs();

            // gets the total population served by hub
            Map<String, TotalPopulationServedByHubDTO> totalPopulationServedByHubList = fixOrder
                    .getTotalPopulationServedByHub();

            // gets the total postal code served by one hub
            Map<String, Integer> hubPerPostalCode = fixOrder.getNoOfPostalCodeServedByHub();

            hubImapact = fixOrder.getTheFixOrderHubs(repairTimeForAllHub, totalPopulationServedByHubList,
                    hubPerPostalCode, this.effectivePopulationServedInOneHub, hubServingPostals);

            /// sort pending
            Collections.sort(hubImapact);

            if (limit >= hubImapact.size()) {
                hubImapactSorted = hubImapact;
            } else {
                for (int i = 0; i < limit; i++) {
                    hubImapactSorted.add(hubImapact.get(i));
                }
            }

            return hubImapactSorted;
        }
    }

    List<Integer> rateOfServiceRestoration(float increment) {
        if (increment < 0 || increment > 1) {
            // as percentage cannot be more than 100
            return null;
        } else {
            RateOfServiceRestoration rateOfServiceRestoration = new RateOfServiceRestoration();
            List<Integer> rateOfServiceRestorationList = new ArrayList<>();
            // the increment given is in percentage 5/100 ~ 0.05 this float should be less
            // than 100 that is 1.00
            // we need the total population
            int totalPopulation_TOTAL = rateOfServiceRestoration.getTotalPopulation();
            // the total people out of service
            int population_NOTSERVED = peopleOutOfService();
            float peopleNotHavingPowerWithinRange = 0;
            // the total people with service
            // the values may come off to be different as people out of service returns int
            // this program converts float to int hence dropping the float value to return
            // int
            // this is an assumption for this implementation
            int population_SERVED = totalPopulation_TOTAL - population_NOTSERVED;

            // the total estimated repair time for each hub
            float timeForRepairAllHubs_TOTAL = rateOfServiceRestoration.getTotalRepairTime();
            // the rate of service restoration is total popl not having power/ total repair
            // time for all hub not repaired
            float rate = ((float) (population_NOTSERVED)) / timeForRepairAllHubs_TOTAL;

            // since increment is percentage it cannot be greater than 100% or 1.00
            int i = 0;
            while (i * increment < 1) {
                // if the population percentage is less than the serviceable population then the
                // repair time hours is zero
                if ((((float) (totalPopulation_TOTAL)) * increment * i) < population_SERVED) {
                    rateOfServiceRestorationList.add(0);
                } else if ((((float) (totalPopulation_TOTAL)) * increment * i) > population_SERVED) {
                    peopleNotHavingPowerWithinRange = Math
                            .abs(((population_SERVED - (i * increment * totalPopulation_TOTAL))));
                    rateOfServiceRestorationList.add(Math.round(peopleNotHavingPowerWithinRange / rate));
                }
                i++;
            }

            return rateOfServiceRestorationList;
        }
    }

    List<HubImpact> repairPlan(String startHub, int maxDistance, float maxTime) {
        Map<String, Point> hubLocations = new HashMap<>();
        String endHub = null;

        RepairPlan repairPlan = new RepairPlan();
        hubLocations = repairPlan.getHubCoordinates();
        endHub = repairPlan.getEndHub(startHub, maxDistance, this.hubImapact, hubLocations);
        // incomplete method completed up to end hub
        /**
         *
         * This can be solved by brute force depth first search using backtracking
         * Various subtrees can be checked to see if following the constraints
         * If not the entire tree can be dropped and the state can be explored by going
         * one step behind and checking for other conditions
         * If a tress follows all the conditions that is the best path to repair all
         * hubs
         */
        return null;
    }

    /**
     * Gets the postal code of the underserved population
     *
     * @param limit
     * @return
     */
    List<String> underservedPostalByPopulation(int limit) {
        if (limit < 0) {
            return null;
        } else {
            Map<String, Integer> populationAndPostals = new HashMap<>();
            Map<String, Integer> hubsAndPostals = new HashMap<>();
            List<String> underservedPopulationsAll = new ArrayList<>();
            List<String> underservedPopulations = new ArrayList<>();
            UnderservedPostalCodes underservedPostalCodes = new UnderservedPostalCodes();
            populationAndPostals = underservedPostalCodes.getPopulationPerPostalCode();
            hubsAndPostals = underservedPostalCodes.getHubsPerPostalCode();
            underservedPopulationsAll = underservedPostalCodes.underservedPostalPopulation(hubsAndPostals,
                    populationAndPostals);

            if (limit >= underservedPopulationsAll.size()) {
                underservedPopulations = underservedPopulationsAll;
            } else {
                for (int i = 0; i < limit; i++) {
                    underservedPopulations.add(underservedPopulationsAll.get(i));
                }
            }

            return underservedPopulations;
        }
    }

    List<String> underservedPostalByArea(int limit) {

        if (limit < 0) {
            return null;
        } else {
            Map<String, Integer> populationAndPostals = new HashMap<>();
            Map<String, Integer> hubsAndPostals = new HashMap<>();
            List<String> underservedPopulationsAll = new ArrayList<>();
            List<String> underservedPopulations = new ArrayList<>();
            UnderservedPostalCodes underservedPostalCodes = new UnderservedPostalCodes();
            populationAndPostals = underservedPostalCodes.getAreaPerPostalCode();
            hubsAndPostals = underservedPostalCodes.getHubsPerPostalCode();
            underservedPopulationsAll = underservedPostalCodes.underservedPostalPopulation(hubsAndPostals,
                    populationAndPostals);

            if (limit >= underservedPopulationsAll.size()) {
                underservedPopulations = underservedPopulationsAll;
            } else {
                for (int i = 0; i < limit; i++) {
                    underservedPopulations.add(underservedPopulationsAll.get(i));
                }
            }

            return underservedPopulations;
        }

    }
}
