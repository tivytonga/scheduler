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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * FXML Controller class
 *
 * @author tivyt
 */
public class ModifyAppointmentController implements Initializable {
    
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
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<String> customerChoiceBox;    

    @FXML
    private Spinner<String> startTime;

    @FXML
    private Spinner<String> endTime;

    @FXML
    private ChoiceBox<String> typeChoiceBox;
    
    @FXML
    private Spinner<String> ampmSpinner1;

    @FXML
    private Spinner<String> ampmSpinner2;
    
    final private User currentUser;
    final private int appointmentId;
    private ArrayList<Customer> customerList;
    private Appointment selectedAppt;
    private Appointment newAppt;
    ObservableList<String> typeChoices;
    private ArrayList<Appointment> existingAppointments;
    
    public ModifyAppointmentController(User currentUser, int appointmentId){
        this.currentUser = currentUser;
        this.appointmentId = appointmentId;
        this.customerList = new ArrayList();
        this.selectedAppt = new Appointment();
        this.newAppt = new Appointment();
        this.typeChoices = FXCollections.observableArrayList("Meeting", "Lunch", "Conference", "Team");
        this.existingAppointments = new ArrayList();
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
    
    /*
     *  Display the selected appointment's data in the fields
     */
    void displaySelectedAppt(){
        titleTextField.setText(selectedAppt.getTitle());
        descriptionTextField.setText(selectedAppt.getDescription());
        locationTextField.setText(selectedAppt.getLocation());
        contactTextField.setText(selectedAppt.getContact());
        urlTextField.setText(selectedAppt.getUrl());
        
        // Set default customer in Choice Box
        for (int i = 0; i < customerList.size(); i++){
            if (customerList.get(i).getCustomerId() == selectedAppt.getCustomerId()){
                customerChoiceBox.getSelectionModel().select(i);
            }
        }
        
        // Set default Type in Choice Box
        for (int i = 0; i < typeChoices.size(); i++){
            if (selectedAppt.getType().equals(typeChoices.get(i))){
                this.typeChoiceBox.setValue(this.typeChoices.get(i));
            }
        }
    }

    private void updateAppt(){
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        newAppt.setTitle(titleTextField.getText());
        newAppt.setDescription(descriptionTextField.getText());
        newAppt.setContact(contactTextField.getText());
        newAppt.setUrl(urlTextField.getText());
        newAppt.setLocation(locationTextField.getText());
        newAppt.setCreateDate(timestamp);
        newAppt.setCreatedBy(currentUser.getUserName());
        newAppt.setLastUpdate(timestamp);
        newAppt.setLastUpdateBy(currentUser.getUserName());
        newAppt.setType(typeChoiceBox.getValue());
        newAppt.setUserId(currentUser.getUserId());
        
        // Set Customer ID of selected customer
        for (int i = 0; i < customerList.size(); i++){
            if (customerList.get(i).getCustomerName().equals(this.customerChoiceBox.getValue())){
                int custId = customerList.get(i).getCustomerId();
                newAppt.setCustomerId(custId);
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
        newAppt.setStart(startTimestamp);
        newAppt.setEnd(endTimestamp);
    }
    
    /*
     *  Add the newly created appointment to the database
     */
    private void updateDatabase(){
        String insertQuery = "UPDATE U05T5t.appointment SET customerId=?, title=?, description=?, location=?, contact=?, url=?, start=?, end=?, lastUpdate=?, lastUpdateBy=?, userId=?, type=? WHERE appointmentId=?";
        
        try{
            PreparedStatement psInsert = DBConnection.getConn().prepareStatement(insertQuery);
            psInsert.setInt(1, newAppt.getCustomerId());
            psInsert.setString(2, newAppt.getTitle());
            psInsert.setString(3, newAppt.getDescription());
            psInsert.setString(4, newAppt.getLocation());
            psInsert.setString(5, newAppt.getContact());
            psInsert.setString(6, newAppt.getUrl());
            psInsert.setTimestamp(7, newAppt.getStart());
            psInsert.setTimestamp(8, newAppt.getEnd());
            psInsert.setTimestamp(9, newAppt.getLastUpdate());
            psInsert.setString(10, newAppt.getLastUpdateBy());
            psInsert.setInt(11, newAppt.getUserId());
            psInsert.setString(12, newAppt.getType());
            psInsert.setInt(13, appointmentId);
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

        SpinnerValueFactory<String> startFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(timeChoices);
        SpinnerValueFactory<String> endFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(timeChoices);
        SpinnerValueFactory<String> ampmFactory1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(ampmChoices);
        SpinnerValueFactory<String> ampmFactory2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(ampmChoices);
      
        // Start Default Values
        Integer[] displayStartTime = formatTime(this.selectedAppt.getStart()); // Returns [H], [m], [am/pm]
        LocalDateTime newDate = this.selectedAppt.getStart().toLocalDateTime();
        
        if (displayStartTime[2] == 1){ // am
            if (displayStartTime[1]==0){
                startFactory.setValue(displayStartTime[0] + ":00");
                ampmFactory1.setValue("AM");
            }
            else{
                startFactory.setValue(displayStartTime[0] + ":" + displayStartTime[1]);
                ampmFactory1.setValue("AM");
            }
        }
        else{
            if (displayStartTime[1]==0){
                startFactory.setValue(displayStartTime[0] + ":00");
                ampmFactory1.setValue("PM");
            }
            else{
                startFactory.setValue(displayStartTime[0] + ":" + displayStartTime[1]);
                ampmFactory1.setValue("PM");
            }
        }
       
        // End Default Values
        Integer[] displayEndTime = formatTime(this.selectedAppt.getEnd()); // Returns [H], [m], [am/pm]
        LocalDateTime endDate = this.selectedAppt.getEnd().toLocalDateTime();
        if (displayEndTime[2] == 1){ // am
            if (displayEndTime[1] == 0){
                endFactory.setValue(displayEndTime[0] + ":00");
                ampmFactory2.setValue("AM");
            }
            else{
                endFactory.setValue(displayEndTime[0] + ":" + displayEndTime[1]);
                ampmFactory2.setValue("AM");
            }
        }
        else{ // pm
            if (displayEndTime[1] == 0){
                endFactory.setValue(displayEndTime[0] + ":00");
                ampmFactory2.setValue("PM");
            }
            else{
                endFactory.setValue(displayEndTime[0] + ":" + displayEndTime[1]);
                ampmFactory2.setValue("PM");
            }
        }
        
        this.startTime.setValueFactory(startFactory);
        this.endTime.setValueFactory(endFactory);
        this.ampmSpinner1.setValueFactory(ampmFactory1);
        this.ampmSpinner2.setValueFactory(ampmFactory2);
        this.startDatePicker.setValue(this.selectedAppt.getStart().toLocalDateTime().toLocalDate());
        this.endDatePicker.setValue(this.selectedAppt.getEnd().toLocalDateTime().toLocalDate());
    }
    
    /*
     *  Returns an array with format 
     *    [H], [m], [am/pm]
     */
    private Integer[] formatTime(Timestamp timestamp){
        Integer[] time = new Integer[3];
        String[] tokens = timestamp.toString().split(" ");
        String[] tokens2 = tokens[1].split(":");
        int hour = Integer.parseInt(tokens2[0]);
        int min = Integer.parseInt(tokens2[1]);
        
        // special case 00:00 = 12:00 AM
        if (hour == 0){
            time[0] = 12;
            time[2] = 1;
        }
        // 1:00 AM - 11:00 AM
        else if (hour > 0 && hour < 12){
            time[0] = hour;
            time[2] = 1;
        }
        // Special case 12:00 PM
        else if (hour == 12){
            time[0] = hour;
            time[2] = 0;
        }
        // 1:00 PM - 11:00 PM (13:00 - 23:00)
        else if (hour > 12){
            hour = hour - 12;
            time[0] = hour;
            time[2] = 0; // 1=AM, 0 = PM
        }
        
        time[1] = min;
        
        return time;
    }
   
    @FXML
    void saveBtnClicked(ActionEvent event) throws IOException {
        if (overlapCheck() || businessHoursCheck()){
            Alert alertPart = new Alert(Alert.AlertType.WARNING);
            alertPart.setTitle("Appointment");
            alertPart.setContentText("Invalid appointment time. Please change appointment time.");
            alertPart.showAndWait();
        }
        else{
            updateAppt();
            updateDatabase();

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
    }
    
    /*
     *  Check if the selected appointment time falls within business hours
     *  - return true if it falls within business hours
     *  - return false if it falls outside of business hours
     */
    private boolean businessHoursCheck(){
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
        
        if (Extractor.extractHour(startTimestamp) < 9 || Extractor.extractHour(endTimestamp) > 17){
            return false;
        }
        
        return true;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.customerList = DBConnection.getAllCustomers("SELECT * FROM U05T5t.customer");
        this.selectedAppt = DBConnection.getSelectedAppointment("SELECT * FROM U05T5t.appointment WHERE appointmentId=?", appointmentId);
        this.existingAppointments = DBConnection.getAllAppointments("SELECT * FROM U05T5t.appointment");

        ObservableList<String> customerChoices = FXCollections.observableArrayList();
        for (int i = 0; i < customerList.size(); i++){
            customerChoices.add(customerList.get(i).getCustomerName());
        }
        this.customerChoiceBox.setItems(customerChoices);
        displaySelectedAppt();
        setupTimeIntervals();
        this.typeChoiceBox.setItems(typeChoices);
    }    
    
}
