package com.exam.portal.teams;

import com.exam.portal.models.Student;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teams.student.SelectTeamStudentController;
import com.exam.portal.teams.teacher.SelectTeamTeacherController;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllStudents implements Initializable {
    public static boolean fromTeacher;
    @FXML
    TableView<Student> table;
    @FXML
    TableColumn<Student, String> name;
    @FXML
    TableColumn<Student, String> contactNo;
    @FXML
    TableColumn<Student, String> email;
    @FXML
    JFXTextField searchText;

    private ArrayList<Student> students;

    private final ObservableList<Student> list= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        fetchStudents();
    }

    //fetching all students included in current team.
    private void fetchStudents(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            if(fromTeacher)
                students = server.getStudentsByTeamId(SelectTeamTeacherController.team.getTeamId());
            else
                students = server.getStudentsByTeamId(SelectTeamStudentController.currentTeam.getTeamId());
            if(students == null)
                students = new ArrayList<>();
            list.addAll(students);
            table.setItems(list);
            searchStudent();
        });
    }

    //adding on text change listener on search field for changing table.
    private void searchStudent(){
        searchText.textProperty().addListener((observableValue, s, t1) -> {
            ArrayList<Student> newList = new ArrayList<>();
            for(Student student: students){
                if(student.getName().contains(t1) || student.getEmail().contains(t1) || student.getContactNo().contains(t1))
                    newList.add(student);
            }

            list.clear();
            list.addAll(newList);
            table.setItems(list);
        });
    }

    @FXML
    public void goBack(ActionEvent event){
        try{
            Stage stage = (Stage) searchText.getScene().getWindow();
            Parent root;
            if(fromTeacher)
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("teacher/selectTeamTeacher.fxml")));
            else
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("student/selectTeamStudent.fxml")));
            stage.setTitle("Exam Portal");
            stage.setScene(new Scene(root,700,700));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
