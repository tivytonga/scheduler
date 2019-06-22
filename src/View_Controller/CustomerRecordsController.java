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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tivinia Tonga
 */
public class CustomerRecordsController implements Initializable {
    
     @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TableView<Customer> customerTbl;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, Integer> phoneCol;

    @FXML
    private TableColumn<Customer, Integer> addressCol;

    @FXML
    private TableColumn<Customer, String> cityCol;

    @FXML
    private TableColumn<Customer, String> countryCol;
    
    private ObservableList<Customer> customerList;
    private List<Customer> customerData;
    final private User currentUser;
    
    /*
     *  Constructor: takes a User parameter
     *  @param User - the user that is logged into the Scheduler
     */
    public CustomerRecordsController(User currentUser){
        this.currentUser = currentUser;
    }
    
    /*
     *  Pull all Customer data from Country, City, Address, and Customer Databases
     */
    private void getCustomerData(){
        ArrayList<Customer> countries = DBConnection.getCountryDB();
        ArrayList<Customer> cities = DBConnection.getCityDB();
        ArrayList<Customer> addresses = DBConnection.getAddressDB();
        ArrayList<Customer> allCustomers = DBConnection.getAllCustomers("SELECT * FROM U05T5t.customer");
        
        for (Customer customer : allCustomers){
            
            // Add address IDs to customers
            for (Customer address : addresses){
                if (customer.getAddressId() == address.getAddressId()){
                    customer.setAddress(address.getAddress());
                    customer.setAddress2(address.getAddress2());
                    customer.setPostalCode(address.getPostalCode());
                    customer.setPhone(address.getPhone());
                    customer.setCityId(address.getCityId());
                }
            }
            
            // Add city IDs to customers
            for (Customer city : cities){
                if (customer.getCityId() == city.getCityId()){
                    customer.setCity(city.getCity());
                    customer.setCountryId(city.getCountryId());
                }
            }
            // Add country IDs to customers
            for (Customer country : countries){
                if (customer.getCountryId() == country.getCountryId()){
                    customer.setCountry(country.getCountry());
                }
            }
        }
        this.customerList = FXCollections.observableArrayList(allCustomers);
    }
    
    /*
     *  Create the TableView for the Customer Data
     */
    private void populateCustomerTbl(){
        getCustomerData();
        
        nameCol.setCellValueFactory(new PropertyValueFactory("customerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory("address"));
        cityCol.setCellValueFactory(new PropertyValueFactory("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory("country"));
        
        customerTbl.setItems(customerList);
        customerTbl.refresh();
    }

    /*
     *  Open "Add Customer" window
     */
    @FXML
    void addBtnClicked(ActionEvent event) throws IOException {
        // Close this window
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
        
        // Open "Add Customer" window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddCustomer.fxml"));
        View_Controller.AddCustomerController controller = new View_Controller.AddCustomerController(currentUser);
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     *  Close the window without saving any changes
     */
    @FXML
    void cancelBtnClicked(ActionEvent event) throws IOException {
        // Close window
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /*
     *  Delete the selected customer from the database/table
     */
    @FXML
    void deleteBtnClicked(ActionEvent event) {
        try{
            // TODO Delete selected customer
            Customer selectedCustomer = this.customerTbl.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM U05T5t.customer WHERE customerId=?";
            PreparedStatement deleteStmt = DBConnection.getConn().prepareStatement(deleteQuery);
            deleteStmt.setInt(1, selectedCustomer.getCustomerId());
            deleteStmt.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }        
        catch(Exception e){ // Customer is not selected before delete button is clicked
            Alert alertPart = new Alert(Alert.AlertType.ERROR);
            alertPart.setTitle("Select Customer");
            alertPart.setContentText("Must select a customer to delete.");
            alertPart.showAndWait();
        }
        
        // Refresh Customer TableView
        customerTbl.getItems().clear();
        populateCustomerTbl();
    }

    /*
     *  Save all the changes and close the window
     */
    @FXML
    void saveBtnClicked(ActionEvent event) throws IOException {
        // TODO Save all changes     
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/Appointments.fxml"));
        View_Controller.AppointmentsController controller = new View_Controller.AppointmentsController(currentUser);
        loader.setController(controller);
        
        // Close window
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
        
        // Open Main Screen
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     *  Open the "ModifyCustomer" window
     */
    @FXML
    void updateBtnClicked(ActionEvent event) throws IOException {
        try{
            Customer selectedCustomer = this.customerTbl.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
            View_Controller.ModifyCustomerController controller = new View_Controller.ModifyCustomerController(currentUser, selectedCustomer);
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Close window
            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        }
        catch(Exception e){ // Customer is not selected before Update button clicked
            e.printStackTrace();
            Alert alertPart = new Alert(Alert.AlertType.ERROR);
            alertPart.setTitle("Select Customer");
            alertPart.setContentText("Must select a customer to update.");
            alertPart.showAndWait();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerData = new ArrayList();
        getCustomerData();
        populateCustomerTbl();
    }    
    
}
