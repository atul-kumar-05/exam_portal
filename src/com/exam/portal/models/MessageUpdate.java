package com.exam.portal.models;

import java.util.ArrayList;

public class MessageUpdate {
    private String teamId;
    private int prevCount;
    private boolean update;
    private ArrayList<Message> messages;

    public MessageUpdate(){}

    public MessageUpdate(String teamId,int prevCount){
        this.teamId = teamId;
        this.prevCount = prevCount;
        update = false;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getPrevCount() {
        return prevCount;
    }

    public void setPrevCount(int prevCount) {
        this.prevCount = prevCount;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
