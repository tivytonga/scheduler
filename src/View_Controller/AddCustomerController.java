/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Model.DBConnection;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * AddCustomer.fxml Controller class
 *
 * @author Tivinia Tonga
 */
public class AddCustomerController implements Initializable {
    
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postalTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField address2TextField;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;
    private Customer customer;
    final private User currentUser;
    
    public AddCustomerController(User currentUser){
        this.currentUser = currentUser;
    }

    /*
     *  Close window without saving any changes
     */
    @FXML
    void cancelBtnClicked(ActionEvent event) throws IOException {
        // Close the window
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        
        // Load Customer Rcords Window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/CustomerRecords.fxml"));
        View_Controller.CustomerRecordsController controller = new View_Controller.CustomerRecordsController(currentUser);
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void saveBtnClicked(ActionEvent event) throws IOException {
        createCustomer();
        addCountryToDB();
        setCountryId();
        addCityToDB();
        setCityId();
        addAddressToDB();
        setAddressId();
        addCustomerToDB();
        setCustomerId();
        
        // Close the window
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
        
        // Open Customer Records Window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/CustomerRecords.fxml"));
        View_Controller.CustomerRecordsController controller = new View_Controller.CustomerRecordsController(currentUser);
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
     *  Create a new customer based on the user's inputs
     *  Update "createdBy" and "lastUpdatedBy" fields to current user
     */
    private void createCustomer(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        customer = new Customer();
        customer.setCustomerName(nameTextField.getText());
        customer.setPhone(phoneTextField.getText());
        customer.setAddress(addressTextField.getText());
        customer.setAddress2(address2TextField.getText());
        customer.setCity(cityTextField.getText());
        customer.setCountry(countryTextField.getText());
        customer.setPostalCode(postalTextField.getText());
        customer.setCreatedBy(currentUser.getUserName());
        customer.setCreateDate(timestamp);
        customer.setLastUpdateBy(currentUser.getUserName());
        customer.setLastUpdate(timestamp);
    }
    
    /*
     *  Add new customer data to Country Table in DB
     */
    private void addCountryToDB(){
        String insertQuery = "INSERT INTO U05T5t.country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?)";
        String checkQuery = "SELECT * FROM U05T5t.country WHERE country=?";
        PreparedStatement psInsert = null;
        
        try{
            psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setString(1, customer.getCountry());
            psInsert.setTimestamp(2, customer.getCreateDate());
            psInsert.setString(3, customer.getCreatedBy());
            psInsert.setTimestamp(4, customer.getLastUpdate());
            psInsert.setString(5, customer.getLastUpdateBy());
            
            // Check if Country DB already contains the country
            PreparedStatement psCheck = DBConnection.getConn().prepareStatement(checkQuery);
            psCheck.setString(1, customer.getCountry());
            ResultSet results = psCheck.executeQuery();           
            
            if (!results.next())
                psInsert.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Update countryId for new customer
     */
    private void setCountryId(){
        try{
            PreparedStatement ps = DBConnection.getConn().prepareStatement("SELECT * FROM U05T5t.country WHERE country=?");
            ps.setString(1, customer.getCountry());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                customer.setCountryId(rs.getInt("countryId"));
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Add new customer data to City Table in DB
     */
    private void addCityToDB(){
        String insertQuery = "INSERT INTO U05T5t.city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?,?)";
        String checkQuery = "SELECT * FROM U05T5t.city WHERE city=?";
        PreparedStatement psInsert = null;
        
        try{
            psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setString(1, customer.getCity());
            psInsert.setInt(2, customer.getCountryId());
            psInsert.setTimestamp(3, customer.getCreateDate());
            psInsert.setString(4, customer.getCreatedBy());
            psInsert.setTimestamp(5, customer.getLastUpdate());
            psInsert.setString(6, customer.getLastUpdateBy());
            
            // Check if Country DB already contains the country
            PreparedStatement psCheck = DBConnection.getConn().prepareStatement(checkQuery);
            psCheck.setString(1, customer.getCity());
            ResultSet results = psCheck.executeQuery();           
            
            if (!results.next())
                psInsert.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Update cityId for new customer
     */
    private void setCityId(){
        try{
            PreparedStatement ps = DBConnection.getConn().prepareStatement("SELECT * FROM U05T5t.city WHERE city=?");
            ps.setString(1, customer.getCity());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                customer.setCityId(rs.getInt("cityId"));
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Add new customer data to Address Table in DB
     */
    private void addAddressToDB(){
        String insertQuery = "INSERT INTO U05T5t.address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?,?,?,?,?)";
        String checkQuery = "SELECT * FROM U05T5t.address WHERE address=?";
        PreparedStatement psInsert = null;
        
        try{
            psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setString(1, customer.getAddress());
            psInsert.setString(2, customer.getAddress2());
            psInsert.setInt(3, customer.getCityId());
            psInsert.setString(4, customer.getPostalCode());
            psInsert.setString(5, customer.getPhone());
            psInsert.setTimestamp(6, customer.getCreateDate());
            psInsert.setString(7, customer.getCreatedBy());
            psInsert.setTimestamp(8, customer.getLastUpdate());
            psInsert.setString(9, customer.getLastUpdateBy());
            
            // Check if Country DB already contains the country
            PreparedStatement psCheck = DBConnection.getConn().prepareStatement(checkQuery);
            psCheck.setString(1, customer.getAddress());
            ResultSet results = psCheck.executeQuery();           
            
            if (!results.next())
                psInsert.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Update addressId for new customer
     */
    private void setAddressId(){
        try{
            PreparedStatement ps = DBConnection.getConn().prepareStatement("SELECT * FROM U05T5t.address WHERE address=?");
            ps.setString(1, customer.getAddress());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                customer.setAddressId(rs.getInt("addressId"));
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Add new customer data to Customer Table in DB
     *  Update customerId for new customer
     */
    private void addCustomerToDB(){
        String insertQuery = "INSERT INTO U05T5t.customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES(?,?,?,?,?,?,?)";
        String checkQuery = "SELECT * FROM U05T5t.customer WHERE customerName=? AND addressId=?";
        PreparedStatement psInsert = null;
        
        try{
            psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setString(1, customer.getCustomerName());
            psInsert.setInt(2, customer.getAddressId());
            psInsert.setBoolean(3, customer.getActive());
            psInsert.setTimestamp(4, customer.getCreateDate());
            psInsert.setString(5, customer.getCreatedBy());
            psInsert.setTimestamp(6, customer.getLastUpdate());
            psInsert.setString(7, customer.getLastUpdateBy());
            
            // Check if Country DB already contains the country
            PreparedStatement psCheck = DBConnection.getConn().prepareStatement(checkQuery);
            psCheck.setString(1, customer.getCustomerName());
            psCheck.setInt(2, customer.getAddressId());
            ResultSet results = psCheck.executeQuery();           
            
            if (!results.next())
                psInsert.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    /*
     *  Update customerId for new customer
     */
    private void setCustomerId(){
        try{
            PreparedStatement ps = DBConnection.getConn().prepareStatement("SELECT * FROM U05T5t.customer WHERE customerName=? AND addressId=? AND createDate=?");
            ps.setString(1, customer.getCustomerName());
            ps.setInt(2, customer.getAddressId());
            ps.setTimestamp(3, customer.getCreateDate());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                customer.setCustomerId(rs.getInt("customerId"));
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
