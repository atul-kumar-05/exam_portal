package com.exam.portal.entities;

public class Teacher {
    private String teacherId;
    private String name;
    private String email;
    private String contactNo;
    private String password;

    public Teacher() {
    }

    public Teacher(String teacherId, String name, String email, String contactNo, String password) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.password = password;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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
