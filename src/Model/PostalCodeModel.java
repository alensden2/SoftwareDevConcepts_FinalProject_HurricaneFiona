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
    String propertyFilename = "/home/cynos/IdeaProjects/finalProject_B00930528/alen/src/dbConfig.prop";


    /**
     * Constructor
     */
    public PostalCodeModel(){
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
    public boolean addPostalCodeToDataBase(String postalCode, int population, int area){
        PostalCodeDTO postalCodeDTO = new PostalCodeDTO(postalCode, population, area);
        Connection connect = null;
        Statement statement = null;
        int resultSet = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC&useSSL=false",
                    username, password);
            statement = connect.createStatement();
            statement.execute("use alen;");
            /**
             * Insert the postal Code
             */
            String stat = "INSERT INTO PostalCode (postalCode, population, area) VALUES (\""+postalCode+"\","+population+","+area+")";
            resultSet = statement.executeUpdate(stat);
            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        return true;
    }

}
