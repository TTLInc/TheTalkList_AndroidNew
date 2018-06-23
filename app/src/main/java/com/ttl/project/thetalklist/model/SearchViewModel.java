package com.ttl.project.thetalklist.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

import java.util.List;

public class SearchViewModel implements ChipInterface{


    private int status;
    private List<SubjectBean> subject;
    private List<LocationBean> location;
    private List<PeopleBean> people;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SubjectBean> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectBean> subject) {
        this.subject = subject;
    }

    public List<LocationBean> getLocation() {
        return location;
    }

    public void setLocation(List<LocationBean> location) {
        this.location = location;
    }

    public List<PeopleBean> getPeople() {
        return people;
    }

    public void setPeople(List<PeopleBean> people) {
        this.people = people;
    }

    @Override
    public Object getId() {
        return null;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    public static class SubjectBean {
        /**
         * subject : Arabic
         */

        private String subject;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public static class LocationBean {
        /**
         * country : Algeria
         */

        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class PeopleBean {
        /**
         * name : AliSharif-Idiris
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
