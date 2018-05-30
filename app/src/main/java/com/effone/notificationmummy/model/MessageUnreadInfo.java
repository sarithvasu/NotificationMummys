package com.effone.notificationmummy.model;

/**
 * Created by sumanth.peddinti on 5/15/2018.
 */

public class MessageUnreadInfo {
    private int unread;
    private String sender_name;
    private String sender_message;
    private int sender_id;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_message() {
        return sender_message;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int  sender_id) {
        this.sender_id = sender_id;
    }
}
