package Controller;

import Model.PostalCodeModel;
import View.PostalCodeView;

public class PostalCodeController {
    /**
     * Model Object
     * View Object
     */
    private PostalCodeModel postalCodeModel;
    private PostalCodeView postalCodeView;

    /**
     * Postal code controller constructor
     */
    public PostalCodeController(PostalCodeModel postalCodeModel, PostalCodeView postalCodeView){
        this.postalCodeModel = postalCodeModel;
        this.postalCodeView = postalCodeView;
    }

    /**
     * Adding to the model class through the controller
     * */
    public void insertPostalCode(String postalCode, int population, int area){
        postalCodeModel.createPostalCodeTable();
        postalCodeModel.addPostalCodeToDataBase(postalCode,population,area);
    }

    /**
     * to view the inserted values
     * */
    public void viewInsertedPostalCode(String postalCode, int population, int area){
        postalCodeView.PrintEnteredPostalCodeDetails(postalCode,population,area);
    }

}
