package com.exam.portal.exams.teacher;

import com.exam.portal.models.Option;
import com.exam.portal.models.Question;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.Stack;

public class AddQuestionController implements Initializable {
    @FXML
    JFXTextArea questionText;
    @FXML
    JFXListView<Label> options;
    @FXML
    JFXToggleButton mcq,subjective;
    @FXML
    JFXTextField answers;
    @FXML
    JFXTextField point;
    @FXML
    JFXTextField negPoint;
    @FXML
    JFXButton chooseFileBtn;

    private boolean isImage;

    private static Question question;

    private Stack<Option> stack;
    private File file;

    //variable used for running and stopping thread.
    private volatile boolean isThreadRunning;
    //contains option count using which thread check that option is added or not.
    private volatile int optionCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stack = new Stack<>();
        question = new Question();
        file = null;
        isThreadRunning = false;
        optionCount =0;
        isImage = false;
        runThread();
    }

    //optioning another stage on which teacher can add option.
    public void addOption(ActionEvent actionEvent) {
        try{
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("addOption.fxml"));
            stage.setTitle("Add Option");
            stage.setScene(new Scene(parent,650,450));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //deleting last added question.
    public void undoOption(ActionEvent actionEvent) {
        Option option = question.undoOption();
        if(option != null){
            optionCount = optionCount-1;
            stack.push(option);               //pushing option in stack for redo.
            options.getItems().remove(optionCount);
        }
    }

    //restoring the last deleted option.
    public void redoOption(ActionEvent actionEvent) {
        if(!stack.isEmpty()){
            Option option = stack.pop();
            question.addOption(option);
        }
    }

    //adding current question in exam and closing current stage.
    public void Done(ActionEvent actionEvent) {
        boolean flag=true;
        if(point.getText().equals("") || negPoint.getText().equals("") || !(mcq.isSelected() || subjective.isSelected())){
            flag = false;
            showAlert("point and answer filling is mandatory.");
        }else{
            if(isImage){
                if(file==null){
                    flag = false;
                    showAlert("Question image is not selected.");
                }else{
                    flag = true;
                    question.setIsImage(true);
                    question.setQuestion(file.getAbsolutePath());
                    question.setFile(encodeImageToBase64Binary(file));
                }
            }else{
                if(questionText.getText().equals("")){
                    flag = false;
                    showAlert("Question text is not entered.");
                }else{
                    flag = true;
                    question.setIsImage(false);
                    question.setQuestion(questionText.getText());
                }
            }

            if(mcq.isSelected())
                question.setQuestionType(1);   // 1 for mcq.
            else
                question.setQuestionType(2);   //2 for subjective.
        }

        if(flag){
            question.setAnswer(answers.getText());
            question.setPoint(Double.parseDouble(point.getText()));
            question.setNegPoint(Double.parseDouble(negPoint.getText()));
            CreateExamController.addQuestion(question);
            stopThread();
            Stage stage = (Stage) questionText.getScene().getWindow();
            stage.close();
        }
    }

    //it open a file chooser for choosing file for question.
    public void chooseFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        isImage = true;
        file = chooser.showOpenDialog(questionText.getScene().getWindow());
        if(file!=null){
            chooseFileBtn.setText(file.getAbsolutePath());
        }
    }

    //function called by add option controller for adding option.
    public static void addOption(Option option){
        question.addOption(option);
    }

    //showing alert with given message.
    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }

    //thread which update option list.
    private void runThread(){
        isThreadRunning = true;
        new Thread(()->{
            while(isThreadRunning){              //running an infinite loop which keeps check that any option is added or not.
                if(question.getOptionCount() != optionCount){
                    Option option = question.getLastQuestion();
                    Label label = new Label();
                    label.setText(option.getText());
                    Platform.runLater(()->{
                        options.getItems().add(label);
                    });
                    optionCount = question.getOptionCount();
                }else{
                    try {
                        Thread.sleep(1000);         //sleeping thread for 1 second.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //stopping running thread by making isThreadRunning false.
    private void stopThread(){
        isThreadRunning = false;
    }

    //encoding image file in string using base64 encoder.
    private String encodeImageToBase64Binary(File file) {
        try{
            FileInputStream fis = new FileInputStream(file);
            String encodedFile = Base64.getEncoder().encodeToString(fis.readAllBytes());
            return encodedFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
