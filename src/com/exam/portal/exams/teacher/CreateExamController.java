package com.exam.portal.exams.teacher;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Question;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CreateExamController implements Initializable {
    @FXML
    JFXComboBox<String> selectedTeam;
    @FXML
    TextField tfTitle;
    @FXML
    TextField tfDuration;
    @FXML
    JFXDatePicker datePicker;
    @FXML
    JFXTimePicker timePicker;
    @FXML
    JFXListView<Label> questionList;

    private String date,time;

    public ObservableList<String> observableList;
    public ArrayList<Team> teams;

    private static Exam exam;

    private Stack<Question> stack;

    private volatile boolean isThreadRunning;
    private volatile int questionCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timePicker.set24HourView(true);
        observableList = FXCollections.observableArrayList();
        setTeams();
        exam = new Exam();
        exam.setExamId(generateRandomId());
        stack = new Stack<>();
        isThreadRunning = false;
        questionCount = 0;
        date = "";
        time = "";
    }

    //adding teams in combobox.
    public void setObservableList(){
        for(Team team:teams){
            observableList.add(team.getName());
        }
        selectedTeam.getItems().addAll(observableList);
    }

    //setting all teams in combobox.
    public void setTeams(){
        Server server = ServerHandler.getInstance();
        Platform.runLater(()->{
            teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
            setObservableList();
        });
    }

    @FXML
    public void createExam(ActionEvent event) {
        boolean flag;
        setDateAndTime();   //initializing data and time.
        if(!checkCombobox())  //checking for team selection.
            return;
        //checking that all fields are filled or not.
        if(tfTitle.getText().equals("") || tfDuration.getText().equals("") || date.equals("") || time.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Please select a team.");
            alert.showAndWait();
            flag = false;
        }else{
            flag = true;    //completing exam object.
            exam.setTitle(tfTitle.getText());
            exam.setExamDate(date);
            exam.setTime(time);
            exam.setDuration(tfDuration.getText());
            exam.setTeamId(teams.get(selectedTeam.getSelectionModel().getSelectedIndex()).getTeamId());
            exam.setCreatorId(TeacherController.teacher.getTeacherId());
            exam.setQuestionCount(exam.getCurrentQuestionCount());
            exam.setMaxScore(exam.calculateMaxScore());
        }

        if(flag){
            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(server.createExam(exam)){     //sending create exam request.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Successful");
                    alert.setContentText("Exam successfully created.");
                    alert.showAndWait();
                    stopThread();
                    back(event);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Failed");
                    alert.setContentText("Exam creation failed");
                    alert.showAndWait();
                }
            });
        }
    }

    //checking that teacher select any team or not.
    public boolean checkCombobox(){
        if(selectedTeam.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Please select a team.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    //creating new stage on which teacher can create question and add it in test.
    public void addQuestion(ActionEvent actionEvent) {
        //for running thread
        if(!isThreadRunning){
            runThread();
        }
        //for stopping running thread.
        if(isThreadRunning){
            Stage stage1 = (Stage) tfTitle.getScene().getWindow();
            stage1.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    stopThread();
                }   //stopping thread before closing stage.
            });
        }

        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addQuestion.fxml")));
            stage.setTitle("Add Question");
            stage.setScene(new Scene(parent,800,800));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //deleting last added question and push it to stack for restoring.
    public void undo(ActionEvent actionEvent) {
        Question question = exam.undoQuestion();
        if(question==null)
            return;
        questionCount--;  //decrementing question count such that thread can check for next question.
        questionList.getItems().remove(questionCount);
        stack.push(question);
    }

    //restoring undo question.
    public void redo(ActionEvent actionEvent) {
        if(!stack.isEmpty()){
            Question question = stack.pop();
            exam.addQuestion(question);
        }
    }

    //method called by Question Controller for adding question.
    public static void addQuestion(Question question){
        question.setExamId(exam.getExamId());
        exam.addQuestion(question);
    }

    //running a thread which check every second that any question is added or not,if added that it add that question in questionList.
    public void runThread(){
        isThreadRunning = true;
        new Thread(()->{
            while (isThreadRunning){
                if(exam.getCurrentQuestionCount() != questionCount){   //checking that question is added or not.
                    Question question = exam.getLastQuestion();
                    Platform.runLater(()->{
                        Label label = new Label();
                        label.setText(question.getQuestion());
                        questionList.getItems().add(label);
                    });
                    questionCount = exam.getCurrentQuestionCount();
                }else{
                    try {
                        Thread.sleep(1000);          //sleep thread for 1 second.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //stopping the infinitely running thread.
    public void stopThread(){
        isThreadRunning = false;  //making variable false breaks the infinite loop in thread.
    }

    //generating a random id for exam.
    public String generateRandomId(){
        String id = UUID.randomUUID().toString();
        int start = Math.abs((Math.abs(new Random().nextInt(id.length()))%id.length()-15));
        id = "Exam#"+id.substring(start,start+6);
        String Id = "Exam#";
        for(int i=5;i<id.length();i++){
            char c = id.charAt(i);
            if(c>='0' && c<='9'){
                Id += c;
                continue;
            }
            char nc = (char) ((Math.abs(c-'a')%26)+'a');
            Id += (nc);
        }
        return Id;
    }

    //initialize data and time variable
    public void setDateAndTime(){
        LocalDate localDate = datePicker.getValue();
        if(localDate==null)
            return;
        date = localDate.getYear()+"-"+localDate.getMonthValue()+"-"+localDate.getDayOfMonth();
        LocalTime localTime = timePicker.getValue();
        if(localTime==null)
            return;
        time = localTime.getHour()+":"+localTime.getMinute()+":00";
    }

    @FXML
    public void back(ActionEvent event){
        try {
            Stage stage = (Stage) tfTitle.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../teacher/teacherDashboard.fxml")));
            stage.setTitle("Exam Portal");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
