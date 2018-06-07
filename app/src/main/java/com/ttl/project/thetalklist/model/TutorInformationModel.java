package com.ttl.project.thetalklist.model;

import java.util.List;

public class TutorInformationModel {

    /**
     * status : 0
     * tutor : [{"id":"405","readytotalk":"0","roleId":"2","uid":"405","firstName":"Andres","hRate":0,"lastName":"Abeyta","pic":"2018/06/04/ec1526336859058eda78c6e5c7f03ae0.jpg","avgRate":"4.57894736","country":"USA"}]
     */

    private int status;
    private List<TutorBean> tutor;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TutorBean> getTutor() {
        return tutor;
    }

    public void setTutor(List<TutorBean> tutor) {
        this.tutor = tutor;
    }

    public static class TutorBean {
        /**
         * id : 405
         * readytotalk : 0
         * roleId : 2
         * uid : 405
         * firstName : Andres
         * hRate : 0
         * lastName : Abeyta
         * pic : 2018/06/04/ec1526336859058eda78c6e5c7f03ae0.jpg
         * avgRate : 4.57894736
         * country : USA
         */

        private String id;
        private String readytotalk;
        private String roleId;
        private String uid;
        private String firstName;
        private double hRate;
        private String lastName;
        private String pic;
        private String avgRate;
        private String country;

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

        public double gethRate() {
            return hRate;
        }

        public void sethRate(double hRate) {
            this.hRate = hRate;
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

        public String getAvgRate() {
            return avgRate;
        }

        public void setAvgRate(String avgRate) {
            this.avgRate = avgRate;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
