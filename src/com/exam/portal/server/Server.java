package com.exam.portal.server;

import com.exam.portal.models.*;

import java.util.ArrayList;

//this interface contains all server-client communication methods.
public interface Server {
    public boolean login(Student student);
    public boolean login(Teacher teacher);
    public boolean register(Student student);
    public boolean register(Teacher teacher);
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
    public boolean createExam(Exam exam);
    public Teacher getTeacher(String email);
    public Student getStudent(String email);
    public ArrayList<Teacher> searchTeacher(String text);
    public ArrayList<Student> searchStudent(String text);
    public ArrayList<Team> getStudentsTeams(String Id);
    public ArrayList<Team> getTeachersTeams(String Id);
    public boolean updateTeacher(Teacher teacher);
    public boolean getProctorResult(Image image);
    public ArrayList<Exam> getExamScheduledBy(String teacherId);
    public Exam getExamById(String examId);
    public Team getTeamById(String id);
    public boolean updateStudent(Student student);
    public ArrayList<Exam> getExamScheduledFor(String studentId);
    public boolean joinTeamWithId(String teamId,String studentId);
    public boolean sendExamResponse(ExamResponse response);
    public ExamResponse getStudentExamResponse(String examId,String studentId);
    public ArrayList<Student> getStudentsByTeamId(String Id);
    public boolean sendMassage(Message newMassage);
    public ArrayList<Message> getMassages(String teamId);
    public TeamUpdate checkTeamUpdate(TeamUpdate update);
    public ExamUpdate checkExamUpdate(ExamUpdate update);
    public MessageUpdate checkMessageUpdate(MessageUpdate update);
    public  ArrayList<StudentResponse> fetchExamDetails(String examId);
    public boolean updateMarks(ExamResponse response);
}
