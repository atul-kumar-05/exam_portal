package com.exam.portal.entities;

import java.util.ArrayList;

public class TeamUpdate {
    private String studentId;
    private int prevCount;
    private boolean update;
    private ArrayList<Team> teams;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getPrevCount() {
        return prevCount;
    }

    public void setPrevCount(int prevCount) {
        this.prevCount = prevCount;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
}
