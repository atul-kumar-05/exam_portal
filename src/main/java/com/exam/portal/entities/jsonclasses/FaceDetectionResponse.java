package com.exam.portal.entities.jsonclasses;

public class FaceDetectionResponse {
    private Faces result;

    public Faces getResult() {
        return result;
    }

    public void setResult(Faces result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "FaceDetectionResponse{" +
                "result=" + result +
                '}';
    }
}
