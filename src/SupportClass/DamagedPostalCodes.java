package SupportClass;

/**
 * Container to store the postal code and time required for repair
 */
public class DamagedPostalCodes {
    private String postalCode;
    private float amtofRepairsNeeded;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public float getAmtofRepairsNeeded() {
        return amtofRepairsNeeded;
    }

    public void setAmtofRepairsNeeded(float amtofRepairsNeeded) {
        this.amtofRepairsNeeded = amtofRepairsNeeded;
    }
}
