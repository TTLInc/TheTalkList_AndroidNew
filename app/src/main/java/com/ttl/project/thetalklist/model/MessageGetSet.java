package com.ttl.project.thetalklist.model;

/**
 * Created by sony on 4/28/2017.
 */

public class MessageGetSet {

    private String ReceivedMessage;
    private String ReceivedDate;
    private String Sender;
    private String time, text;
    private String cname;
    private String userName;
    private String Id;



    public MessageGetSet(String text, String time, String userName, String Id) {


        this.text = text;
        this.time = time;
        this.userName = userName;
        this.Id = Id;


    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }


    public String getReceivedDate() {
        return ReceivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        ReceivedDate = receivedDate;
    }

    public String getReceivedMessage() {
        return ReceivedMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        ReceivedMessage = receivedMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
