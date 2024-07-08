package com.exam.portal.entities;

import java.util.ArrayList;

public class MessageUpdate {
    private String teamId;
    private int prevCount;
    private boolean update;
    private ArrayList<Message> messages;

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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
