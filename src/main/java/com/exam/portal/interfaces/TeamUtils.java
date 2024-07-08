package com.exam.portal.interfaces;

import com.exam.portal.entities.*;

import java.util.ArrayList;

public interface TeamUtils {
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
    public ArrayList<Team> findStudentTeamsById(String Id);
    public ArrayList<Team> findTeacherTeamsById(String Id);
    public ArrayList<Student> getStudentsByTeamId(String Id);
    public boolean makeAdmin(String teamId,String teacherId);
    public Team findTeamById(String id);
    public boolean joinWithTeamId(String teamId,String studentId);
    public TeamUpdate checkTeamUpdate(TeamUpdate update);
    public boolean addMessage(Message message);
    public ArrayList<Message> getTeamMessages(String teamId);
    public MessageUpdate checkMessageUpdate(MessageUpdate update);
}
