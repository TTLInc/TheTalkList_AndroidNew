package com.ttl.project.thetalklist.Pagination;

/**
 * Created by Saubhagyam on 22/09/2017.
 */

public class History_model {

    String name;
    String Tutorname;
    String date;
    String rate;
    int sid;
    int tid;
    String hRate;

    public String gethRate() {
        return hRate;
    }

    public void sethRate(String hRate) {
        this.hRate = hRate;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTutorname() {
        return Tutorname;
    }

    public void setTutorname(String tutorname) {
        Tutorname = tutorname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
