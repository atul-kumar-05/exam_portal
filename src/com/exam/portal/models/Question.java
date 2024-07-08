package com.exam.portal.models;

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
        options = new ArrayList<>();
        isImage = false;
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

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
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

    public int getQuestionType(){
        return questionType;
    }

    public void setQuestionType(int type){
        this.questionType = type;
    }

    public String getResponse(){
        return response;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public void addOption(Option option){
        option.setIndex(options.size()+"");
        options.add(option);
    }

    public Option undoOption(){
        int idx = options.size();
        if(idx==0)
            return null;
        Option res = options.get(idx-1);
        options.remove(idx-1);
        return res;
    }

    public void setOptions(ArrayList<Option> options){
        this.options = options;
    }

    public ArrayList<Option> getOptions(){
        return options;
    }

    public int getOptionCount(){
        return options.size();
    }

    public Option getLastQuestion(){
        return options.get(options.size()-1);
    }
}
