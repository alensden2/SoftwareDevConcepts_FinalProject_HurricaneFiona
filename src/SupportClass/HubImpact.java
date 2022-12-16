package SupportClass;

/**
 * Stores the hub impact
 */
public class HubImpact implements Comparable<HubImpact> {
    public String hubIdentifier;
    public float impactValue;

    public String getHubIdentifier() {
        return hubIdentifier;
    }

    public void setHubIdentifier(String hubIdentifier) {
        this.hubIdentifier = hubIdentifier;
    }

    public float getImpactValue() {
        return impactValue;
    }

    public void setImpactValue(float impactValue) {
        this.impactValue = impactValue;
    }

    @Override
    public String toString() {
        return "HubImpact{" +
                "hubIdentifier='" + hubIdentifier + '\'' +
                ", impactValue=" + impactValue +
                '}';
    }

    @Override
    public int compareTo(HubImpact o) {
        int compareImpactValue = 0;
        compareImpactValue = (int) ((HubImpact) o).getImpactValue();

        // For Ascending order
        return (int) (compareImpactValue - this.impactValue);
    }
}
