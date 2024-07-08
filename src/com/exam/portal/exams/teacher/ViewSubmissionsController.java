package com.exam.portal.exams.teacher;

import com.exam.portal.models.Exam;
import com.exam.portal.models.StudentResponse;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewSubmissionsController implements Initializable {
    public static Exam exam;

    @FXML
    TableView<StudentResponse> responseTable;
    @FXML
    TableColumn<StudentResponse, String> nameColumn;
    @FXML
    TableColumn<StudentResponse, String> emailColumn;
    @FXML
    TableColumn<StudentResponse, String> contactColumn;
    @FXML
    TableColumn<StudentResponse, String> marksColumn;
    @FXML
    Label title;
    @FXML
    TextField searchText;
    @FXML
    Button backBtn;

    private ArrayList<StudentResponse> list;
    private ObservableList<StudentResponse> responseList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        title.setText(exam.getTitle());
        responseList = FXCollections.observableArrayList();
        setListenerToTable();
        fetchSubmissionDetails();
    }

    //fetching students details and their submissions.
    public  void fetchSubmissionDetails(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            list=server.fetchExamDetails(exam.getExamId());
            if(list==null)
                list=new ArrayList<>();
            responseList.addAll(list);
            responseTable.setItems(responseList);
            searchStudent();
        });
    }

    //filtering students by entered text.
    private void searchStudent(){
        searchText.textProperty().addListener((observableValue,s,t1)->{
            ArrayList<StudentResponse>newList=new ArrayList<>();
            for(StudentResponse studentResponse:list){     //checking for entered text that whether it's part of name,email or contact no.
                if(studentResponse.getName().contains(t1)|| studentResponse.getEmail().contains(t1) || studentResponse.getContact().contains(t1))
                    newList.add(studentResponse);
            }
            responseList.clear();
            responseList.addAll(newList);
            responseTable.setItems(responseList);
        });
    }

    private void setListenerToTable(){
        responseTable.setOnMouseClicked(mouseEvent -> {
            StudentResponse studentResponse = responseTable.getSelectionModel().getSelectedItem();
            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                StudentController.student = server.getStudent(studentResponse.getEmail());
                try {
                    Stage stage = (Stage) searchText.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../student/submissions.fxml")));
                    stage.setScene(new Scene(root,600,600));
                    stage.setTitle("Exam Portal");
                    stage.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    @FXML
    void back(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scheduled/scheduledExam.fxml")));
            stage.setTitle("Scheduled Exams");
            stage.setScene(new Scene(root, 700, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
