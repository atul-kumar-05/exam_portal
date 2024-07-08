package com.exam.portal.entities.jsonclasses;

import java.util.ArrayList;

public class Faces {
    private ArrayList<Face> faces;

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }

    @Override
    public String toString() {
        return "Faces{" +
                "faces=" + faces +
                '}';
    }
}
