import Controller.PostalCodeController;
import Model.PostalCodeModel;
import View.PostalCodeView;

public class PowerService {
    boolean addPostalCode(String postalCode, int population, int area){
        /**
         * Setting instance of view and model
         * They are accessed through the controller
         * */
        PostalCodeModel postalCodeModel = new PostalCodeModel();
        PostalCodeView postalCodeView = new PostalCodeView();
        PostalCodeController postalCodeController = new PostalCodeController(postalCodeModel,postalCodeView);

        /**
         * Inserting values
         * */
        postalCodeController.insertPostalCode(postalCode,population,area);
        return true;
    }
}
