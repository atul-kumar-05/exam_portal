package com.exam.portal.entities.jsonclasses;

public class Face {
    private double confidence;
    private Coordinates coordinates;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Face{" +
                "confidence=" + confidence +
                ", coordinates=" + coordinates +
                '}';
    }
}

class Coordinates {
    private double height;
    private double width;
    private double xmax;
    private double xmin;
    private double ymax;
    private double ymin;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getXmax() {
        return xmax;
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public double getXmin() {
        return xmin;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public double getYmin() {
        return ymin;
    }

    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "height=" + height +
                ", width=" + width +
                ", xmax=" + xmax +
                ", xmin=" + xmin +
                ", ymax=" + ymax +
                ", ymin=" + ymin +
                '}';
    }
}
