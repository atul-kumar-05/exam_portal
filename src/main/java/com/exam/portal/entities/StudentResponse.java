package com.exam.portal.entities;

public class StudentResponse {
    public  String name;
    public  String email;
    public  String contact;
    public double marksObtained;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }
}
