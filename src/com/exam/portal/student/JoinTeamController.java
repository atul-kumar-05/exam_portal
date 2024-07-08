package com.exam.portal.student;

import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

import static com.exam.portal.student.StudentController.student;

public class JoinTeamController {
    @FXML
    JFXButton joinWithIdClickedBtn;

    @FXML
    TextField teamId;

    @FXML
    void joinWithIdClicked(ActionEvent event) {
        if(teamId.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);     //checking all the fields are filled or not.
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Team ID mandatory");
            alert.showAndWait();
        }else{
            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(server.joinTeamWithId(teamId.getText(), student.getStudentId())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Added Successfully");
                    alert.showAndWait();
                    Stage stage = (Stage) teamId.getScene().getWindow();
                    stage.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);     //checking all the fields are filled or not.
                    alert.setHeaderText(null);
                    alert.setTitle("Warning");
                    alert.setContentText("Team ID is invalid");
                    alert.showAndWait();}
            });
        }
    }
}
