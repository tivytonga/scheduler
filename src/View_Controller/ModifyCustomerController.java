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
import java.sql.Timestamp;
import java.util.ArrayList;
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
 * FXML Controller class
 *
 * @author Tivinia Tonga
 */
public class ModifyCustomerController implements Initializable {
    
     @FXML
    private TextField nameTextField;

    @FXML
    private TextField address1TextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postalCodeTextField;

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
    
    final private User currentUser;
    final private Customer selectedCustomer;
    private Customer updatedCustomer;
    private ArrayList<Customer> countries;
    private ArrayList<Customer> cities;
    private ArrayList<Customer> addresses;
    private boolean customerChanged;
    private boolean addressChanged;
    private boolean cityChanged;
    private boolean countryChanged;
    
    public ModifyCustomerController(User currentUser, Customer selectedCustomer){
        this.currentUser = currentUser;
        this.selectedCustomer = selectedCustomer;
        this.customerChanged = false;
        this.addressChanged = false;
        this.cityChanged = false;
        this.countryChanged = false;
        this.countries = new ArrayList();
        this.cities = new ArrayList();
        this.addresses = new ArrayList();
        this.updatedCustomer = new Customer();
    }

    /*
     *  Close window without saving changes
     */
    @FXML
    void cancelBtnClicked(ActionEvent event) throws IOException 
    {
        // Close window
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        
        // Load Appointments Window
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
     *  Save all the changes to the Selected Customer's database data and close window
     */
    @FXML
    void saveBtnClicked(ActionEvent event) throws IOException {
        //If there are any changes, update the customer (set LastUpdate, LastUpdateBy first!
        this.updatedCustomer.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        this.updatedCustomer.setLastUpdateBy(this.currentUser.getUserName());
        this.update();
        
        // Close window
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
        
        // Load Appointments Window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/CustomerRecords.fxml"));
        View_Controller.CustomerRecordsController controller = new View_Controller.CustomerRecordsController(currentUser);
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void update(){
        // Update Country
        if (this.countryChanged){
            // Check if country exists in DB
            for (Customer country : this.countries) {
                if (this.countryTextField.getText().equals(country.getCountry())){
                    updatedCustomer.setCountry(country.getCountry());
                    updatedCustomer.setCountryId(country.getCountryId());
                }     
            }
            // Else, add country and update CountryId
            this.updatedCustomer.setCountry(this.countryTextField.getText());
            DBConnection.addCountryDB(updatedCustomer);
            
            // Update Country ID
            this.countries = DBConnection.getCountryDB();
            for (Customer country : this.countries){
                if (this.updatedCustomer.getCountry().equals(country.getCountry()))
                    this.updatedCustomer.setCountryId(country.getCountryId());
            }
        }
        
        // Update City
        if (this.cityChanged){
            // Check if city exists in DB
            for (Customer city : this.cities) {
                if (this.cityTextField.getText().equals(city.getCity())){
                    updatedCustomer.setCity(city.getCountry());
                    updatedCustomer.setCityId(city.getCountryId());
                }     
            }
            // Else, add city
            this.updatedCustomer.setCity(this.cityTextField.getText());
            DBConnection.addCityDB(updatedCustomer);
            
            // Update City ID
            this.cities = DBConnection.getCityDB();
            for (Customer city : this.cities){
                if (this.updatedCustomer.getCity().equals(city.getCity()))
                    this.updatedCustomer.setCityId(city.getCityId());
            }
        }
        
        // Update Address
        if (this.addressChanged){
            // Check if address exists in DB
            for (Customer address : this.addresses) {
                if (this.address1TextField.getText().equals(address.getAddress())){
                    updatedCustomer.setAddress(address.getAddress());
                    updatedCustomer.setAddressId(address.getAddressId());
                }     
            }
            // Else, add address
            this.updatedCustomer.setAddress(this.address1TextField.getText());
            this.updatedCustomer.setAddress2(this.address2TextField.getText());
            this.updatedCustomer.setPostalCode(this.postalCodeTextField.getText());
            this.updatedCustomer.setPhone(this.phoneTextField.getText());
            DBConnection.addAddressDB(updatedCustomer);
            
            // Update Address ID
            this.addresses = DBConnection.getAddressDB();
            for (Customer address : this.addresses){
                if (this.updatedCustomer.getAddress().equals(address.getAddress()))
                    this.updatedCustomer.setAddressId(address.getAddressId());
            }
        }
        
        // Update Customer
        if (this.customerChanged){
            this.updatedCustomer.setCustomerName(this.nameTextField.getText());
            for (Customer address : this.addresses){
                if (this.updatedCustomer.getAddress().equals(address.getAddress()))
                    this.updatedCustomer.setAddressId(address.getAddressId());
            }
            
        }
        DBConnection.updateCustomerDB(updatedCustomer);
    }
    
    private void setup(){
        // Set the textfields to the selected customer's data
        nameTextField.setText(selectedCustomer.getCustomerName());
        phoneTextField.setText(selectedCustomer.getPhone());
        address1TextField.setText(selectedCustomer.getAddress());
        address2TextField.setText(selectedCustomer.getAddress2());
        cityTextField.setText(selectedCustomer.getCity());
        countryTextField.setText(selectedCustomer.getCountry());
        postalCodeTextField.setText(selectedCustomer.getPostalCode());
        
        // Set all the lists
        this.countries = DBConnection.getCountryDB();
        this.cities = DBConnection.getCityDB();
        this.addresses = DBConnection.getAddressDB();
        
        // Set updated customer data
        this.updatedCustomer = this.selectedCustomer;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        this.setup();
        
        this.nameTextField.textProperty().addListener((observable, oldValue, newValue)->{
            this.customerChanged = true;
        });
        
        this.address1TextField.textProperty().addListener((obs, oldVal, newVal)->{
            this.addressChanged = true;
        });
        
        this.countryTextField.textProperty().addListener((obs, oldVal, newVal)->{
            this.countryChanged = true;
        });
        
        this.cityTextField.textProperty().addListener((obs, oldVal, newVal)->{
            this.cityChanged = true;
        });
        
        this.address1TextField.setOnMouseClicked(e -> {
            this.address1TextField.clear();
            this.address2TextField.clear();
            this.cityTextField.clear();
            this.countryTextField.clear();
            this.phoneTextField.clear();
        });
    }    
    
}
