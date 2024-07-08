package com.exam.portal.student;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.login.LoginController;
import com.exam.portal.models.Student;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teams.student.StudentTeamsController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ResourceBundle;


public class StudentController implements Initializable {
    public static Student student;

    @FXML
    Label lblName;
    @FXML
    Hyperlink lblMail;
    @FXML
    Label lblPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(lblMail != null){
            lblMail.setText(student.getEmail());    //setting student details on dashboard.
            lblName.setText(student.getName());
            lblPhone.setText(student.getContactNo());
        }else{
            newName.setText(student.getName());
            newMail.setText(student.getEmail());     //if current stage is of edit than setting old details on fields.
            newNo.setText(student.getContactNo());
        }
    }

    //open edit details window for editing details.
    public void editDetails(ActionEvent actionEvent) {
        String path = "edit.fxml";
        changeStage(path,"Edit Details",500,500);
    }

    //opening team window of selected team.
    public void teamsClicked(ActionEvent actionEvent) {
        String path = "../teams/student/studentTeams.fxml";
        changeStage(path,"Exam Portal",600,600);
    }

    //opening window which shows all the exams.
    public void examsClicked(ActionEvent actionEvent) {
        String path = "../exams/scheduled/scheduledExam.fxml";
        ScheduledExam.fromTeacher = false;
        changeStage(path,"Scheduled Exams",600,600);
    }

    public void coursesClicked(ActionEvent actionEvent) {
    }

    //logging out student.
    public void logout(ActionEvent actionEvent) {
        String path = "../login/login.fxml";
        LoginController.isSignUp = false;
        removeCredentials();                                 //removing saved credentials in file.
        changeStage(path,"Login",600,600);
    }

    public void gotoJoinTeam(ActionEvent actionEvent) {
        String path = "joinWithId.fxml";
        changeStage(path,"Join with Team ID",300,300);
    }

    //create a new stage and show it.
    public void changeStage(String path,String title,int width,int height){
        try {
            Stage stage;
            if(title.equals("Join with Team ID") || title.equals("Edit Details"))
                stage = new Stage();
            else{
                stage = (Stage) lblName.getScene().getWindow();
                StudentTeamsController.stage = stage;
                ScheduledExam.stage = stage;
            }
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Edit Detail Section
    @FXML
    TextField newName;
    @FXML
    TextField newMail;
    @FXML
    TextField newNo;
    @FXML
    TextField Pass;

    @FXML
    void editSubmitted(ActionEvent event) {
        if(newMail.getText().equals("") || newName.getText().equals("") || newNo.getText().equals("") || Pass.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);     //checking all the fields are filled or not.
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("All fields are mandatory.");
            alert.showAndWait();
        }else{
            Student student1 = new Student();
            student1.setStudentId(student.getStudentId());
            student1.setName(newName.getText());
            student1.setEmail(newMail.getText());
            student1.setContactNo(newNo.getText());student1.setPassword(getHash(Pass.getText()));

            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(server.updateStudent(student1)){                     //sending update request.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Update Successful.");
                    alert.showAndWait();
                    Stage stage = (Stage) newName.getScene().getWindow();
                    stage.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Update Failed");
                    alert.setContentText("Check you password or email.");
                    alert.showAndWait();
                }
            });
        }
    }


    //generate md5 hash value using given text.
    private String getHash(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            String hash=number.toString();
            while (hash.length()<32){
                hash = "v"+hash;
            }
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //removing credentials saved in file.
    private void removeCredentials(){
        try {
            String dirPath = File.listRoots()[1]+"\\Exam_Portal";
            String filePath = "\\cd.ep";
            Files.deleteIfExists(Paths.get(dirPath+filePath));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
