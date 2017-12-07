package com.sxz.reaction.model;

import java.util.Date;

/**
 * Created by Shihao on 12/6/17.
 */

public class Record {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Float getTime() {
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

    private String userName;
    private Float time;
    private Date date;
}
