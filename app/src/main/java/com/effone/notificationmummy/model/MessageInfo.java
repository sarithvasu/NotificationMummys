package com.effone.notificationmummy.model;

import java.io.Serializable;

/**
 * Created by sumanth.peddinti on 5/15/2018.
 */

public class MessageInfo implements Serializable {
    private int message_id;
    private int sender_id;



    private String sender_message;
    private String sender_message_format;
    private String sender_message_time_stamp;
    private String message_type;
    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMessage_type() {
        return message_type;

    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_message() {
        return sender_message;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }

    public String getSender_message_format() {
        return sender_message_format;
    }

    public void setSender_message_format(String sender_message_format) {
        this.sender_message_format = sender_message_format;
    }

    public String getSender_message_time_stamp() {
        return sender_message_time_stamp;
    }

    public void setSender_message_time_stamp(String sender_message_time_stamp) {
        this.sender_message_time_stamp = sender_message_time_stamp;
    }
    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}
