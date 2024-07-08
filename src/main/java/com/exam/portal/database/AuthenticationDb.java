package com.exam.portal.database;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

import java.sql.*;
import java.util.ArrayList;

public class AuthenticationDb {
    private final Connection connection;

    public AuthenticationDb(){
        connection = DatabaseConfig.getConnection();
    }

    //makes a new teacher in database and returns true if successful.
    public boolean register(Teacher teacher){
        PreparedStatement preparedStatement=null;
        String query="INSERT INTO Teacher values(?,?,?,?,?)";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,teacher.getTeacherId());
            preparedStatement.setString(2,teacher.getName());
            preparedStatement.setString(3,teacher.getEmail());
            preparedStatement.setString(4,teacher.getContactNo());
            preparedStatement.setString(5,teacher.getPassword());
            preparedStatement.execute();
            return true;

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //makes a new student in database and returns true if successful.
    public boolean register(Student student) {
        PreparedStatement preparedStatement = null;
        // connection=null;
        String query = "INSERT INTO Student values(?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getContactNo());
            preparedStatement.setString(5, student.getPassword());
            preparedStatement.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //check student's credentials , if match found than return true otherwise false.
    public boolean login(Student student){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT PASSWORD FROM STUDENT WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,student.getEmail());
            rs=preparedStatement.executeQuery();
            if(rs.next()){
                String checkPass=rs.getString(1);
                return checkPass.equals(student.getPassword());   //checking that password is correct or not.
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //check teacher's credentials , if match found than return true otherwise false.
    public boolean login(Teacher teacher){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT PASSWORD FROM TEACHER WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,teacher.getEmail());
            rs=preparedStatement.executeQuery();
            if(rs.next()){
                String checkPass=rs.getString(1);
                return checkPass.equals(teacher.getPassword());   //checking that password is correct or not.
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //search for the teacher in the teacher table with the help of email id and return if any teacher exist with the same.
    public Teacher findTeacherByEmail(String email){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT * FROM TEACHER WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            if (rs.next()){
                return extractTeacher(rs);   //creating a teacher object from table's row and return it.
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //search for the student in the student table with the help of email id and return if any teacher exist with the same.
    public Student findStudentByEmail(String email){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT * FROM Student WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            if (rs.next()){
                Student student = extractStudent(rs);   //creating a student object from table's row and return it.
                return student;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //searching for student in database , if student's name or email has prefix as a prefix than it is included in the result.
    public ArrayList<Student> searchStudent(String prefix){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        ArrayList<Student> students;
        String query="SELECT * FROM Student WHERE Name LIKE '%"+prefix+"%' OR Email LIKE '%"+prefix+"%'";
        try{
            preparedStatement=connection.prepareStatement(query);
            rs=preparedStatement.executeQuery();
            students = new ArrayList<>();
            while (rs.next()){
                students.add(extractStudent(rs));
            }
            return students;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //searching for teacher in database , if teacher's name or email has prefix as a prefix than it is included in the result.
    public ArrayList<Teacher> searchTeacher(String prefix){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        ArrayList<Teacher> teachers;
        String query="SELECT * FROM Teacher WHERE Name LIKE '%"+prefix+"%' OR Email LIKE '%"+prefix+"%'";
        try{
            preparedStatement=connection.prepareStatement(query);
            rs=preparedStatement.executeQuery();
            teachers= new ArrayList<>();
            while (rs.next()){
                Teacher teacher = extractTeacher(rs);
                teachers.add(teacher);
            }
            return teachers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //first authenticate teacher's credentials than update details save in the database.
    public boolean updateTeacher(Teacher teacher){
        PreparedStatement preparedStatement = null;
        String query = "UPDATE Teacher SET Email=?,Name=?,ContactNo=? WHERE Teacher_Id=? AND Password=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teacher.getEmail());
            preparedStatement.setString(2,teacher.getName());
            preparedStatement.setString(3,teacher.getContactNo());
            preparedStatement.setString(4, teacher.getTeacherId());
            preparedStatement.setString(5,teacher.getPassword());
            if(preparedStatement.executeUpdate()>0)         //check that if any row gets updated or not.
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    //first authenticate student's credentials than update details save in the database.
    public boolean updateStudent(Student student){
        PreparedStatement preparedStatement = null;
        String query = "UPDATE Student SET Email=?,Name=?,ContactNo=? WHERE Student_Id=? AND Password=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,student.getEmail());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getContactNo());
            preparedStatement.setString(4, student.getStudentId());
            preparedStatement.setString(5,student.getPassword());
            if(preparedStatement.executeUpdate()>0)
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    //create a teacher object from table's row.
    private Teacher extractTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getString(1));
        teacher.setName(rs.getString(2));
        teacher.setEmail(rs.getString(3));
        teacher.setContactNo(rs.getString(4));
        teacher.setPassword("");
        return teacher;
    }

    //create a student object from table's row.
    private Student extractStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString(1));
        student.setName(rs.getString(2));
        student.setEmail(rs.getString(3));
        student.setContactNo(rs.getString(4));
        student.setPassword("");
        return student;
    }

}