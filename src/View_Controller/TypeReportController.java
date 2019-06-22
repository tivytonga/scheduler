/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tivyt
 */
public class TypeReportController implements Initializable {
    
    @FXML
    private Label meetingTotal;

    @FXML
    private Label lunchTotal;

    @FXML
    private Label conferenceTotal;

    @FXML
    private Label teamTotal;
    
    @FXML
    private Label monthText;
    
    @FXML
    private Button closeBtn;
    
    private ArrayList<Integer> typesByMonth;
    private String selectedMonth = "";
    
    public TypeReportController(String selectedMonth, ArrayList<Integer> typesByMonth){
        this.typesByMonth = typesByMonth;
        this.selectedMonth = selectedMonth;
    }

    @FXML
    void closeBtnClicked(ActionEvent event) {
        // Close this window
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
    
    void displayResults(){
        this.monthText.setText(selectedMonth);
        meetingTotal.setText(typesByMonth.get(0).toString());
        lunchTotal.setText(typesByMonth.get(1).toString());
        conferenceTotal.setText(typesByMonth.get(2).toString());
        teamTotal.setText(typesByMonth.get(3).toString());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayResults();
    }    
    
}
