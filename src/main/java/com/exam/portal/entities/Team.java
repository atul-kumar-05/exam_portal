package com.exam.portal.entities;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Date;

public class Team {
    private String teamId;
    private String name;
    private String creatorId;

    @Temporal(TemporalType.DATE)
    private Date dateCreated;

    public Team(){}

    public Team(String teamId, String name, String creatorId, Date dateCreated) {
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
