package com.exam.portal.controllers;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;
import com.exam.portal.interfaces.Authentication;
import com.exam.portal.services.AuthenticationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/exam-portal/authentication")
public class AuthenticationController {
    private static final String SUCCESSFUL = "SUCCESSFUL";
    private static final String FAILED = "FAILED";

    private final Authentication authentication;

    public AuthenticationController(){
        authentication = new AuthenticationService();
    }

    @RequestMapping("/student/signUp")
    public String studentRegister(@RequestBody Student student){
        if(authentication.register(student))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/student/signIn")
    public String studentLogin(@RequestBody Student student){
        if(authentication.login(student))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/teacher/signUp")
    public String teacherRegister(@RequestBody Teacher teacher){
        if(authentication.register(teacher))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/teacher/signIn")
    public String teacherLogin(@RequestBody Teacher teacher){
        if(authentication.login(teacher))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/teacher/get/{email}")
    public Teacher getTeacher(@PathVariable String email){
        return authentication.findTeacherByEmail(email);
    }

    @RequestMapping("/student/get/{email}")
    public Student getStudent(@PathVariable String email){
        return authentication.findStudentByEmail(email);
    }

    @RequestMapping("/teacher/search/q={prefix}")
    public ArrayList<Teacher> searchTeacher(@PathVariable String prefix){
        return authentication.searchTeacher(prefix);
    }

    @RequestMapping("/student/search/q={prefix}")
    public ArrayList<Student> searchStudent(@PathVariable String prefix){
        return authentication.searchStudent(prefix);
    }

    @RequestMapping("/teacher/update")
    public String updateTeacherDetails(@RequestBody Teacher teacher){
        if(authentication.updateTeacher(teacher))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/student/update")
    public String updateStudentDetails(@RequestBody Student student){
        if(authentication.updateStudent(student))
            return SUCCESSFUL;
        return FAILED;
    }

}
