package com.exam.portal.teacher;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.login.LoginController;
import com.exam.portal.models.Teacher;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    public static Teacher teacher;

    @FXML
     Label lblTName;

    @FXML
     Hyperlink lblTMail;

    @FXML
     Label lblTPhone;

    @FXML
     Button btnTeams;

    @FXML
     Button btnExams;

    @FXML
     Button btnCourses;

    @FXML
     Button btnEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(lblTName != null){
            lblTName.setText(teacher.getName());             //setting teacher details.
            lblTMail.setText(teacher.getEmail());
            lblTPhone.setText(teacher.getContactNo());
        }else{
            newName.setText(teacher.getName());                //if current stage is of edit than setting old details.
            newMail.setText(teacher.getEmail());
            newNo.setText(teacher.getContactNo());
        }
    }

    @FXML
    void logout(ActionEvent event) {
        String path = "../login/login.fxml";
        LoginController.isSignUp = false;
        removeCredentials();                //removing credentials.
        changeStage(path,"Login",600,600);
    }

    @FXML
    void coursesClicked(ActionEvent event) {
        System.out.println("courses");
    }

    @FXML
    void examsClicked(ActionEvent event) {          //opening window which shows all the exam scheduled by teacher.
        String path = "../exams/scheduled/scheduledExam.fxml";
        ScheduledExam.fromTeacher = true;
        changeStage(path,"Scheduled Exams",800,600);
    }

    @FXML
    void teamsClicked(ActionEvent event) {       //opens selected team window.
        changeStage("../teams/teacher/teacherTeams.fxml","Teams",500,500);
    }

    public void gotoCreateTeam(ActionEvent actionEvent) {      //opens a new window using which teacher can create a new team.
        changeStage("../teams/teacher/createTeam.fxml","Create Team",500,500);
    }

    public void gotoConductExam(ActionEvent actionEvent) {     //opens a new window using which teacher can create a new exam.
        changeStage("../exams/teacher/createExam.fxml","Create Exam",800,800);
    }

    public void gotoAddStudent(ActionEvent actionEvent) {        //opens a new window using which teacher can add a new student in team.
        changeStage("../teams/teacher/addStudent.fxml","Add Student",700,500);
    }

    ////opens a new window using which teacher can edit details.
    public void editDetails(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("edit.fxml"));
            stage.setTitle("Edit Details");
            stage.setScene(new Scene(parent,500,500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //functions which create new stage and show it.
    public void changeStage(String path,String title,int width,int height){
        try{
            Stage stage;
            if(title.equals("Edit Details") || title.equals("Create Team"))
                stage = new Stage();
            else
                stage = (Stage) lblTName.getScene().getWindow();
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   // Edit details
    @FXML
    TextField newName;

    @FXML
     TextField newMail;

    @FXML
     TextField newNo;

    @FXML
     PasswordField newPass;

    @FXML
     Button btnEditSubmit;
    @FXML
    void EditSubmitted(ActionEvent event) {
        if(newMail.getText().equals("") || newName.getText().equals("") || newNo.getText().equals("") || newPass.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);         //checking that all fields are filled or not.
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("All fields are mandatory.");
            alert.showAndWait();
        }else{
            Teacher teacher1 = new Teacher();
            teacher1.setTeacherId(teacher.getTeacherId());
            teacher1.setName(newName.getText());
            teacher1.setEmail(newMail.getText());
            teacher1.setContactNo(newNo.getText());
            teacher1.setPassword(getHash(newPass.getText()));

            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(server.updateTeacher(teacher1)){                      //sending update request to the server.
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

    //encode text using md5 algorithm and return encoded string.
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
