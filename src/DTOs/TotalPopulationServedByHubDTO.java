package DTOs;

public class TotalPopulationServedByHubDTO {
    public String totalPopulationServedByHub;
    public String postalCode;
    public String hubIdentifier;

    public TotalPopulationServedByHubDTO(String totalPopulationServedByHub, String postalCode, String hubIdentifier) {
        this.totalPopulationServedByHub = totalPopulationServedByHub;
        this.postalCode = postalCode;
        this.hubIdentifier = hubIdentifier;
    }
}
