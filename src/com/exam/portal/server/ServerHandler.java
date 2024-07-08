package com.exam.portal.server;

import com.exam.portal.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler implements Server{
    private static final String AUTHENTICATION_URL = "/authentication";
    private static final String TEAM_URL = "/team-utilities";
    private static final  String EXAM_URL = "/exam-utilities";

    private static final String SUCCESSFUL = "SUCCESSFUL";

    private static Gson gson;
    //connection which link to the specific url on server.
    private HttpURLConnection connection;


    private static ServerHandler serverHandler=null;

    private ServerHandler(){
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    //returns the singleton object of ServerHandler
    public static ServerHandler getInstance(){
        if(serverHandler == null)
            serverHandler = new ServerHandler();
        return serverHandler;
    }

    //sending login request to the server.
    @Override
    public boolean login(Student student) {
        try{
            String url = AUTHENTICATION_URL + "/student/signIn";
            connection = ServerConfig.getConnection(url);
            String json = gson.toJson(student);
            assert connection != null;
            writeJson(json);

            //reading server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();

            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //sending login request to the server.
    @Override
    public boolean login(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/signIn";
            connection = ServerConfig.getConnection(url);
            String json = gson.toJson(teacher);
            assert connection != null;
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending sign up request to the server.
    @Override
    public boolean register(Student student) {
        try {
            String url = AUTHENTICATION_URL + "/student/signUp";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(student);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending sign up request to the server.
    @Override
    public boolean register(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/signUp";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(teacher);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending request for creating a new team.
    @Override
    public boolean createTeam(Team team) {
        try {
            String url = TEAM_URL + "/create-team";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(team);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending request to add student in team,belongTo consist student and team object.
    @Override
    public boolean addStudent(BelongTo belongTo) {
        try {
            String url = TEAM_URL + "/add-student";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(belongTo);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending request for creating new exam.
    @Override
    public boolean createExam(Exam exam) {
        try {
            String url = EXAM_URL + "/create-exam";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(exam);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //fetching teacher details via email id.
    @Override
    public Teacher getTeacher(String email) {
        try{
            String url = AUTHENTICATION_URL + "/teacher/get/"+email;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            Teacher teacher = gson.fromJson(response,Teacher.class);
            return teacher;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching student details via email id.
    @Override
    public Student getStudent(String email) {
        try{
            String url = AUTHENTICATION_URL + "/student/get/"+email;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            Student student = gson.fromJson(response,Student.class);
            return student;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching teachers whose name or email contains text as a substring.
    @Override
    public ArrayList<Teacher> searchTeacher(String text) {
        try{
            String url = AUTHENTICATION_URL + "/teacher/search/q="+text;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Teacher> teachers = gson.fromJson(response,new TypeToken<List<Teacher>>(){}.getType());
            return teachers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching students whose name or email contains text as a substring.
    @Override
    public ArrayList<Student> searchStudent(String text) {
        try{
            String url = AUTHENTICATION_URL + "/student/search/q="+text;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Student> students = gson.fromJson(response,new TypeToken<List<Student>>(){}.getType());
            return students;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching all teams who contains one student with given id.
    @Override
    public ArrayList<Team> getStudentsTeams(String Id) {
        try{
            String url = TEAM_URL + "/get/all/student/id="+Id;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Team> teams = gson.fromJson(response,new TypeToken<List<Team>>(){}.getType());
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching all teams who contains one admin with given id.
    @Override
    public ArrayList<Team> getTeachersTeams(String Id) {
        try{
            String url = TEAM_URL + "/get/all/teacher/id="+Id;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Team> teams = gson.fromJson(response,new TypeToken<List<Team>>(){}.getType());
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //sending request for updating teacher's details.
    @Override
    public boolean updateTeacher(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/update";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(teacher);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //sending request for updating student's details.
    @Override
    public boolean updateStudent(Student student) {
        try {
            String url = AUTHENTICATION_URL + "/student/update";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(student);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean getProctorResult(Image image){
        try{
            String url = EXAM_URL+"/proctor/get";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(image);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    //fetching all the exams scheduled by teacher with id teacherId
    @Override
    public ArrayList<Exam> getExamScheduledBy(String teacherId) {
        ArrayList<Exam> exams;
        try {
            String url = EXAM_URL+"/scheduled-by/"+teacherId;
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            exams = gson.fromJson(response,new TypeToken<List<Exam>>(){}.getType());
            return exams;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //fetching all exams scheduled in teams in which student with id studentId exist.
    @Override
    public ArrayList<Exam> getExamScheduledFor(String studentId){
        ArrayList<Exam> exams;
        try {
            String url = EXAM_URL+"/scheduled-for/"+studentId;
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            exams = gson.fromJson(response,new TypeToken<List<Exam>>(){}.getType());
            return exams;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //fetching exam with given Id.
    @Override
    public Exam getExamById(String examId) {
        try{
            String url = EXAM_URL + "/get-exam/id="+(examId.substring(examId.indexOf('#')+1));
            System.out.println(url);
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            Exam exam = gson.fromJson(response,Exam.class);
            return exam;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //fetching team with given id.
    @Override
    public Team getTeamById(String id) {
        try{
            String url = TEAM_URL+"/get/id="+(id.substring(id.indexOf('#')+1));
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Team team = gson.fromJson(reader,Team.class);
            return team;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //join team with ID
    @Override
    public boolean joinTeamWithId(String teamId,String studentId){
        try {
            teamId = teamId.substring(teamId.indexOf("#")+1);
            String url =  TEAM_URL+ "/student/join/"+ teamId+"/"+studentId;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //fetching all the students of particular team with given id
    @Override
    public ArrayList<Student> getStudentsByTeamId(String Id) {
        try {
            Id = Id.substring(Id.indexOf("#")+1);
            String url = TEAM_URL + "/get/student/all/id=" + Id;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Student> students = gson.fromJson(response, new TypeToken<List<Student>>() {}.getType());
            return students;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //sending student response to server.
    @Override
    public boolean sendExamResponse(ExamResponse response) {
        try{
            String url = EXAM_URL+"/student/submit-test";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(response);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String res = reader.readLine();
            return res.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //requesting for student's submission of exam.
    @Override
    public ExamResponse getStudentExamResponse(String examId, String studentId) {
        try{
            examId = examId.substring(examId.indexOf('#')+1);
            String url = EXAM_URL+"/get/submission/"+examId+"/"+studentId;
            connection = ServerConfig.getConnection(url);
            assert connection!= null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String res = reader.readLine();
            ExamResponse response = gson.fromJson(res,ExamResponse.class);
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateMarks(ExamResponse response){
        try {
            String url = EXAM_URL+"/update/marks";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(response);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String res = reader.readLine();
            return res.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //sending request for checking that whether student has been added in new team or not.
    @Override
    public TeamUpdate checkTeamUpdate(TeamUpdate update){
        try{
            String url = TEAM_URL+"/get/update";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(update);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String res = reader.readLine();
            update = gson.fromJson(res,TeamUpdate.class);
            return update;
        }catch (Exception e){
            e.printStackTrace();
        }

        return update;
    }

    //sending request for checking whether a new exam has been scheduled or ready to start within 15
    // minutes in teams in which student is present.
    @Override
    public ExamUpdate checkExamUpdate(ExamUpdate update){
        try{
            String url = EXAM_URL+"/get/update";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(update);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String res = reader.readLine();
            update = gson.fromJson(res,ExamUpdate.class);
            return update;
        }catch (Exception e){
            e.printStackTrace();
        }

        return update;
    }

    //sending message in team.
    @Override
    public  boolean sendMassage(Message newMassage){
        try{
            String url = TEAM_URL + "/send/message/";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(newMassage);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //fetching all messages sent in the team.
    @Override
    public ArrayList<Message> getMassages(String teamId){
        ArrayList<Message>returnedMassages;
        try{
            teamId = teamId.substring(teamId.indexOf('#')+1);
            String url=TEAM_URL + "/get/all/messages/id="+teamId;
            connection=ServerConfig.getConnection(url);
            assert connection!=null;

            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            returnedMassages = gson.fromJson(response,new TypeToken<List<Message>>(){}.getType());
            return returnedMassages;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<StudentResponse> fetchExamDetails(String examId){
        ArrayList<StudentResponse>fetchedDetails;
        try{
            examId = examId.substring(examId.indexOf('#')+1);
            String url=EXAM_URL+"/get/exam/submission/details/exam_id="+examId;
            connection=ServerConfig.getConnection(url);
            assert connection!=null;
            BufferedReader reader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response=reader.readLine();
            fetchedDetails=gson.fromJson(response,new TypeToken<List<StudentResponse>>(){}.getType());
            return fetchedDetails;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //sending current count of messages which will be compare with messages on server and extra messages gets returned.
    @Override
    public MessageUpdate checkMessageUpdate(MessageUpdate update){
        try {
            String url = TEAM_URL+"/get/update/message";
            connection = ServerConfig.getConnection(url);
            assert connection!=null;
            String json = gson.toJson(update);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            update = gson.fromJson(response,MessageUpdate.class);
            return update;
        }catch (Exception e){
            e.printStackTrace();
        }
        return update;
    }

    //method for writing object in request body
    private void writeJson(String json){
        try {
            //System.out.println(json);
            connection.setFixedLengthStreamingMode(json.length());
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
