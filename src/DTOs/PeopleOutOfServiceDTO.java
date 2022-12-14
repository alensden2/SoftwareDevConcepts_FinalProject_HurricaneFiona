package DTOs;

/**
 * DTO Class
 */
public class PeopleOutOfServiceDTO {
    public String postalCode;
    public String population;
    public String hubIdentifier;

    public PeopleOutOfServiceDTO(String postalCode, String population, String hubIdentifier) {
        this.population = population;
        this.postalCode = postalCode;
        this.hubIdentifier = hubIdentifier;
    }
}
