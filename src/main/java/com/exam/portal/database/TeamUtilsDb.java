package com.exam.portal.database;

import com.exam.portal.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class TeamUtilsDb {
    private final Connection connection;

    public TeamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    //creates a new team in the database and returns true if successfully created otherwise returns false.
    public boolean createTeam(Team team){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Teams values(?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, team.getTeamId());
            preparedStatement.setString(2, team.getName());
            preparedStatement.setString(3, team.getCreatorId());
            preparedStatement.setDate(4,team.getDateCreated());
            preparedStatement.execute();
            return makeAdminOf(team.getCreatorId(),team.getTeamId());    //making creator admin as well.
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //makes teacher with teacherId a admin of team with teamId.
    public boolean makeAdminOf(String teacherId,String teamId){
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO ADMIN_OF VALUES(?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            preparedStatement.setString(2,teacherId);
            preparedStatement.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //adding student in the team , belongTo contains details of student and teams both.
    public boolean addStudent(BelongTo belongTo){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO BelongTo values(?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, belongTo.getStudentId());
            preparedStatement.setString(2, belongTo.getTeamId());
            preparedStatement.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            preparedStatement.execute();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //returns all the teams in which student with Id included or teacher with Id is a admin.
    //relationName is the name of table , for student's team it is belongTo and for teacher's team it is adminOf.
    public ArrayList<Team> findTeamsById(String Id,String relationName,String relationId){
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        ArrayList<Team> teams;
        String query = "SELECT * FROM TEAMS WHERE TEAMS.Team_Id IN (SELECT Team_Id FROM "+relationName+" WHERE "+relationId+"=?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Id);
            rs = preparedStatement.executeQuery();
            teams = new ArrayList<>();
            while(rs.next()){
                Team team = new Team();
                team.setTeamId(rs.getString(1));
                team.setCreatorId(rs.getString(3));
                team.setName(rs.getString(2));
                team.setDateCreated(rs.getDate(4));
                teams.add(team);
            }
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return a team having id same as teamId.
    public Team findTeamById(String teamId){
        PreparedStatement preparedStatement=null;
        try {
            String query = "SELECT * FROM Teams WHERE Team_Id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Team team = new Team();
                team.setTeamId(teamId);
                team.setCreatorId(rs.getString(3));
                team.setName(rs.getString(2));
                team.setDateCreated(rs.getDate(4));

                return team;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // join a team with team id for student
    public boolean joinTeamWithId(String teamId,String studentId){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO BelongTo values(?,?,?) ";
        try{
            Team requiredTeam=findTeamById(teamId);
            if(requiredTeam!=null){
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, studentId);
                preparedStatement.setString(2, teamId);
                preparedStatement.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
                preparedStatement.execute();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
  public boolean addMessage(Message message){
      PreparedStatement preparedStatement=null;
      String query = "INSERT INTO Messages values(?,?,?,?,?,?)";
      try{
          preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, message.getMessageId());
          preparedStatement.setString(2, message.getTeamId());
          preparedStatement.setString(3, message.getSenderId());
          preparedStatement.setString(4,message.getSenderName());
          preparedStatement.setString(5, message.getMessage());
          preparedStatement.setTimestamp(6,new Timestamp(System.currentTimeMillis()));
          preparedStatement.execute();
          return true;
      }catch(Exception e){
          e.printStackTrace();
      }

      return false;
  }

  public  ArrayList<Message> getTeamMessage(String teamId){
        PreparedStatement preparedStatement=null;
        ArrayList<Message> messages=new ArrayList<>();
        try{
            String query="SELECT * FROM Messages WHERE TEAM_ID=? ORDER BY Time";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message();
                message.setMessageId(rs.getString(1));
                message.setTeamId(teamId);
                message.setSenderId(rs.getString(3));
                message.setSenderName(rs.getString(4));
                message.setMessage(rs.getString(5));
                message.setDate(rs.getTimestamp(6));
                messages.add(message);
            }
            return messages;

        }catch (Exception e){
            e.printStackTrace();}
        return messages;
  }

    //selecting all student who are in the team with id teamId.
    public ArrayList<Student>  getStudents(String teamId){
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        ArrayList<Student> students;
        String query = "SELECT * FROM STUDENT WHERE Student_Id IN (SELECT Student_Id from belongTo where Team_Id=?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            rs = preparedStatement.executeQuery();
            students=new ArrayList<>();
            while(rs.next()){
                Student student= new Student();
                student.setStudentId(rs.getString(1));
                student.setEmail(rs.getString(3));
                student.setName(rs.getString(2));
                student.setContactNo(rs.getString(4));
                students.add(student);
            }
            return students;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //check for whether student added in new team , if added then sending new teams for notification.
    public TeamUpdate checkTeamUpdate(TeamUpdate teamUpdate){
        int count = getTeamCount(teamUpdate.getStudentId());
        if(count > teamUpdate.getPrevCount()){  //if current count is greater than previous means student added in some new teams.
            teamUpdate.setUpdate(true);
            int diff = count-teamUpdate.getPrevCount();
            teamUpdate.setPrevCount(count);
            try{
                String query = "SELECT T.Team_Id,T.Name,T.Creator_Id,T.Date_Created FROM Teams T INNER JOIN " +
                        "(SELECT Team_Id FROM BelongTo WHERE Student_Id=? ORDER BY Added_Date DESC LIMIT ?) V ON T.Team_Id=V.Team_Id";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,teamUpdate.getStudentId());
                preparedStatement.setInt(2,diff);
                ResultSet rs = preparedStatement.executeQuery();
                ArrayList<Team> teams = new ArrayList<>();
                while(rs.next()){
                    Team team = new Team();
                    team.setTeamId(rs.getString(1));
                    team.setName(rs.getString(2));
                    team.setCreatorId(rs.getString(3));
                    team.setDateCreated(rs.getDate(4));
                    teams.add(team);
                }
                teamUpdate.setTeams(teams);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else
            teamUpdate.setUpdate(false);

        return teamUpdate;
    }

    //getting team count in which student with id studentId has been added.
    private int getTeamCount(String studentId){
        try {
            String query = "SELECT COUNT(Team_Id) FROM BelongTo WHERE Student_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,studentId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                return rs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    //comparing message count with prevCount and returns extra messages
    public MessageUpdate getMessageUpdate(MessageUpdate update){
        int count = getMessageCount(update.getTeamId());
        if(count>update.getPrevCount()){
            update.setUpdate(true);
            int diff = count-update.getPrevCount();
            update.setPrevCount(count);
            try {
                String query = "SELECT * FROM Messages WHERE Team_Id=? ORDER BY Time DESC LIMIT ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,update.getTeamId());
                preparedStatement.setInt(2,diff);
                ResultSet rs = preparedStatement.executeQuery();
                ArrayList<Message> messages = new ArrayList<>();
                while(rs.next()){
                    Message message = new Message();
                    message.setMessageId(rs.getString(1));
                    message.setTeamId(update.getTeamId());
                    message.setSenderId(rs.getString(3));
                    message.setSenderName(rs.getString(4));
                    message.setMessage(rs.getString(5));
                    message.setDate(rs.getTimestamp(6));
                    messages.add(message);
                }
                update.setMessages(messages);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return update;
    }

    //getting count of messages in team with given id.
    private int getMessageCount(String teamId){
        try {
            String query = "SELECT COUNT(Message_Id) FROM Messages WHERE Team_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                return rs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  0;
    }
}
