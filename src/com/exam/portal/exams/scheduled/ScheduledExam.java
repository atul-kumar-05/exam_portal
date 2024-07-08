package com.exam.portal.exams.scheduled;

import com.exam.portal.exams.student.InstructionController;
import com.exam.portal.models.Exam;
import com.exam.portal.models.ExamUpdate;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.exam.portal.teacher.TeacherController;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScheduledExam implements Initializable {
    public static final String QUESTION_PAPER_PATH = "../student/questionPaper.fxml";
    public static final String SUBMISSION_PATH = "../student/submissions.fxml";
    public static final String INSTRUCTIONS_PATH = "../student/instructions.fxml";
    public static final String VIEW_SUBMISSIONS_PATH = "../teacher/viewSubmissions.fxml";

    @FXML
    VBox vBox;
    @FXML
    JFXButton activeBtn;
    @FXML
    JFXButton upcomingBtn;

    //variable used for determining that it is student or teacher.
    public static boolean fromTeacher;

    private ArrayList<Exam> exams;
    private volatile int examsCount;
    private volatile boolean runThread;

    public static Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        examsCount = 0;
        runThread = false;
        fetchExams();
    }

    //fetching only details of exams (without questions).
    private void fetchExams(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            if(fromTeacher)
                exams = server.getExamScheduledBy(TeacherController.teacher.getTeacherId());
            else
                exams = server.getExamScheduledFor(StudentController.student.getStudentId());
            if(exams == null)
                exams = new ArrayList<>();
            activeBtn.fire();          //setting active exams initially.
            examsCount = exams.size();
            if(!fromTeacher)
                checkExamUpdate();
        });
    }

    //displaying all exams.
    private void addExamsToVBox(ArrayList<Exam> examArrayList,String path){
        ExamItem.stage = (Stage)vBox.getScene().getWindow();
        Server server = ServerHandler.getInstance();
        try{
           for(Exam exam:examArrayList) {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("examItem.fxml"));
               Node node = loader.load();
               ExamItem examItem = loader.getController();
               examItem.setTitle(exam.getTitle());
               examItem.setDate(exam.getExamDate());
               examItem.setTime(exam.getTime());
               examItem.setExam(exam);
               Team team = server.getTeamById(exam.getTeamId());
               examItem.setTeamName(team.getName());
               examItem.setTeam(team);
               vBox.getChildren().add(node);
               examItem.addEventListener(path);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all the exams.
    public void activeExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> activeExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-examDate.getTime();
                long milli = Long.parseLong(exam.getDuration())*60*1000;      //duration from minutes to milliseconds.
                if(diff>=0 && diff<milli)
                    activeExam.add(exam);
            }
            if(fromTeacher)
                addExamsToVBox(activeExam,QUESTION_PAPER_PATH);
            else
                addExamsToVBox(activeExam,INSTRUCTIONS_PATH);
            InstructionController.status = "active";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all exams which has not been finished yet.
    public void upcomingExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> upcomingExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-examDate.getTime();
                if(diff<0)
                    upcomingExam.add(exam);
            }
            if(fromTeacher)
                addExamsToVBox(upcomingExam,QUESTION_PAPER_PATH);
            else
                addExamsToVBox(upcomingExam,INSTRUCTIONS_PATH);
            InstructionController.status = "upcoming";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all finished exams by comparing current time and exam time.
    public void archivedExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> archivedExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-(examDate.getTime()+ (long) (Integer.parseInt(exam.getDuration())) *60*1000);
                if(diff>0)
                    archivedExam.add(exam);
            }

            if(fromTeacher)
                addExamsToVBox(archivedExam,VIEW_SUBMISSIONS_PATH);
            else
                addExamsToVBox(archivedExam,SUBMISSION_PATH);
            InstructionController.status = "archived";
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent event){
        try {
            Stage stage = (Stage) vBox.getScene().getWindow();
            Parent root;
            if(fromTeacher)
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../teacher/teacherDashboard.fxml")));
            else
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../student/studentDashboard.fxml")));
            stage.setScene(new Scene(root,600,600));
            stage.setTitle("Exam Portal");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //checking that whether any exam scheduled in any team or not.
    private void checkExamUpdate(){
        runThread = true;
        new Thread(()->{
            while(runThread){
                try {
                    ExamUpdate update = new ExamUpdate(StudentController.student.getStudentId(),examsCount);
                    update.setType("create");
                    Server server = ServerHandler.getInstance();
                    update = server.checkExamUpdate(update);
                    if(update!=null && update.isUpdate()){
                        ExamUpdate finalUpdate = update;
                        Platform.runLater(()->{
                            exams.addAll(finalUpdate.getExams());
                            upcomingBtn.fire();
                        });
                    }

                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        stage.setOnCloseRequest(windowEvent -> runThread = false);  //stopping thread after stage gets closed.
    }
}
