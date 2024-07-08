package com.exam.portal.services;

import com.exam.portal.database.AuthenticationDb;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;
import com.exam.portal.interfaces.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

public class AuthenticationService implements Authentication {
    private final AuthenticationDb authentication;

    public AuthenticationService(){
        authentication = new AuthenticationDb();
    }

    @Override
    public boolean register(Student student) {
        return authentication.register(student);
    }

    @Override
    public boolean register(Teacher teacher) {
        return authentication.register(teacher);
    }

    @Override
    public boolean login(Student student) {
        return authentication.login(student);
    }

    @Override
    public boolean login(Teacher teacher) {
        return authentication.login(teacher);
    }

    @Override
    public Teacher findTeacherByEmail(String email) {
        return authentication.findTeacherByEmail(email);
    }

    @Override
    public Student findStudentByEmail(String email) {
        return authentication.findStudentByEmail(email);
    }

    @Override
    public ArrayList<Teacher> searchTeacher(String prefix) {
        return authentication.searchTeacher(prefix);
    }

    @Override
    public ArrayList<Student> searchStudent(String prefix) {
        return authentication.searchStudent(prefix);
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        return authentication.updateTeacher(teacher);
    }

    @Override
    public boolean updateStudent(Student student){
        return authentication.updateStudent(student);
    }
}
