package com.exam.portal.teams.teacher;

import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class TeacherTeamsController implements Initializable {
    @FXML
    JFXListView<Label> teamList;

    @FXML
    Label lblCandidateName;

    @FXML
    Label lblCandidateMail;

    @FXML
    Button btnManageTeams;

    @FXML
    Button btnCreateNewTeam;

    @FXML
    Button btnBack;

    private ArrayList<Team> teams;

    int r,g,b;
    double op;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblCandidateMail.setText(TeacherController.teacher.getEmail());    //setting student or teacher details.
        lblCandidateName.setText(TeacherController.teacher.getName());
        teamList.setExpanded(true);
        fetchTeams();                    //fetching all teams in which student is added or teacher is a admin accordingly.

        teamList.setOnMouseClicked(this::teamListItemClicked);  //adding click listener to teamList.
    }

    //fetching all teams associated with teacher from server
    private void fetchTeams(){
        Server server = ServerHandler.getInstance();
        Platform.runLater(()->{
            teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
            if(teams==null)
                teams = new ArrayList<>();
            setListView();
        });
    }

    //setting all fetched teams in the list view.
    private void setListView(){
        for(Team team:teams){
            Label label = new Label();         //creating custom label and adding in listview.
            label.setText(team.getName());
            Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
            label.setFont(font);
            label.setTextFill(Color.WHITE);
            teamList.getItems().add(label);
        }
    }

    //function which invoked when user click on listview.
    private void teamListItemClicked(MouseEvent mouseEvent) {
        String path = "selectTeamTeacher.fxml";
        Team team = teams.get(teamList.getSelectionModel().getSelectedIndex());
        SelectTeamTeacherController.team = team;
        changeStage(path,team.getName(),800,800);      //changing current stage to team stage on which user clicked.
    }

    //showing window on which user can create new team.
    public void createNewTeam(ActionEvent actionEvent) {
        String path = "createTeam.fxml";
        changeStage(path,"Create Team",400,500);
    }

    //change stage back to dashboard.
    public void goBack(ActionEvent actionEvent) {
        String path = "../../teacher/teacherDashboard.fxml";
        changeStage(path,"Dashboard",700,500);
    }

    //function which change current stage to new stage.
    public void changeStage(String path,String title,int width,int height){
        try {
            Stage stage;
            if(title.equals("Create Team"))
                stage = new Stage();
            else{
                stage = (Stage) btnBack.getScene().getWindow();
                SelectTeamTeacherController.stage = stage;
            }
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //function which generates random value of r,g,b and opacity for color.
    private void generateRandomColor(){
        Random random = new Random();
        int c = random.nextInt();
        r = c&255;
        g = (c>>>8)&255;
        b = (c>>>16)&255;
        op = (c>>>24)/255.0;
    }
}
