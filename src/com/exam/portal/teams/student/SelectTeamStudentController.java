package com.exam.portal.teams.student;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.Message;
import com.exam.portal.models.MessageUpdate;
import com.exam.portal.models.Team;
import com.exam.portal.teams.ShowAllStudents;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;


import static com.exam.portal.student.StudentController.student;

public class SelectTeamStudentController implements Initializable {
    public static Team currentTeam;
    public static Stage stage;

    @FXML
    private Label lblTeamName;
    @FXML
    private ListView<Label> listViewofMassages;

    @FXML
    Button btnShowStudents;

    @FXML
    Button btnScheduledExam;

    @FXML
    Button btnGoBack;

    @FXML
    TextField tfMessage;

    @FXML
    Button btnSend;

    private ArrayList<Message> returnedMessages;
    private volatile int messageCount;
    private volatile boolean runThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTeamName.setText(currentTeam.getName());
        messageCount = 0;
        runThread = false;
        fetchMessages();
    }

    private void loadCurrentMessages(){
        runThread = true;
        new Thread(() -> {
            while(runThread) {
                try{
                    MessageUpdate update = new MessageUpdate(currentTeam.getTeamId(),messageCount);
                    Server server = ServerHandler.getInstance();
                    update = server.checkMessageUpdate(update);
                    if(update.isUpdate()){
                        messageCount = update.getPrevCount();
                        MessageUpdate finalUpdate = update;
                        returnedMessages.addAll(finalUpdate.getMessages());
                        Platform.runLater(()->{
                            setMessages(finalUpdate.getMessages());
                        });
                    }
                    Thread.sleep(5000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        //for stopping thread after closing messages stage.
        stage.setOnCloseRequest(windowEvent -> {
            runThread = false;
        });
    }

    //fetching messages from server.
    private void fetchMessages(){
        Platform.runLater(()->{
            Server server =  ServerHandler.getInstance();
            returnedMessages = server.getMassages(currentTeam.getTeamId());
            if(returnedMessages ==null)
                returnedMessages =new ArrayList<>();
            setMessages(returnedMessages);
            messageCount = returnedMessages.size();
            loadCurrentMessages();
        });
    }

    //adding all fetched messages in listview.
    private  void setMessages(ArrayList<Message> messages){
        if(messages==null)return;
        for( Message message:messages){
            Label label=new Label();
            String msg = message.getSenderName() + " : " + message.getMessage();
            label.setText(msg);
            label.autosize();
            label.setMaxWidth(500);
           // label.setStyle("-fx-background-color: DarkSlateGrey");
          //  label.setStyle("-fx-border-color: black");
            listViewofMassages.getItems().add(label);

        }
    }

    @FXML
    void send(ActionEvent event) {
        if (tfMessage.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("warning");
            alert.setContentText("massage can't empty");
            alert.showAndWait();
        } else {
            Message message = new Message();
            message.setMessageId(generateId());
            message.setTeamId(currentTeam.getTeamId());
            message.setSenderId(student.getStudentId());
            message.setSenderName(student.getName());
            message.setMessage(tfMessage.getText());
            message.setDate(new Timestamp(System.currentTimeMillis()));

            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(!server.sendMassage(message)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("Warning");
                    alert.setContentText("unable to send message.");
                    alert.showAndWait();
                }else
                    tfMessage.setText("");
            });
        }
    }
    @FXML
    void showAllStudents(ActionEvent event) {
        ShowAllStudents.fromTeacher = false;
        String path = "../showAllStudents.fxml";
        changeStage(path);
    }

    private String generateId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

    @FXML
    void checkExam(ActionEvent event) {
        String path = "../../exams/scheduled/scheduledExam.fxml";
        ScheduledExam.fromTeacher = false;
        changeStage(path);
    }

    @FXML
    void goBack(ActionEvent event) {
        String path = "studentTeams.fxml";
        changeStage(path);
    }

    private void changeStage(String path){
        try {
            runThread = false;
            Stage stage = (Stage) btnSend.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(path));
            stage.setScene(new Scene(root,600,600));
            stage.setTitle("Exam Portal");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



