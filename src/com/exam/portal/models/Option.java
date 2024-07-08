package com.exam.portal.models;

public class Option {
    private String examId;
    private String questionId;
    private String index;

    private boolean isImage;
    private String text;
    private String  file;

    private boolean selected;
    private boolean correct;

    public Option(){
        selected = false;
        correct = false;
    }

    public Option(String text){
        this();
        this.text = text;
        this.isImage = false;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean isCorrect) {
        this.correct = isCorrect;
    }

    public boolean getSelected() {
        return selected;
    }

    public boolean setSelected() {
        selected = !selected;
        return selected;
    }
}
