package com.effone.notificationmummy.model;

/**
 * Created by sumanth.peddinti on 5/11/2018.
 */

public class NotificaitionMessageInfo {
    private String sender_name,sender_ph_number,sender_message,sender_message_formatted,package_name;
    private long sender_message_time_stamp;
    private int sender_Id,is_read;
    private  String sender_date,message_type,strAppName;
    private int is_allowed;

    public String getStrAppName() {
        return strAppName;
    }

    public void setStrAppName(String strAppName) {
        this.strAppName = strAppName;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String sender_type) {
        this.message_type = sender_type;
    }

    public String getSender_date() {
        return sender_date;
    }

    public void setSender_date(String sender_date) {
        this.sender_date = sender_date;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_ph_number() {
        return sender_ph_number;
    }

    public void setSender_ph_number(String sender_ph_number) {
        this.sender_ph_number = sender_ph_number;
    }

    public int getIs_allowed() {
        return is_allowed;
    }

    public void setIs_allowed(int is_allowed) {
        this.is_allowed = is_allowed;
    }

    public String getSender_message() {
        return sender_message;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }

    public String getSender_message_formatted() {
        return sender_message_formatted;
    }

    public void setSender_message_formatted(String sender_message_formatted) {
        this.sender_message_formatted = sender_message_formatted;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public long getSender_message_time_stamp() {
        return sender_message_time_stamp;
    }

    public void setSender_message_time_stamp(long sender_message_time_stamp) {
        this.sender_message_time_stamp = sender_message_time_stamp;
    }

    public int getSender_Id() {
        return sender_Id;
    }

    public void setSender_Id(int sender_Id) {
        this.sender_Id = sender_Id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }



}
