package com.exam.portal.models;


import java.sql.Timestamp;

public class Message {
    private String messageId;
    private  String teamId;
    private  String senderId;
    private String senderName;
    private  String message;
    private Timestamp date;

    public  void setMessageId(String messageId){this.messageId = messageId;}

    public  void  setTeamId(String teamId){this.teamId=teamId;}

    public  void  setSenderId(String senderId){this.senderId=senderId;}

    public  void setMessage(String message){this.message = message;}

    public  void  setDate(Timestamp date){this.date=date;}

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public  String getMessageId(){return messageId;}

    public  String getTeamId(){return teamId;}

    public String getSenderId(){return senderId;}

    public  String getMessage(){return message;}

    public Timestamp getDate(){return date;}
}
