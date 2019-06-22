/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.DBConnection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author tivyt
 */
public class SchedulerApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        //testConnection();
        
        // Load Login Screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/LoginForm.fxml"));
        View_Controller.LoginFormController controller = new View_Controller.LoginFormController();
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void testConnection()
    {
        Statement stmt;
        ResultSet rs = null;
        try
        {
            DBConnection.init();
            
            stmt = DBConnection.getConn().createStatement();
            rs = stmt.executeQuery("SELECT * FROM U05T5t.customer");
            
            
            while (rs.next())
            {
                for (int i = 1; i < 7; i++)
                {
                    String customer = rs.getString(i);
                    System.out.print(customer + " ");
                }
            }
            
            DBConnection.closeConn();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
