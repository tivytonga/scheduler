/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.DBConnection;
import Model.Extractor;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Appointments.fxml Controller class
 *
 * @author Tivinia Tonga
 */
public class AppointmentsController implements Initializable {
     @FXML
    private TableView<Appointment> apptTbl;
     
    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, String> urlCol;

    @FXML
    private TableColumn<Appointment, Timestamp> startCol;

    @FXML
    private TableColumn<Appointment, Timestamp> endCol;

    @FXML
    private RadioButton monthRadioBtn;

    @FXML
    private RadioButton weekRadioBtn;
    
    @FXML
    private RadioButton allRadioBtn;
    
    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;
    
    @FXML
    private Button deleteBtn;   
    
    @FXML
    private Button reportsBtn;    

    @FXML
    private Button customerRecordsBtn;
    
    @FXML
    private Button logoutBtn;    
    
    @FXML
    private Button customerBtn;
    
    final private User currentUser;
    private Appointment selectedAppt;
    private ObservableList<Appointment> apptList;
    private ObservableList<Appointment> monthList;
    private ObservableList<Appointment> weekList;
    private ArrayList<Appointment> apptDataList;
    private ArrayList<Appointment> monthDataList;
    private ArrayList<Appointment> weekDataList;
    private ArrayList<Appointment> userApptList;
    private ArrayList<Customer> customerData;
    final private Locale locale;
    private Timestamp loginTime;
    private boolean login;
    private ZoneId userZoneId;
    
    public AppointmentsController(User currentUser){
        this.currentUser = currentUser;
        locale = Locale.getDefault();
    }  
    
    public AppointmentsController(User currentUser, Timestamp loginTime, boolean login){
        this.currentUser = currentUser;
        locale = Locale.getDefault();
        userZoneId = ZoneId.systemDefault();
        this.loginTime = loginTime;
        this.login = login;
        this.userApptList = new ArrayList();
        this.customerData = new ArrayList();
        this.customerData = new ArrayList();
    } 
    
    /*
     *  Opens the Customer's Record (Modify Customer screen with selected customer)
     */
    @FXML
    void customerRecordsBtnClicked(ActionEvent event) throws IOException {        
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
     *  Close this window and open the Login Window
     */
    @FXML
    void logoutBtnClicked(ActionEvent event) throws IOException {
        //Close connection
        DBConnection.closeConn();
        
        // Close window
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
        
        // Open new Login Screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/LoginForm.fxml"));
        View_Controller.LoginFormController controller = new View_Controller.LoginFormController();
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
     *  Open the ModifyCustomer window for the selected Appointment
     */
    @FXML
    void customerBtnClicked(ActionEvent event) throws IOException {
        getCustomerData();
        Customer selectedCustomer = new Customer();
        for (int i = 0; i < customerData.size(); i++){
            if (customerData.get(i).getCustomerId() == apptTbl.getSelectionModel().getSelectedItem().getCustomerId()){
                selectedCustomer = customerData.get(i);
            }
        }
                
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
        View_Controller.ModifyCustomerController controller = new View_Controller.ModifyCustomerController(currentUser, selectedCustomer);
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void reportsBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/Reports.fxml"));
        View_Controller.ReportsController controller = new View_Controller.ReportsController(currentUser);
        loader.setController(controller);

        // Open Reports Window
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @FXML
    void addBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddAppointment.fxml"));
        View_Controller.AddAppointmentController controller = new View_Controller.AddAppointmentController(currentUser);
        loader.setController(controller);

        // Open Add Appointment Window
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Close this window
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void deleteBtnClicked(ActionEvent event) {
        try{
            // Delete selected appointment from database
            Appointment selectedAppointment = this.apptTbl.getSelectionModel().getSelectedItem();
            String deleteQuery = "DELETE FROM U05T5t.appointment WHERE appointmentId=?";
            PreparedStatement deleteStmt = DBConnection.getConn().prepareStatement(deleteQuery);
            deleteStmt.setInt(1, selectedAppointment.getAppointmentId());
            deleteStmt.execute();
            
            // Delete selected appointment from ObservableList (will update Table View)
            for (int i = 0; i < apptList.size(); i++){
                if (apptList.get(i).getAppointmentId() == selectedAppointment.getAppointmentId()){
                    apptList.remove(apptList.get(i));
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    @FXML
    void updateBtnClicked(ActionEvent event) throws IOException { 
        try{
            selectedAppt = apptTbl.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyAppointment.fxml"));
            View_Controller.ModifyAppointmentController controller = new View_Controller.ModifyAppointmentController(currentUser, selectedAppt.getAppointmentId());
            loader.setController(controller);

            // Open Update Appointment window
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Close this window
            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        }
        catch(Exception e){
            Alert alertPart = new Alert(Alert.AlertType.ERROR);
            alertPart.setTitle("Select Appointment");
            alertPart.setContentText("Must select an appointment to update.");
            alertPart.showAndWait();
        }
    }
    
    /*
     *  Fill in the Appointment Table
     */
    private void populateApptTbl(){
        titleCol.setCellValueFactory(new PropertyValueFactory("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory("contact"));
        urlCol.setCellValueFactory(new PropertyValueFactory("url"));
        startCol.setCellValueFactory(new PropertyValueFactory("start"));
        endCol.setCellValueFactory(new PropertyValueFactory("end"));
        
        apptTbl.setItems(apptList);
        apptTbl.refresh();
    }    
    
    @FXML
    void allRadioBtnSelected(ActionEvent event) {
        // Make sure the ArrayList is empty before pulling data
        this.apptDataList.clear();
        this.apptList.clear();
        
        this.apptDataList = DBConnection.getAllAppointments("SELECT * FROM U05T5t.appointment");
        this.apptList = FXCollections.observableArrayList(apptDataList);
        populateApptTbl();
    }
    
    @FXML
    void monthRadioBtnSelected(ActionEvent event) {
        ObservableList<Appointment> monthlyApptList = FXCollections.observableArrayList();
        Timestamp tempTimestamp = new Timestamp(System.currentTimeMillis());
        int currentMonth = Extractor.extractMonth(tempTimestamp);
        int startMonth = -1;
      
        // Lambda Example: Shortened code. The original code had 6 lines
        //  - loop through appointments, keep appointments with current month
        apptList.forEach((appt)->{if(Extractor.extractMonth(appt.getStart()) == currentMonth){monthlyApptList.add(appt);}});
                    
        // Update Table View  to monthly appointments
        this.apptTbl.setItems(monthlyApptList);
        this.apptTbl.refresh();
    }
    
    @FXML
    void weekRadioBtnSelected(ActionEvent event) {
        ObservableList<Appointment> weeklyApptList = FXCollections.observableArrayList();
        Timestamp tempTimestamp = new Timestamp(System.currentTimeMillis());
        int currentMonth = Extractor.extractMonth(tempTimestamp);
        int currentWeek = Extractor.extractWeek(tempTimestamp);
        int startMonth;
        int startWeek;
        
        apptList.forEach((appt)->{if(Extractor.extractWeek(appt.getStart())==currentWeek){weeklyApptList.add(appt);}});
        
        // Update Table View to weekly appointments
        this.apptTbl.setItems(weeklyApptList);
        this.apptTbl.refresh();
    }
            
    /*
     *  Checks if there are any appointments within 15 minutes of user's login
     */
    private void checkNearestAppt(){
        int month = Extractor.extractMonth(loginTime);
        int week = Extractor.extractWeek(loginTime);
        int day = Extractor.extractDay(loginTime);
        LocalTime time = Extractor.extractTime(loginTime);
        int hour = time.getHour();
        int min = time.getMinute();
             
        for (int i = 0; i < this.apptDataList.size(); i++){
            int startMonth = Extractor.extractMonth(apptDataList.get(i).getStart());
            int startWeek = Extractor.extractWeek(apptDataList.get(i).getStart());
            int startDay = Extractor.extractDay(apptDataList.get(i).getStart());
            LocalTime startTime = Extractor.extractTime(apptDataList.get(i).getStart());
            int startHour = startTime.getHour();
            int startMin = startTime.getMinute();
            
            if ((apptDataList.get(i).getUserId() == this.currentUser.getUserId()) && (month == startMonth) && (week == startWeek) && (day == startDay)){
                if ((hour == startHour) && (startMin >= min) && (startMin <= (min + 15))){ // same hour within 15 minutes
                    Alert alertPart = new Alert(Alert.AlertType.INFORMATION);
                    alertPart.setTitle("Appointment");
                    alertPart.setContentText("You have an appointment within 15 minutes!");
                    alertPart.showAndWait();
                }
                else if (((hour + 1) == startHour) && (startMin <= (min-45)) && (startMin >= 0)){ // start time is next hour but within 15 minutes
                    Alert alertPart = new Alert(Alert.AlertType.INFORMATION);
                    alertPart.setTitle("Appointment");
                    alertPart.setContentText("You have an appointment within 15 minutes!");
                    alertPart.showAndWait();
                }
                else{ // no appt within 15 mins
                    
                }
            }
        }
    }
    
        /*
     *  Pull all customer data from database and put it in a List
     */
    private void getCustomerData(){
        try{
            // Query the Customer Table
            Statement stmt = DBConnection.getConn().createStatement();
            ResultSet customerResults = stmt.executeQuery("SELECT customerName, phone, address, city, country, c.customerId, a.addressId, d.cityId, e.countryId FROM U05T5t.customer c "
                    + "JOIN U05T5t.address a ON c.addressId=a.addressId "
                    + "JOIN U05T5t.city d ON a.cityId=d.cityId "
                    + "JOIN U05T5t.country e ON d.countryId=e.countryId;");
            
            while(customerResults.next()){
                Customer cust = new Customer();
                cust.setCustomerName(customerResults.getString("customerName"));
                cust.setPhone(customerResults.getString("phone"));
                cust.setAddress(customerResults.getString("address"));
                cust.setCity(customerResults.getString("city"));
                cust.setCountry(customerResults.getString("country"));
                cust.setCustomerId(customerResults.getInt("customerId"));
                cust.setAddressId(customerResults.getInt("addressId"));
                cust.setCityId(customerResults.getInt("cityId"));
                cust.setCountryId(customerResults.getInt("countryId"));
                this.customerData.add(cust);
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    private void convertToUserTZ(){
        // apptDataList is in the timezone from the Database
        ZonedDateTime zdt = ZonedDateTime.now().toOffsetDateTime().atZoneSameInstant(this.userZoneId);
        Timestamp zdtTS = Timestamp.valueOf(zdt.toLocalDateTime());
        
        // Get user's timezone and convert each appt to that timezone
        for (Appointment appt : apptDataList){
            // Start Time
            ZonedDateTime zdtStart = ZonedDateTime.ofInstant(appt.getStart().toInstant(), this.userZoneId);
            Timestamp start = Timestamp.valueOf(zdtStart.toLocalDateTime());
            
            // End Time
            ZonedDateTime zdtEnd = ZonedDateTime.ofInstant(appt.getEnd().toInstant(), this.userZoneId);
            Timestamp end = Timestamp.valueOf(zdtEnd.toLocalDateTime());
            
            // Set new times to display in user's local timezone
            appt.setStart(start);
            appt.setEnd(end);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apptDataList = new ArrayList();
        this.apptDataList = DBConnection.getAllAppointments("SELECT * FROM U05T5t.appointment");
        this.convertToUserTZ();
        
        // Lambda Example: sort the appointments by month for the table view
        apptDataList.sort((Appointment o1, Appointment o2)->Extractor.extractMonth(o1.getStart())-Extractor.extractMonth(o2.getStart()));
        
        // Update the ObservableList
        apptList = FXCollections.observableList(apptDataList);
        
        this.populateApptTbl();
        if (login){
            this.checkNearestAppt();
        }
    }    
    
}
