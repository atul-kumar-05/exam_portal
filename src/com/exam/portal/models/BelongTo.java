package com.exam.portal.models;

public class BelongTo {
    private String studentId;
    private String teamId;
    private String date;

    public BelongTo() {
    }

    public BelongTo(String studentId, String teamId, String date) {
        this.studentId = studentId;
        this.teamId = teamId;
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
