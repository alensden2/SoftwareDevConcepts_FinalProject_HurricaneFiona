package View;

public class HubRepairView {
    public void PrintEnteredHubRepairDetails(String hubIdentifier, String employeeID, float repairTime,
            boolean inService) {
        System.out.println("Hub ID : " + hubIdentifier);
        System.out.println("Employee ID : " + employeeID);
        System.out.println("Repair time: " + repairTime);
        System.out.println("Hub status: " + inService);
    }
}
