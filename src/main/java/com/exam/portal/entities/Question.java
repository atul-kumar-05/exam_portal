package com.exam.portal.entities;

import java.util.ArrayList;;

public class Question {
    private String examId;
    private String questionId;
    private String question;
    private String file;
    private boolean isImage;
    private int questionType;
    private String response;

    private double point;
    private double negPoint;

    private ArrayList<Option> options;
    private String answer;

    public Question(){
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getNegPoint() {
        return negPoint;
    }

    public void setNegPoint(double negPoint) {
        this.negPoint = negPoint;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setOptions(ArrayList<Option> options){
        this.options = options;
    }

    public ArrayList<Option> getOptions(){
        return options;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}