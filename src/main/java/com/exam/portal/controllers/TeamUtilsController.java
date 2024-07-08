package com.exam.portal.controllers;

import com.exam.portal.entities.*;

import com.exam.portal.interfaces.TeamUtils;
import com.exam.portal.services.TeamUtilsService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/exam-portal/team-utilities")
public class TeamUtilsController {
    private static final String SUCCESSFUL = "SUCCESSFUL";
    private static final String FAILED = "FAILED";

    private final TeamUtils teamUtils;

    public TeamUtilsController(){
        teamUtils = new TeamUtilsService();
    }

    @RequestMapping("/create-team")
    public String createTeam(@RequestBody Team team){
        if(teamUtils.createTeam(team))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/add-student")
    public String addStudent(@RequestBody BelongTo belongTo){
        if(teamUtils.addStudent(belongTo))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/get/all/student/id={Id}")
    public ArrayList<Team> getStudentTeams(@PathVariable String Id){
        return teamUtils.findStudentTeamsById(Id);
    }

    @RequestMapping("/get/all/teacher/id={Id}")
    public ArrayList<Team> getTeacherTeams(@PathVariable String Id){
        return teamUtils.findTeacherTeamsById(Id);
    }

    @RequestMapping("/teacher/make-admin/team-id={teamId}&teacher-id={teacherId}")
    public String makeAdmin(@PathVariable String teacherId, @PathVariable String teamId){
        if(teamUtils.makeAdmin(teamId,teacherId))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/get/id={Id}")
    public Team findTeamById(@PathVariable String Id){
        Id = "Team#"+Id;
        return teamUtils.findTeamById(Id);
    }

    @RequestMapping("/student/join/{teamId}/{studentId}")
    public String joinWithTeamId(@PathVariable String studentId, @PathVariable String teamId){
        teamId = "Team#"+teamId;
        if(teamUtils.joinWithTeamId(teamId,studentId))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/send/message")
    public String addMessage(@RequestBody Message message){
        if(teamUtils.addMessage(message))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/get/all/messages/id={teamId}")
    public ArrayList<Message> getTeamMessages(@PathVariable String teamId){
        teamId = "Team#"+teamId;
        return teamUtils.getTeamMessages(teamId);
    }

    @RequestMapping("/get/student/all/id={teamId}")
    public ArrayList<Student> getStudents(@PathVariable String teamId){
        teamId = "Team#"+teamId;
        return teamUtils.getStudentsByTeamId(teamId);
    }

    @RequestMapping("/get/update")
    public TeamUpdate checkTeamUpdate(@RequestBody TeamUpdate update){
        return teamUtils.checkTeamUpdate(update);
    }

    @RequestMapping("/get/update/message")
    public MessageUpdate checkMessageUpdate(@RequestBody MessageUpdate update){
        return teamUtils.checkMessageUpdate(update);
    }
}
