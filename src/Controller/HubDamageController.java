package Controller;

import Model.HubDamageModel;
import View.HubDamageView;

public class HubDamageController {
    private HubDamageModel hubDamageModel;
    private HubDamageView hubDamageView;

    /**
     * Controller
     * 
     * @param hubDamageView
     * @param hubDamageModel
     */
    public HubDamageController(HubDamageView hubDamageView, HubDamageModel hubDamageModel) {
        this.hubDamageModel = hubDamageModel;
        this.hubDamageView = hubDamageView;
    }

    /**
     * inserts the details in the db
     * Creates the required table
     * 
     * @param hubIdentifier
     * @param repairTimeEstimate
     */
    public void insertHubDamage(String hubIdentifier, float repairTimeEstimate) {
        hubDamageModel.createHubDamageTable();
        hubDamageModel.addHubDamageToDataBase(hubIdentifier, repairTimeEstimate);
    }

    /**
     * view the entered value
     * 
     * @param hubIdentifier
     * @param repairTimeEstimate
     */
    public void viewHubDamage(String hubIdentifier, float repairTimeEstimate) {
        hubDamageView.PrintEnteredHubDamageDetails(hubIdentifier, repairTimeEstimate);
    }
}
