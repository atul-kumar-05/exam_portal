package com.exam.portal.entities.jsonclasses;

public class Tag {
    private String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "en='" + en + '\'' +
                '}';
    }
}
