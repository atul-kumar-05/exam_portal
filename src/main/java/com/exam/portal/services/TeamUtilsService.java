package com.exam.portal.services;

import com.exam.portal.database.TeamUtilsDb;
import com.exam.portal.entities.*;
import com.exam.portal.interfaces.TeamUtils;

import java.util.ArrayList;

public class TeamUtilsService implements TeamUtils {
    private final TeamUtilsDb teamUtilsDb;

    public TeamUtilsService(){
        teamUtilsDb = new TeamUtilsDb();
    }

    @Override
    public boolean createTeam(Team team) {
        return teamUtilsDb.createTeam(team);
    }

    @Override
    public boolean addStudent(BelongTo belongTo) {
        return teamUtilsDb.addStudent(belongTo);
    }

    @Override
    public ArrayList<Team> findStudentTeamsById(String Id) {
        return teamUtilsDb.findTeamsById(Id,"BelongTo","Student_Id");
    }

    @Override
    public ArrayList<Team> findTeacherTeamsById(String Id) {
        return teamUtilsDb.findTeamsById(Id,"Admin_Of","Admin_Id");
    }

    @Override
    public boolean makeAdmin(String teamId, String teacherId) {
        return teamUtilsDb.makeAdminOf(teacherId,teamId);
    }

    @Override
    public Team findTeamById(String id) {
        return teamUtilsDb.findTeamById(id);
    }

    @Override
    public boolean addMessage(Message message){return teamUtilsDb.addMessage(message);}

    @Override
    public ArrayList<Message> getTeamMessages(String teamId){return teamUtilsDb.getTeamMessage(teamId);}

    @Override
    public boolean joinWithTeamId(String teamId,String studentId){
        return teamUtilsDb.joinTeamWithId(teamId,studentId);
    }

    @Override
    public ArrayList<Student> getStudentsByTeamId(String Id){
        return teamUtilsDb.getStudents(Id);
    }

    @Override
    public TeamUpdate checkTeamUpdate(TeamUpdate update){
        return teamUtilsDb.checkTeamUpdate(update);
    }

    @Override
    public MessageUpdate checkMessageUpdate(MessageUpdate update){
        return teamUtilsDb.getMessageUpdate(update);
    }
}
