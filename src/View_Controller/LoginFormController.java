/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.DBConnection;
import Model.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * LoginForm.fxml Controller class
 *
 * @author Tivinia Tonga
 */
public class LoginFormController implements Initializable {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label usernamePassword;

    @FXML
    private Label loginTitle;
    
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;
    
    private User currentUser;
    private Locale locale;
    private Timestamp loginTime;
    private ResourceBundle res;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        // Close window. End program.
        Platform.exit();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) throws IOException, SQLException {
        // Save user's login time
        loginTime = new Timestamp(System.currentTimeMillis());
        
        //If user's credentials are VALID, set currentUser and open Main Screen
        PreparedStatement ps = DBConnection.getConn().prepareStatement("SELECT * FROM U05T5t.user WHERE userName=? AND password=?");
        ps.setString(1, this.usernameTextField.getText());
        ps.setString(2, this.passwordTextField.getText());
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()){
            currentUser = currentUser.extractUserData(rs);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/Appointments.fxml"));
            View_Controller.AppointmentsController controller = new View_Controller.AppointmentsController(currentUser, loginTime, true);
            loader.setController(controller);
            
            recordLogin();
            
            // Close this window
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();

            // Open Main Window
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        // If user's credentials are NOT VALID, display error message
        else{
            Alert alertPart = new Alert(Alert.AlertType.ERROR);
            alertPart.setTitle("Incorrect Login");
            alertPart.setContentText(res.getString("error"));
            alertPart.showAndWait();
        }
    }
    
    private void recordLogin(){
        String username = currentUser.getUserName();
        String filename = currentUser.getUserName() + ".txt";
        String directory = System.getProperty("user.dir");
        String absoluteFilePath = directory + File.separator + filename;
        
        try{
            // Create File
            File file = new File(absoluteFilePath);

            // Check if file already exists
            if (file.exists()){ // If it exists, add onto file
                FileWriter fileWriter = new FileWriter(file,true);
                BufferedWriter fileOut = new BufferedWriter(fileWriter);
                fileOut.write(username + "\t" + this.loginTime);
                fileOut.newLine();
                fileOut.close();
                fileWriter.close();
            }
            else{ // Otherwise, write into the new file
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file,true);
                BufferedWriter fileOut = new BufferedWriter(fileWriter);
                fileOut.write(username + "\t" + this.loginTime);
                fileOut.newLine();
                fileOut.close();
                fileWriter.close();
            }
        }
        catch(IOException ie){
            ie.printStackTrace();
        }
    }
    
    /**
     * Initializes the controller class and initializes database connection
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConnection.init();
        currentUser = new User();
        
        locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("Login", locale);
        res = rb;
        
        loginTitle.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        usernamePassword.setText(rb.getString("password"));
        submitBtn.setText(rb.getString("submit"));
        cancelBtn.setText(rb.getString("cancel"));
    }    
    
}
