package View;

public class PostalCodeView {
    /**
     * Gives the view of the entered data
     * 
     * @param postalCode
     * @param population
     * @param area
     */
    public void PrintEnteredPostalCodeDetails(String postalCode, int population, int area) {
        System.out.println("Postal Code : " + postalCode);
        System.out.println("Postal Code Population  : " + population);
        System.out.println("Postal Code area : " + area);
    }
}
