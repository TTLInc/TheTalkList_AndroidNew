package com.ttl.project.thetalklist.model;

import java.util.List;

public class SearchTutorsModel {

    /**
     * status : 0
     * tutors : [{"id":"19462","readytotalk":"1","roleId":"1","uid":"19462","firstName":"Parth","hRate":5.99,"avgRate":"2.57142857","lastName":"Patel","pic":"2018/04/09/dfefd717a9de6d00eeaeb215935e4f36.jpg","country":"India","isMyFavourite":"0"}]
     */

    private int status;
    private List<TutorsBean> tutors;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TutorsBean> getTutors() {
        return tutors;
    }

    public void setTutors(List<TutorsBean> tutors) {
        this.tutors = tutors;
    }

    public static class TutorsBean {
        /**
         * id : 19462
         * readytotalk : 1
         * roleId : 1
         * uid : 19462
         * firstName : Parth
         * hRate : 5.99
         * avgRate : 2.57142857
         * lastName : Patel
         * pic : 2018/04/09/dfefd717a9de6d00eeaeb215935e4f36.jpg
         * country : India
         * isMyFavourite : 0
         */

        private String id;
        private String readytotalk;
        private String roleId;
        private String uid;
        private String firstName;
        private String hRate;
        private String avgRate;
        private String lastName;
        private String pic;
        private String country;
        private String isMyFavourite;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReadytotalk() {
            return readytotalk;
        }

        public void setReadytotalk(String readytotalk) {
            this.readytotalk = readytotalk;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getHRate() {
            return hRate;
        }

        public void setHRate(String hRate) {
            this.hRate = hRate;
        }

        public String getAvgRate() {
            return avgRate;
        }

        public void setAvgRate(String avgRate) {
            this.avgRate = avgRate;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getIsMyFavourite() {
            return isMyFavourite;
        }

        public void setIsMyFavourite(String isMyFavourite) {
            this.isMyFavourite = isMyFavourite;
        }
    }
}
