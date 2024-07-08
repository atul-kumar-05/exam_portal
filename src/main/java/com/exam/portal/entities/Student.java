package com.exam.portal.entities;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String contactNo;
    private String password;

    public Student(){}

    public Student(String studentId, String name, String email, String contactNo, String password) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
