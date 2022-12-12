package Controller;

import Model.DistrubutionHubModel;
import View.DistrubutionHubView;

public class DistrubutionHubController {
    /**
     * Model Object
     * View Object
     */

    private DistrubutionHubModel distrubutionHubModel;
    private DistrubutionHubView distrubutionHubView;

    /**
     * Distrubution hub controller constructor
     * 
     * @param distrubutionHubModel
     * @param distrubutionHubView
     */
    public DistrubutionHubController(DistrubutionHubModel distrubutionHubModel,
            DistrubutionHubView distrubutionHubView) {
        this.distrubutionHubModel = distrubutionHubModel;
        this.distrubutionHubView = distrubutionHubView;
    }

    /**
     * Adding the data through the model class
     * 
     * @param hubIdentifier
     * @param postalCode
     */
    public void insertHubServiceArea(String hubIdentifier, String postalCode) {
        distrubutionHubModel.createHubsTable();
        distrubutionHubModel.addHubtoDataBase(hubIdentifier, postalCode);
    }

    /**
     * Adding data
     * 
     * @param hubIdentifier
     * @param x
     * @param y
     */
    public void insertHubLocation(String hubIdentifier, int x, int y) {
        distrubutionHubModel.createHubsTable();
        distrubutionHubModel.addHubLocationtoDataBase(hubIdentifier, x, y);
    }

    /**
     * This can be used to view the entered detais
     * 
     * @param hubIdentifier
     * @param x
     * @param y
     * @param postalCode
     */
    public void viewInsertedHub(String hubIdentifier, int x, int y, String postalCode) {
        distrubutionHubView.PrintEnteredDistrubutionHubDetails(hubIdentifier, x, y, postalCode);
    }
}
