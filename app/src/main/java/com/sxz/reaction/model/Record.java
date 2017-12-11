package com.sxz.reaction.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Shihao on 12/6/17.
 */


public class Record {

    public static class Type {
        public static final int AUDITORY = 0;
        public static final int VISUAL = 1;
    }

    @SerializedName("uid")
    private String userID;
    private float time;
    private Date date;
    private int type;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
