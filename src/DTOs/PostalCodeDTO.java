/**
 * This is a Data Transfer object
 * This casts the 3 attributes to be stored/received in the database
 */
package DTOs;

public class PostalCodeDTO {
    private String postalCode;
    private int population;
    private int area;
    public PostalCodeDTO(String postalCode, int population, int area){
        this.area = area;
        this.postalCode = postalCode;
        this.population = population;
    }
}
