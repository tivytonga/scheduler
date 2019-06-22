/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.User;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tivyt
 */
public class MainScreenController implements Initializable {
    
      @FXML
    private Button customerBtn;

    @FXML
    private Button apptBtn;
    
    @FXML
    private Button logoutBtn;
    
    final private User currentUser;
    final private Timestamp loginTime;
    
    public MainScreenController(User currentUser, Timestamp loginTime){
        this.currentUser = currentUser;
        this.loginTime = loginTime;
    }

    @FXML
    void apptBtnClicked(ActionEvent event) throws SQLException, IOException 
    {
        // Close this window
        Stage stage = (Stage) apptBtn.getScene().getWindow();
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
     *  Open "Customer Records" window
     */
    @FXML
    void customerBtnClicked(ActionEvent event) throws IOException 
    {
        // Close this window
        Stage stage = (Stage) customerBtn.getScene().getWindow();
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
    
    /*
     *  Log out the User and return to Login Screen
     */
    @FXML
    void logoutBtnClicked(ActionEvent event) throws IOException 
    {
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
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       
    }    
    
}
