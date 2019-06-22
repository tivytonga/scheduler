/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.DBConnection;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tivyt
 */
public class ReportsController implements Initializable {
    
    @FXML
    private Button apptReportBtn;

    @FXML
    private Button consultantReportBtn;

    @FXML
    private Button customerReportBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ChoiceBox<User> consultantChooser;

    @FXML
    private ChoiceBox<Customer> customerChooser;

    @FXML
    private TableView<Appointment> reportTbl;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, Timestamp> startCol;

    @FXML
    private TableColumn<Appointment, Timestamp> endCol;

    @FXML
    private TableColumn<Appointment, String> urlCol;
    
    @FXML
    private ChoiceBox<String> monthChooser;
    
    private ObservableList<User> consultantObsList;
    private ObservableList<Customer> customerObsList;
    private ObservableList<Appointment> apptObsList;
    private ObservableList<String> months;
    private ArrayList<User> consultantList;
    private ArrayList<Customer> customerList;
    private ArrayList<Appointment> apptList;
    private User currentUser;
    
    public ReportsController(User currentUser){
        this.currentUser = currentUser;
        this.consultantList = new ArrayList();
        this.customerList = new ArrayList();
        this.apptList = new ArrayList();
    }

    /*
     *  Number of appointment types by month
     */
    @FXML
    void apptReportBtnClicked(ActionEvent event) throws IOException {
        ArrayList<Integer> typeResults = new ArrayList();
        int selMonthInt = this.monthChooser.getSelectionModel().getSelectedIndex() + 1; // get selected month number
        int meetingCount = 0;
        int lunchCount = 0;
        int conferenceCount = 0;
        int teamCount = 0;
        
        for (int i = 0; i < this.apptList.size(); i++){
            int month = this.extractMonth(this.apptList.get(i).getStart());
            String apptType = this.apptList.get(i).getType();
            if (month == selMonthInt){
                if (apptType.equals("Meeting"))
                    meetingCount++;
                else if (apptType.equals("Lunch"))
                    lunchCount++;
                else if (apptType.equals("Conference"))
                    conferenceCount++;
                else if (apptType.equals("Team"))
                    teamCount++;
            }
        }
        
        typeResults.add(meetingCount);
        typeResults.add(lunchCount);
        typeResults.add(conferenceCount);
        typeResults.add(teamCount);    
        
        // Load Appointments Window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/TypeReport.fxml"));
        View_Controller.TypeReportController controller = new View_Controller.TypeReportController(this.monthChooser.getValue(), typeResults);
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
     *  Extract the month part of the timestamp and return the int of that month
     */
    private int extractMonth(Timestamp timestamp){
        
        // Break timestamp into tokens
        String[] tokens = timestamp.toString().split(" ");
        
        // tokens[0] is date stamp
        String date = tokens[0];
        
        // substring loc 5-7 is month
        String month = date.substring(5, 7);
        
        // parse substring to int
        int extractedMonth = Integer.parseInt(month);
        
        return extractedMonth;
    }

    /*
     *  All appointments associated with selected consultant
     */
    @FXML
    void consultantReportBtnClicked(ActionEvent event) {
        User selectedConsultant = this.consultantChooser.getSelectionModel().getSelectedItem();
        ArrayList<Appointment> assocAppts = this.getAppointments(selectedConsultant);
        this.setupTableView(assocAppts);
    }

    /*
     *  All appointments associated with selected customer
     */
    @FXML
    void customerReportBtnClicked(ActionEvent event) {
        Customer selectedCustomer = this.customerChooser.getSelectionModel().getSelectedItem();
        ArrayList<Appointment> assocAppts = this.getAppointments(selectedCustomer);
        this.setupTableView(assocAppts);
    }
    
    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        // Close this window
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
    
    private void getDatabase(){
        try{
            // Pull from User Database
            String user = "SELECT * FROM U05T5t.user";
            PreparedStatement userStmt = DBConnection.getConn().prepareStatement(user);
            ResultSet userRslt = userStmt.executeQuery();
            
            while (userRslt.next()){ // populate consultantList
                User tempUser = new User();
                tempUser.setUserId(userRslt.getInt("userId"));
                tempUser.setUserName(userRslt.getString("userName"));
                this.consultantList.add(tempUser);                
            }
            
            // Pull from Customer Database
            String customer = "SELECT * FROM U05T5t.customer";
            PreparedStatement customerStmt = DBConnection.getConn().prepareStatement(customer);
            ResultSet customerRslt = customerStmt.executeQuery();
            
            while (customerRslt.next()){ // populate customerList
                Customer tempCustomer = new Customer();
                tempCustomer.setCustomerId(customerRslt.getInt("customerId"));
                tempCustomer.setCustomerName(customerRslt.getString("customerName"));
                this.customerList.add(tempCustomer);
            }
            
            // Pull from Appointment Database
            String appt = "SELECT * FROM U05T5t.appointment";
            PreparedStatement apptStmt = DBConnection.getConn().prepareStatement(appt);
            ResultSet apptRslt = apptStmt.executeQuery();
            
            while (apptRslt.next()){ // populate apptList
                Appointment tempAppt = new Appointment();
                tempAppt.setTitle(apptRslt.getString("title"));
                tempAppt.setType(apptRslt.getString("type"));
                tempAppt.setDescription(apptRslt.getString("description"));
                tempAppt.setLocation(apptRslt.getString("location"));
                tempAppt.setContact(apptRslt.getString("contact"));
                tempAppt.setStart(apptRslt.getTimestamp("start"));
                tempAppt.setEnd(apptRslt.getTimestamp("end"));
                tempAppt.setCustomerId(apptRslt.getInt("customerId"));
                tempAppt.setUserId(apptRslt.getInt("userId"));
                tempAppt.setUrl(apptRslt.getString("url"));
                this.apptList.add(tempAppt);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        // Convert ArrayLists to corresponding ObservableLists
        this.consultantObsList = FXCollections.observableArrayList(consultantList);
        this.customerObsList = FXCollections.observableArrayList(customerList);
        this.apptObsList = FXCollections.observableArrayList(apptList);
    }
    
    private void populateChoosers(){
        // Set Consultant Chooser items
        this.consultantChooser.setItems(consultantObsList);
        this.consultantChooser.getSelectionModel().selectFirst();
        
        // Set Customer Chooser items
        this.customerChooser.setItems(customerObsList);
        this.customerChooser.getSelectionModel().selectFirst();
        
        // Set Month Chooser items
        months = FXCollections.observableArrayList(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December");
        this.monthChooser.setItems(months);
        this.monthChooser.getSelectionModel().selectFirst();
    }
    
    private ArrayList<Appointment> getAppointments(Customer cust){
        ArrayList<Appointment> assocAppts = new ArrayList();
        
        for (int i = 0; i < apptList.size(); i++){
            if (apptList.get(i).getCustomerId() == cust.getCustomerId()){
                assocAppts.add(apptList.get(i));
            }
        }
        
        return assocAppts;
    }
    
    private ArrayList<Appointment> getAppointments(User consult){
        ArrayList<Appointment> assocAppts = new ArrayList();
        
        for (int i = 0; i < apptList.size(); i++){
            if (apptList.get(i).getUserId() == consult.getUserId()){
                assocAppts.add(apptList.get(i));
            }
        }
        
        return assocAppts;
    }
    
    private void getApptTypesCount(){
        
    }
    
    private void setupTableView(ArrayList<Appointment> assocAppts){
        // Associate all columns with corresponding appointment properties
        this.titleCol.setCellValueFactory(new PropertyValueFactory("title"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        this.contactCol.setCellValueFactory(new PropertyValueFactory("contact"));
        this.urlCol.setCellValueFactory(new PropertyValueFactory("url"));
        this.startCol.setCellValueFactory(new PropertyValueFactory("start"));
        this.endCol.setCellValueFactory(new PropertyValueFactory("end"));
        
        // Convert to ObservableList
        ObservableList<Appointment> assocObsList = FXCollections.observableArrayList(assocAppts);
        
        // Display
        this.reportTbl.setItems(assocObsList);
        this.reportTbl.refresh();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getDatabase();
        populateChoosers();
    }    
    
}
