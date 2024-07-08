package com.exam.portal.entities.jsonclasses;

import java.util.ArrayList;

public class JsonObject {
    private ArrayList<Tags> tags;

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "tags=" + tags +
                '}';
    }
}
