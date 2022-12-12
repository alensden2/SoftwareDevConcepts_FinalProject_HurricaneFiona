package View;

public class DistrubutionHubView {
    /**
     * Gives the view of the entered data
     * 
     * @param hubIdentifier
     * @param x
     * @param y
     * @param postalCode
     */
    public void PrintEnteredDistrubutionHubDetails(String hubIdentifier, int x, int y, String postalCode) {
        System.out.println("Hub Identifier : " + hubIdentifier);
        System.out.println("UTM (x) : " + x);
        System.out.println("UTM (y) : " + y);
        System.out.println("Postal Code : " + postalCode);
    }
}
