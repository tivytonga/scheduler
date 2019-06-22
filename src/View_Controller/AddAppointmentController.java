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
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * AddAppointment.fxml Controller class
 *
 * @author Tivinia Tonga
 */
public class AddAppointmentController implements Initializable {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField urlTextField;

    @FXML
    private TextField locationTextField;
    
    @FXML
    private ChoiceBox<Time> startTimeChooser;

    @FXML
    private ChoiceBox<Time> endTimeChooser;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private Spinner<String> startTime;

    @FXML
    private Spinner<String> endTime;    

    @FXML
    private ChoiceBox<String> customerChoiceBox;
    
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    
    @FXML
    private Spinner<String> ampmSpinner1;

    @FXML
    private Spinner<String> ampmSpinner2;

    
    final private User currentUser;
    private Appointment appt;
    private ArrayList<Customer> customerList;
    private ArrayList<Appointment> existingAppointments;
    
    public AddAppointmentController(User currentUser){
        this.currentUser = currentUser;
        this.appt = new Appointment();
        this.customerList = new ArrayList();
    }

    @FXML
    void cancelBtnClicked(ActionEvent event) throws IOException {
        // Close this window
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        
        // Load Appointments Window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/Appointments.fxml"));
        View_Controller.AppointmentsController controller = new View_Controller.AppointmentsController(currentUser);
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //  FIGURE OUT HOW TO MAKE AN ALERT POP UP FOR OVERLAPPING APPOINTMENT TIMES SO THAT WHEN THE USER CLICKS "OKAY" THE WINDOW DOES NOT CLOSE
    //     AND ALLOWS USER TO SELECT A DIFFERENT TIME. CHECK HOW IT IS DONE IN OTHER WINDOWS/SCREENS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @FXML
    void saveBtnClicked(ActionEvent event) throws IOException {
        if (createAppointment()){
            addToDatabase();
            
            // Close this window
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        
            // Load Appointments Window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/Appointments.fxml"));
            View_Controller.AppointmentsController controller = new View_Controller.AppointmentsController(currentUser);
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else{
            Alert alertPart = new Alert(Alert.AlertType.ERROR);
            alertPart.setTitle("Appointment");
            alertPart.setContentText("Invalid appointment time. Please change appointment time.");
            alertPart.showAndWait();
        }
        
        
        
    }
    
    /*
     *  Check if new appointment overlaps with any existing appointments
     */
    private boolean overlapCheck(){
        // User's selections
        Timestamp selectedTime = Extractor.getTimestamp(this.startDatePicker.getValue(), this.startTime.getValue(), this.ampmSpinner1.getValue());
        
        // Compare user's selections to existing appointments
        for (int i = 0; i < this.existingAppointments.size(); i++){
            // After start time AND before end time
            if ((selectedTime.after(this.existingAppointments.get(i).getStart()) || selectedTime.equals(this.existingAppointments.get(i).getStart())) 
                    && (selectedTime.before(this.existingAppointments.get(i).getEnd()) || selectedTime.equals(this.existingAppointments.get(i).getEnd()))){
                return true;
            }
        }
        return false;
    }
    
    /*
     *  Populate the Customer Choice Box with all customers in the database
     */
    private void populateCustomerChoices(){
        ObservableList<String> custChoices = FXCollections.observableArrayList();
        
        for (int i = 0; i < customerList.size(); i++){
            custChoices.add(customerList.get(i).getCustomerName());
        }
        
        customerChoiceBox.setItems(custChoices);
    }
    
    /*
     *  Populates the Type Choice Box with all meeting types
     */
    private void populateTypeChoices(){
        ObservableList<String> typeChoices = FXCollections.observableArrayList();
        typeChoices.add("Meeting");
        typeChoices.add("Lunch");
        typeChoices.add("Conference");
        typeChoices.add("Team");
        
        this.typeChoiceBox.setItems(typeChoices);
    }
    
    /*
     *  Create appointment based on user's inputs
     */
    private boolean createAppointment(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        appt.setTitle(titleTextField.getText());
        appt.setDescription(descriptionTextField.getText());
        appt.setContact(contactTextField.getText());
        appt.setUrl(urlTextField.getText());
        appt.setLocation(locationTextField.getText());
        appt.setCreateDate(timestamp);
        appt.setCreatedBy(currentUser.getUserName());
        appt.setLastUpdate(timestamp);
        appt.setLastUpdateBy(currentUser.getUserName());
        appt.setType(typeChoiceBox.getValue());
        appt.setUserId(currentUser.getUserId());
        
        // Set Customer ID of selected customer
        for (int i = 0; i < customerList.size(); i++){
            if (customerList.get(i).getCustomerName().equals(this.customerChoiceBox.getValue())){
                int custId = customerList.get(i).getCustomerId();
                appt.setCustomerId(custId);
            }
        }
        
        // Get date "YYYY-MM-DD"
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        // Get time
        String startString = this.startTime.getValue();
        String endString = this.endTime.getValue();
        String ampmStart = this.ampmSpinner1.getValue();
        String ampmEnd = this.ampmSpinner2.getValue();
        
        // Create a timestamp and set the Start and End values of Appointment
        Timestamp startTimestamp = Extractor.getTimestamp(startDate, startString, ampmStart);   
        Timestamp endTimestamp = Extractor.getTimestamp(endDate, endString, ampmEnd);
        
        // Overlap check
        if(this.overlapCheck())
        {
            return false;
        }
        else if (Extractor.extractHour(startTimestamp) < 9 || Extractor.extractHour(endTimestamp) > 17){
            return false;
        }
        else{
            appt.setStart(startTimestamp);
            appt.setEnd(endTimestamp);
            return true;
        }
    }
    
    /*
     *  Add the newly created appointment to the database
     */
    private void addToDatabase(){
        String insertQuery = "INSERT INTO U05T5t.appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy, userId, type)"
        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        try{
            PreparedStatement psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setInt(1, appt.getCustomerId());
            psInsert.setString(2, appt.getTitle());
            psInsert.setString(3, appt.getDescription());
            psInsert.setString(4, appt.getLocation());
            psInsert.setString(5, appt.getContact());
            psInsert.setString(6, appt.getUrl());
            psInsert.setTimestamp(7, appt.getStart());
            psInsert.setTimestamp(8, appt.getEnd());
            psInsert.setTimestamp(9, appt.getCreateDate());
            psInsert.setString(10, appt.getCreatedBy());
            psInsert.setTimestamp(11, appt.getLastUpdate());
            psInsert.setString(12, appt.getLastUpdateBy());
            psInsert.setInt(13, appt.getUserId());
            psInsert.setString(14, appt.getType());
            
            psInsert.execute();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    private void setupTimeIntervals(){
        ObservableList<String> ampmChoices = FXCollections.observableArrayList("AM", "PM");
        ObservableList<String> timeChoices = FXCollections.observableArrayList(
                "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
                "8:00", "9:00", "10:00", "11:00");
        
        String str = "1986-04-08 12:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        Timestamp tempTime = Timestamp.valueOf(dateTime);

        SpinnerValueFactory<String> startFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(timeChoices);
        SpinnerValueFactory<String> endFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(timeChoices);
        SpinnerValueFactory<String> ampmFactory1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(ampmChoices);
        SpinnerValueFactory<String> ampmFactory2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(ampmChoices);
      
       // Default value
       startFactory.setValue("9:00");
       endFactory.setValue("9:00");
       ampmFactory1.setValue("AM");
       ampmFactory2.setValue("AM");
 
       startTime.setValueFactory(startFactory);
       endTime.setValueFactory(endFactory);
       this.ampmSpinner1.setValueFactory(ampmFactory1);
       this.ampmSpinner2.setValueFactory(ampmFactory2);
    }
    
    private void testSpinner(){
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.existingAppointments = DBConnection.getAllAppointments("SELECT * FROM U05T5t.appointment");
        setupTimeIntervals();
//        getCustomerData();
        this.customerList = DBConnection.getAllCustomers("SELECT * FROM U05T5t.customer");
        populateCustomerChoices();
        populateTypeChoices();
    }    
    
}
