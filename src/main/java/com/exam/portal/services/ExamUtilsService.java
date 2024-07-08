package com.exam.portal.services;

import com.exam.portal.database.ExamUtilsDb;
import com.exam.portal.entities.*;
import com.exam.portal.interfaces.ExamUtils;

import java.util.ArrayList;

public class ExamUtilsService implements ExamUtils {
    private final ExamUtilsDb examUtilsDb;

    public ExamUtilsService(){
        examUtilsDb = new ExamUtilsDb();
    }

    @Override
    public boolean createExam(Exam exam, ArrayList<Question> questions) {
        return examUtilsDb.createExam(exam) && examUtilsDb.addQuestions(questions);
    }

    @Override
    public ArrayList<Exam> getExamsScheduledBy(String teacherId) {
        return examUtilsDb.getExamsScheduledBy(teacherId);
    }

    @Override
    public ArrayList<Exam> getExamsScheduledFor(String studentId) {
        return examUtilsDb.getExamsScheduledFor(studentId);
    }

    @Override
    public Exam getExamById(String examId) {
        return examUtilsDb.getExamById(examId);
    }

    @Override
    public boolean submitExam(ExamResponse response) {
        return examUtilsDb.submitExam(response);
    }

    @Override
    public ExamResponse getStudentSubmission(String examId,String studentId){
        return examUtilsDb.getStudentSubmissions(examId,studentId);
    }

    @Override
    public ExamUpdate checkExamUpdate(ExamUpdate update){
        return examUtilsDb.checkExamUpdate(update);
    }

    @Override
    public ExamUpdate checkExamStartUpdate(ExamUpdate update) {
        return  examUtilsDb.checkExamStartUpdate(update);
    }

    @Override
    public ArrayList<StudentResponse> getExamsSubmissionDetails(String examId) {
        return examUtilsDb.getExamsSubmissionDetails(examId);
    }

    @Override
    public boolean updateMarks(ExamResponse response){
        return examUtilsDb.updateMarks(response);
    }

}
