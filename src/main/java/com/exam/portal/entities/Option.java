package com.exam.portal.entities;

public class Option {
    private String examId;
    private String questionId;
    private String index;

    private boolean isImage;
    private String text;
    private String file;

    private boolean correct;

    public Option(){}

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

    public void setFile(String  file) {
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

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
