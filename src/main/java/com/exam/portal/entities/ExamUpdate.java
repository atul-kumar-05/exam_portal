package com.exam.portal.entities;

import java.util.ArrayList;

public class ExamUpdate {
    private String studentId;
    private String type;        //for distinguish between exam create or exam start.
    private int prevCount;
    private boolean update;
    private ArrayList<Exam> exams;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }

    public int getPrevCount() {
        return prevCount;
    }

    public void setPrevCount(int prevCount) {
        this.prevCount = prevCount;
    }
}
