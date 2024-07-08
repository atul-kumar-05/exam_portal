package com.exam.portal.entities;

import java.util.Base64;

public class Image {
    String studentId;
    String bytes;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public byte[] decodeBytes(){
        return Base64.getDecoder().decode(this.bytes);
    }
}
