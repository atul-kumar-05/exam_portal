package com.exam.portal.entities;

import java.util.ArrayList;

public class ExamResponse {
    private String examId;
    private String studentId;
    private ArrayList<QuestionResponse> responses;
    private double marks;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ArrayList<QuestionResponse> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<QuestionResponse> responses) {
        this.responses = responses;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
}
