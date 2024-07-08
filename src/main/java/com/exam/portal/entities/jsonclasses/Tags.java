package com.exam.portal.entities.jsonclasses;

public class Tags {
    private double confidence;
    private Tag tag;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "confidence=" + confidence +
                ", tag=" + tag +
                '}';
    }
}
