package com.exam.portal.models;

public class Team {
    private String teamId;
    private String name;
    private String creatorId;
    private String dateCreated;

    public Team(){}

    public Team(String teamId, String name, String creatorId, String dateCreated) {
        this.teamId = teamId;
        this.name = name;
        this.creatorId = creatorId;
        this.dateCreated = dateCreated;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
