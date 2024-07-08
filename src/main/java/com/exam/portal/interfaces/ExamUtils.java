package com.exam.portal.interfaces;

import com.exam.portal.entities.*;

import java.util.ArrayList;

public interface ExamUtils {
    public boolean createExam(Exam exam,ArrayList<Question> questions);
    public ArrayList<Exam> getExamsScheduledBy(String teacherId);
    public ArrayList<Exam> getExamsScheduledFor(String studentId);
    public Exam getExamById(String examId);
    public boolean submitExam(ExamResponse response);
    public ExamResponse getStudentSubmission(String examId,String studentId);
    public ExamUpdate checkExamUpdate(ExamUpdate update);
    public ExamUpdate checkExamStartUpdate(ExamUpdate update);
    public ArrayList<StudentResponse> getExamsSubmissionDetails(String examId);
    public boolean updateMarks(ExamResponse response);
}
