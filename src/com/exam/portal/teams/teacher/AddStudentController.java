package com.exam.portal.teams.teacher;

import com.exam.portal.models.BelongTo;
import com.exam.portal.models.Student;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable {
    @FXML
    JFXComboBox<String> selectedTeam;
    @FXML
    JFXTextField searchText;
    @FXML
    JFXListView<Label> searchResultList;

    private ArrayList<Team> teams;                //for storing teams.
    private ObservableList<String> observableList;
    private ArrayList<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableList = FXCollections.observableArrayList();
        fetchTeams();
    }

    //adding all teams in combobox.
    private void setList() {
        selectedTeam.getItems().addAll(observableList);
    }

    //fetching all teams from server and showing in combobox.
    private void fetchTeams() {
        Server server = ServerHandler.getInstance();
        Platform.runLater(()->{    //making request asynchronous.
            teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
            if(teams==null)
                teams = new ArrayList<>();
            for(Team team:teams){
                observableList.add(team.getName());
            }
            setList();
        });
    }

    //searching for student using text entered by teacher.
    @FXML
    public void search(ActionEvent actionEvent){
        Platform.runLater(() -> {
            if(searchText.getText().equals("")){     //checking that teacher has entered any text or not.
                showAlert("search item is empty.","Warning", Alert.AlertType.WARNING);
            }else{
                searchResultList.getItems().clear();
                Server server = ServerHandler.getInstance();
                students = server.searchStudent(searchText.getText());    //sending search request to server.
                if(students==null)
                    students = new ArrayList<>();
                for(Student student:students){
                    Label label = new Label();               //creating a custom label and adding it to result list.
                    label.setText(student.getName());
                    Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
                    label.setFont(font);
                    label.setTextFill(Color.BLACK);
                    searchResultList.getItems().add(label);
                }
            }
        });
    }

    //send student and teacher details to server for adding student in team.
    @FXML
    public void addStudent(ActionEvent actionEvent){
        if(searchResultList.getSelectionModel().isEmpty() || selectedTeam.getSelectionModel().isEmpty()){
            showAlert("Please select student from list or team from team list.","Warning", Alert.AlertType.WARNING);
        }else{
            Server server = ServerHandler.getInstance();
            Student student = students.get(searchResultList.getSelectionModel().getSelectedIndex());
            Team team = teams.get(selectedTeam.getSelectionModel().getSelectedIndex());
            BelongTo belongTo = new BelongTo();
            belongTo.setStudentId(student.getStudentId());
            belongTo.setTeamId(team.getTeamId());
            //getting date
            LocalDate date = LocalDate.now();
            String sDate = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
            belongTo.setDate(sDate);

            Platform.runLater(()->{
                if(server.addStudent(belongTo)){    //sending add student request to server.
                    showAlert("Student successfully added to team","Successful", Alert.AlertType.INFORMATION);
                    back(actionEvent);
                }else{
                    String message = "Not able to add student in this team.\nMay be student already exist in this team.";
                    showAlert(message,"Warning", Alert.AlertType.WARNING);
                }
            });
        }
    }

    @FXML
    public void back(ActionEvent event){
        try {
            Stage stage = (Stage) searchText.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../teacher/teacherDashboard.fxml")));
            stage.setTitle("Exam Portal");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //function for showing alerts.
    public void showAlert(String message,String title, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
