/**
 * This is the model class for PostalCode
 */
package Model;

import DTOs.PostalCodeDTO;
import database.CreateTables;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.TrustAnchor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class PostalCodeModel {
    private String postalCode;
    private int population;
    private int area;
    Properties identity = new Properties();
    String username = "";
    String password = "";
    String propertyFilename = "dbConfig.prop";


    /**
     * Constructor
     */
    PostalCodeModel(){
        /**
         * Reference from lab work
         */
        try {
            InputStream stream = new FileInputStream(propertyFilename);
            identity.load(stream);
            username = identity.getProperty("username");
            password = identity.getProperty("password");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * gets the postal code
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * sets the postal code
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * gets the population
     * @return
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the population
     * @param population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Gets the area
     * @return
     */
    public int getArea() {
        return area;
    }

    /**
     * Sets area
     * @param area
     */
    public void setArea(int area) {
        this.area = area;
    }

    /**
     * Creates the table for the postal codes
     */
    public void createPostalCodeTable(){
        CreateTables createTables = new CreateTables();
        createTables.CreatePostalCodeTable();
    }

    /**
     * Adds to the database
     * @param postalCode
     * @param population
     * @param area
     * @return
     */
    private boolean addPostalCodeToDataBase(String postalCode, int population, int area){
        PostalCodeDTO postalCodeDTO = new PostalCodeDTO(postalCode, population, area);
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /*
             * Stored procedure for getting the customer list
             */
            String stat = "";
            resultSet = statement.executeQuery(stat);

            while (resultSet.next()) {

            }

            resultSet.close();
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }

}
