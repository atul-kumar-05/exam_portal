package com.exam.portal.exams.scheduled;

import com.exam.portal.exams.student.InstructionController;
import com.exam.portal.exams.student.QuestionPaper;
import com.exam.portal.exams.student.SubmissionsController;
import com.exam.portal.exams.teacher.ViewSubmissionsController;
import com.exam.portal.models.Exam;
import com.exam.portal.models.Team;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ExamItem {
    @FXML
    VBox vBox;
    @FXML
    Label title;
    @FXML
    Label date;
    @FXML
    Label time;
    @FXML
    Label teamName;

    private Exam exam;
    private Team team;
    public static Stage stage;

    public void setTitle(String text){
        title.setText(text);
    }

    public void setDate(String text){
        date.setText(text);
    }

    public void setTime(String text){
        time.setText(text);
    }

    public void setTeamName(String text){
        teamName.setText(text);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setExamId(String id){
        this.exam.setExamId(id);
    }

    //take to the question paper when we clicked on exam.
    public void addEventListener(String path){
        vBox.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    QuestionPaper.exam = exam;
                    InstructionController.exam = exam;
                    SubmissionsController.exam = exam;
                    ViewSubmissionsController.exam = exam;
                    QuestionPaper.stage = stage;
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
                    Scene scene = new Scene(root, 600, 600);
                    stage.setTitle("Exam Portal");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
