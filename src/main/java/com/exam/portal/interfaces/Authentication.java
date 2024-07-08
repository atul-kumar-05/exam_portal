package com.exam.portal.interfaces;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

import java.util.ArrayList;

public interface Authentication {
    public boolean register(Student student);
    public boolean register(Teacher teacher);
    public boolean login(Student student);
    public boolean login(Teacher teacher);
    public Teacher findTeacherByEmail(String email);
    public Student findStudentByEmail(String email);
    public ArrayList<Teacher> searchTeacher(String prefix);
    public ArrayList<Student> searchStudent(String prefix);
    public boolean updateTeacher(Teacher teacher);
    public boolean updateStudent(Student student);
}
