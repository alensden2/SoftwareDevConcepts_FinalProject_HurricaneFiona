package Controller;

import Model.HubRepairModel;
import View.HubRepairView;

public class HubRepairController {
    private HubRepairView hubRepairView;
    private HubRepairModel hubRepairModel;

    /**
     * Controller
     * 
     * @param hubRepairView
     * @param hubRepairModel
     */
    public HubRepairController(HubRepairView hubRepairView, HubRepairModel hubRepairModel) {
        this.hubRepairModel = hubRepairModel;
        this.hubRepairView = hubRepairView;
    }

    /**
     * Inserts the repair task done
     * 
     * @param hubIdentifier
     * @param employeeID
     * @param repairTime
     * @param inService
     */
    public void insertHubRepair(String hubIdentifier, String employeeID, float repairTime, boolean inService) {
        hubRepairModel.createHubRepairTables();
        hubRepairModel.addHubRepairToDataBase(hubIdentifier, employeeID, repairTime, inService);
    }

    /**
     * Can be used to view the inserted details
     * 
     * @param hubIdentifier
     * @param employeeID
     * @param repairTime
     * @param inService
     */
    public void viewInsertedHub(String hubIdentifier, String employeeID, float repairTime, boolean inService) {
        hubRepairView.PrintEnteredHubRepairDetails(hubIdentifier, employeeID, repairTime, inService);
    }
}
