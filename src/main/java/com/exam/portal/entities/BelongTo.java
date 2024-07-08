package com.exam.portal.entities;

import java.sql.Date;

public class BelongTo {
    private String studentId;
    private String teamId;
    private Date date;

    public BelongTo() {
    }

    public BelongTo(String studentId, String teamId, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
