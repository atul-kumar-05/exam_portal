package com.exam.portal.database;

import com.exam.portal.entities.*;

import javax.sql.rowset.serial.SerialBlob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.UUID;

public class ExamUtilsDb {
    private static final int SUBJECTIVE=2;
    private static final String OPTIONS = "options";
    private static final String TEXT = "text";

    private final Connection connection;

    public ExamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    //creates a new exam in the team and return true if successfully created otherwise returns false.
    public boolean createExam(Exam exam){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Exams VALUES (?,?,?,?,?,?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,exam.getExamId());
            preparedStatement.setString(2,exam.getTeamId());
            preparedStatement.setString(3,exam.getCreatorId());
            preparedStatement.setString(4,exam.getTitle());
            preparedStatement.setDate(5,exam.getExamDate());
            preparedStatement.setTime(6,exam.getTime());
            preparedStatement.setInt(7,Integer.parseInt(exam.getDuration()));
            preparedStatement.setTimestamp(8,new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(9,exam.getQuestionCount());
            preparedStatement.setDouble(10,exam.getMaxScore());
            preparedStatement.execute();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    //adding all questions of a exam in questions table and map them with exam using examId.
    public boolean addQuestions(ArrayList<Question> questions){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Questions VALUES(?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            for(Question question:questions){
                preparedStatement.setString(1, question.getExamId());
                preparedStatement.setString(2,question.getQuestionId());
                preparedStatement.setDouble(5,question.getPoint());
                preparedStatement.setDouble(6,question.getNegPoint());
                preparedStatement.setInt(8,question.getQuestionType());
                preparedStatement.setBoolean(4,question.getIsImage());
                if(question.getIsImage()){                                         //checking that if question is a image or not.
                    byte[] bytes = decodeStringToImage(question.getFile());       //decode image from encoded string.
                    preparedStatement.setString(3,uploadFile(bytes));  //image is uploaded and its id gets returned.
                }
                else
                    preparedStatement.setString(3,question.getQuestion());

                if(question.getQuestionType()==SUBJECTIVE){       //if question is subjective than storing answer's id as answer.
                    String id = uploadText(question.getAnswer());
                    preparedStatement.setString(7,id);
                }else
                    preparedStatement.setString(7,question.getAnswer());

                preparedStatement.execute();
                uploadOptions(question.getOptions());             //uploading all options in current question.
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //uploading options of question.
    public void uploadOptions(ArrayList<Option> options){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Options VALUES(?,?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            for(Option option:options){
                preparedStatement.setString(1,option.getExamId());
                preparedStatement.setString(2,option.getQuestionId());
                preparedStatement.setString(3,option.getIndex());
                preparedStatement.setBoolean(4,option.isImage());
                preparedStatement.setBoolean(6,option.isCorrect());
                if(option.isImage()){                                       //checking that option is image or not.
                    byte[] bytes = decodeStringToImage(option.getFile());    //decoding image from encoded string.
                    preparedStatement.setString(5,uploadFile(bytes));  //adding image's id in column.
                }
                else
                    preparedStatement.setString(5,option.getText());
                preparedStatement.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //uploading images and return its id.
    public String uploadFile(byte[] file){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO files VALUES(?,?)";
        try{
            String id = generateId();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,id);
            Blob blob = new SerialBlob(file);           //creating blob from byte array.
            preparedStatement.setBlob(2,blob);
            preparedStatement.execute();
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String uploadText(String text){
        String id = checkText(text);
        if(id != null)
            return id;
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO Text_Responses VALUES (?,?)";
        try{
            id = generateId();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,text);
            preparedStatement.execute();

            return id;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //return textId if text is already exist in table.
    public String checkText(String text){
        PreparedStatement preparedStatement=null;
        String query = "SELECT Text_Id FROM Text_Responses WHERE Text=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,text);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                return rs.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return all the exams(without questions) which are scheduled by teacher having id as teacherId.
    public ArrayList<Exam> getExamsScheduledBy(String teacherId){
        PreparedStatement preparedStatement=null;
        ResultSet rs;
        String query = "SELECT * FROM Exams WHERE Creator_Id=? ORDER BY Created_At";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teacherId);
            rs = preparedStatement.executeQuery();
            ArrayList<Exam> exams = new ArrayList<>();
            while(rs.next()){
                Exam exam = extractExam(rs);
                exams.add(exam);
            }
            return exams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return all the exams(without questions) which are scheduled in team in which student having id studentId is included.
    public ArrayList<Exam> getExamsScheduledFor(String studentId){
        PreparedStatement preparedStatement;
        ResultSet rs;
        String query = "SELECT * FROM Exams WHERE Team_Id IN (SELECT Team_Id FROM BelongTo WHERE Student_Id=?) ORDER BY Created_At";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,studentId);
            rs = preparedStatement.executeQuery();
            ArrayList<Exam> exams = new ArrayList<>();
            while(rs.next()){
                Exam exam = extractExam(rs);
                exams.add(exam);
            }
            return exams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return full detailed exam object including questions and options.
    public Exam getExamById(String examId){
        try {
            String query = "SELECT * FROM Exams WHERE Exam_Id=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                System.out.println(examId);
                Exam exam = extractExam(rs);
                exam.setQuestions(getExamQuestions(examId));    //fetching questions adding in exam.

                return exam;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //creating exam object from table row and return it.
    private Exam extractExam(ResultSet rs) throws SQLException {
        Exam exam = new Exam();
        exam.setExamId(rs.getString(1));
        exam.setTeamId(rs.getString(2));
        exam.setCreatorId(rs.getString(3));
        exam.setTitle(rs.getString(4));
        exam.setExamDate(rs.getDate(5));
        exam.setTime(rs.getTime(6));
        exam.setDuration(rs.getInt(7)+"");
        exam.setQuestionCount(rs.getInt(9));
        exam.setMaxScore(rs.getDouble(10));
        return  exam;
    }

    //returns list of all questions included in exam with id same as examId.
    public ArrayList<Question> getExamQuestions(String examId){
        try {
            String query = "SELECT * FROM Questions WHERE Exam_Id=? ORDER  BY Q_Id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Question> questions = new ArrayList<>();
            while(rs.next()){
                Question question = new Question();
                question.setExamId(rs.getString(1));
                question.setQuestionId(rs.getString(2));
                question.setIsImage(rs.getBoolean(4));
                if(question.getIsImage())
                    question.setFile(getImage(rs.getString(3)));   //encoding image in string setting as a file.
                else
                    question.setQuestion(rs.getString(3));
                question.setPoint(rs.getInt(5));
                question.setNegPoint(rs.getInt(6));
                question.setQuestionType(rs.getInt(8));
                if(question.getQuestionType()==SUBJECTIVE){   //if question is subjective than getting the original answer.
                    question.setAnswer(getText(rs.getString(7)));
                }
                question.setOptions(getOptionsOf(examId,question.getQuestionId()));   //fetching options and adding in question.

                questions.add(question);
            }

            return questions;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return all the options of question with id as questionId and in same exam as examId.
    public ArrayList<Option> getOptionsOf(String examId,String questionId){
        try {
            String query = "SELECT * FROM Options WHERE Exam_Id=? AND Q_Id=? ORDER BY O_Id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            preparedStatement.setString(2,questionId);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Option> options = new ArrayList<>();
            while (rs.next()){
                Option option = new Option();
                option.setExamId(rs.getString(1));
                option.setQuestionId(rs.getString(2));
                option.setIndex(rs.getString(3));
                option.setIsImage(rs.getBoolean(4));
                option.setCorrect(rs.getBoolean(6));
                if(option.isImage())
                    option.setFile(getImage(rs.getString(5)));  //encoding image as string and setting it.
                else
                    option.setText(rs.getString(5));

                options.add(option);
            }

            return options;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //add student submission in database.
    public boolean submitExam(ExamResponse response){
        String query = "INSERT INTO Exam_Response VALUES (?,?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,response.getExamId());
            preparedStatement.setString(2,response.getStudentId());
            preparedStatement.setString(3,getResponse(response.getResponses()));
            preparedStatement.setDouble(4,response.getMarks());
            preparedStatement.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //creating a single response string by concatenating all responses and separating by "|;
    private String getResponse(ArrayList<QuestionResponse> responses){
        if(responses==null || responses.size()==0)
            return "";
        StringBuilder response = new StringBuilder();
        for(QuestionResponse questionResponse:responses){
            if(questionResponse.getResponseType().equals(OPTIONS))
                response.append(questionResponse.getResponse()).append("@").append(OPTIONS).append("|");
            else if(questionResponse.getResponseType().equals(TEXT)){
                response.append(uploadText(questionResponse.getResponse())).append("@").append(TEXT).append("|");
            }
        }
        response.deleteCharAt(response.lastIndexOf("|"));
        return response.toString();
    }

    //fetching student submissions details
    public ExamResponse getStudentSubmissions(String examId,String studentId){
        String query = "SELECT * FROM Exam_Response WHERE Exam_Id=? AND Student_Id=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            preparedStatement.setString(2,studentId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                ExamResponse response = new ExamResponse();
                response.setExamId(examId);
                response.setStudentId(studentId);
                response.setMarks(rs.getDouble(4));
                response.setResponses(extractQuestionResponse(rs.getString(3)));
                return response;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //extracting question response from response string.
    private ArrayList<QuestionResponse> extractQuestionResponse(String response){
        ArrayList<QuestionResponse> responses = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(response,"|");
        int idx=0;
        while(tokenizer.hasMoreTokens()){
            QuestionResponse questionResponse = new QuestionResponse();
            String token = tokenizer.nextToken();
            String text = token.substring(0,token.lastIndexOf('@'));
            String type = token.substring(token.lastIndexOf('@')+1);
            questionResponse.setQId(""+idx++);
            questionResponse.setResponse(text);
            if(type.equals(TEXT)){
                questionResponse.setResponseType(TEXT);
                questionResponse.setResponse(getText(text));
            }else{
                questionResponse.setResponseType(OPTIONS);
            }
            responses.add(questionResponse);
        }

        return responses;
    }

    //updating student marks.
    public boolean updateMarks(ExamResponse response){
        try {
            String query = "UPDATE Exam_Response SET Marks=? WHERE Exam_Id=? AND Student_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1,response.getMarks());
            preparedStatement.setString(2,response.getExamId());
            preparedStatement.setString(3,response.getStudentId());
            int count = preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //comparing prevCount of exams with current count if they are not equal than adding new exams to list in examUpdate.
    public ExamUpdate checkExamUpdate(ExamUpdate update){
        int count = getExamCount(update.getStudentId());
        if(count>update.getPrevCount()){
            update.setUpdate(true);
            int diff = count-update.getPrevCount();
            update.setPrevCount(count);
            String query = "SELECT * FROM Exams WHERE Team_Id IN (SELECT Team_Id FROM BelongTo WHERE Student_Id=?) ORDER BY Created_At DESC LIMIT ?";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,update.getStudentId());
                preparedStatement.setInt(2,diff);
                ResultSet rs = preparedStatement.executeQuery();
                ArrayList<Exam> exams = new ArrayList<>();
                while(rs.next()){
                    exams.add(extractExam(rs));
                }
                update.setExams(exams);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else
            update.setUpdate(false);
        return update;
    }

    //getting count of all exams in team in which student with id studentId has been added.
    private int getExamCount(String studentId){
        try {
            String query = "SELECT COUNT(Exam_Id) FROM Exams WHERE Creator_Id IN (SELECT Team_Id FROM BelongTo WHERE Student_Id=?)";
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

    //checking for exams which starts after 15 minutes.
    public ExamUpdate checkExamStartUpdate(ExamUpdate update){
        int count = getExamStartCount(update.getStudentId());
        if(count>update.getPrevCount()){
            update.setUpdate(true);
            try{
                int diff = count-update.getPrevCount();
                String query = "SELECT COUNT(Exam_Id) WHERE Creator_Id IN (SELECT Team_Id FROM BelongTo WHERE Student_Id=?) AND  " +
                               "Exam_Date=NOW() AND Exam_Time>NOW() AND Exam_Time<=NOW()+INTERVAL 15 MINUTE ORDER BY Created_At DESC LIMIT ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(2,diff);
                preparedStatement.setString(1,update.getStudentId());
                ResultSet rs = preparedStatement.executeQuery();
                ArrayList<Exam> exams = new ArrayList<>();
                while(rs.next())
                    exams.add(extractExam(rs));
                update.setExams(exams);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else
            update.setUpdate(false);
        return update;
    }

    //getting count of all exams in team in which student with id studentId has been added.
    private int getExamStartCount(String studentId){
        try {
            String query = "SELECT COUNT(Exam_Id) WHERE Creator_Id IN (SELECT Team_Id FROM BelongTo WHERE Student_Id=?) AND  " +
                           "Exam_Date=NOW() AND Exam_Time>NOW() AND Exam_Time<=NOW()+INTERVAL 15 MINUTE";
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

    //getting submission details of an Exam
    public ArrayList<StudentResponse> getExamsSubmissionDetails(String examId){
        PreparedStatement preparedStatement=null;
        ResultSet rs;
        String query = "SELECT s.Name,s.Email,s.ContactNo,e.Marks FROM Exam_Response e " +
                "INNER JOIN STUDENT s ON s.Student_Id=e.Student_Id AND e.Exam_Id=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            rs = preparedStatement.executeQuery();
            ArrayList<StudentResponse> studentResponses = new ArrayList<>();
            while(rs.next()){
                StudentResponse studentresponse = new StudentResponse();
                studentresponse.setName(rs.getString(1));
                studentresponse.setEmail(rs.getString(2));
                studentresponse.setContact(rs.getString(3));
                studentresponse.setMarksObtained(rs.getDouble(4));

                studentResponses.add(studentresponse);
            }
            return studentResponses;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //getting blob of image using fileId and encode it as a string and return ti.
    private String getImage(String fileId){
        try {
            String query = "SELECT data FROM files WHERE file_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fileId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return encodeImageToString(rs.getBlob(1));   //encoding blob.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //getting actual text with the help of id.
    private String getText(String textId){
        try{
            String query = "SELECT Text FROM Text_Responses WHERE Text_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,textId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
                return rs.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //generates a unique id for fileId.
    private String generateId(){
        return UUID.randomUUID().toString();
    }

    //decoding string in base64 to byte array.
    private byte[] decodeStringToImage(String encodedFile){
        return Base64.getDecoder().decode(encodedFile);
    }

    //encoding blob as a string using base64 encoder.
    private String encodeImageToString(Blob blob){
        try {
            return Base64.getEncoder().encodeToString(blob.getBinaryStream().readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
