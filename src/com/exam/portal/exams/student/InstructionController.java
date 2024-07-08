package com.exam.portal.exams.student;

import com.exam.portal.models.Exam;
import com.exam.portal.proctoring.ProcessesDetails;
import com.exam.portal.proctoring.WebcamRecorder;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class InstructionController implements Initializable {
    private static final String ACTIVE = "active";
    private static final String ARCHIVED = "archived";

    @FXML
    Label testTitle;
    @FXML
    Label duration;
    @FXML
    Label questionCount;
    @FXML
    Label maxScore;
    @FXML
    Label timer;
    @FXML
    Label timerStatus;
    @FXML
    Label webcamStatus;
    @FXML
    JFXButton startBtn;

    public static String status;
    public static Exam exam;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setWebcamStatus();
        if(status.equals(ACTIVE)){    //setting start button and timer.
            startBtn.setDisable(false);
            examClosedInTimer();
        }
        else{
            startBtn.setDisable(true);
            examStartInTimer();
        }
        testTitle.setText(exam.getTitle());
        duration.setText(exam.getDuration() + " Minutes");
        questionCount.setText(exam.getQuestionCount()+"");
        maxScore.setText(exam.getMaxScore()+"");
    }

    //if exam is upcoming then calculating how much time remains to start.
    private void examStartInTimer(){
        new Thread(()->{
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = examDate.getTime()-System.currentTimeMillis();
                int hr = (int) (diff/(60*60*1000));                    //hours remaining.
                int mm = (int) ((diff%(60*60*1000))/(60*1000));        //minutes remaining.
                int sec = (int) ((diff%(60*1000))/1000);               //seconds remaining.
                Platform.runLater(()->{
                    timerStatus.setText("Starting In");
                });
                while(hr>0 || mm>0 || sec>0){
                    if(mm==0 && hr>0){  //updating minutes.
                        hr--;
                        mm+=60;
                    }
                    if(sec==0 && mm>0){   //updating seconds.
                        sec+=60;
                        mm--;
                    }

                    sec--;
                    int finalHr = hr;
                    int finalMm = mm;
                    int finalSec = sec;
                    Platform.runLater(()->{
                        timer.setText(finalHr +":"+ finalMm +":"+ finalSec);
                    });
                    Thread.sleep(1000);
                }
                Platform.runLater(this::setWebcamStatus);
                examClosedInTimer();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    //if exam is running then calculating how much time remains in closing of exam.
    public void examClosedInTimer(){
        new Thread(()->{
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = examDate.getTime()+((Long.parseLong(exam.getDuration()))*60*1000)-System.currentTimeMillis();
                int hr = (int) (diff/(60*60*1000));                    //hours remaining.
                int mm = (int) ((diff%(60*60*1000))/(60*1000));        //minutes remaining.
                int sec = (int) ((diff%(60*1000))/1000);               //seconds remaining.
                Platform.runLater(()->{
                    timerStatus.setText("Closed In");
                });
                while(hr>0 || mm>0 || sec>0){
                    if(mm==0 && hr>0){  //updating minutes.
                        mm+=60;
                        hr--;
                    }
                    if(sec==0 && mm>0){   //updating seconds.
                        sec+=60;
                        mm--;
                    }

                    sec--;
                    int finalHr = hr;
                    int finalMm = mm;
                    int finalSec = sec;
                    Platform.runLater(()->{
                        timer.setText(finalHr +":"+ finalMm +":"+ finalSec);
                    });
                    Thread.sleep(1000);
                }
                Platform.runLater(()->{
                    timerStatus.setText("Closed");
                    startBtn.setDisable(true);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    //setting webcam status.
    private void setWebcamStatus(){
        WebcamRecorder recorder = new WebcamRecorder();
        if(recorder.isWebcamAvailable()){
            webcamStatus.setText("OK");
            startBtn.setDisable(false);
        }
        else{
            webcamStatus.setText("Not Working");
            startBtn.setDisable(true);
        }
    }

    @FXML
    public void startTest(ActionEvent event) {
        try {
            //check application count before starting test.
            if(!checkApplicationCount()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("You need to close all other applications running \nbefore starting test.");
                alert.showAndWait();
                return;
            }

            Stage stage = (Stage) startBtn.getScene().getWindow();
            QuestionPaper.exam = exam;
            QuestionPaper.stage = stage;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionPaper.fxml")));
            stage.setTitle("Exam Portal");
            stage.setScene(new Scene(root,600,600));
            //setting screen to full screen
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkApplicationCount(){
        ProcessesDetails processesDetails = new ProcessesDetails();
        return processesDetails.getProcessesCount() == 1;
    }

    @FXML
    public void back(ActionEvent event){
        try {
            Stage stage = (Stage) timerStatus.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scheduled/scheduledExam.fxml")));
            stage.setScene(new Scene(root,600,600));
            stage.setTitle("Exam Portal");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
