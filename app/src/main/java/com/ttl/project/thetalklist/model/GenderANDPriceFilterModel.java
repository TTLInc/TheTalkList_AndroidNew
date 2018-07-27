package com.ttl.project.thetalklist.model;

import java.util.List;

public class GenderANDPriceFilterModel {

    /**
     * status : 0
     * tutors : [{"id":"19462","readytotalk":"1","roleId":"1","uid":"19462","firstName":"Parth","hRate":5.99,"avgRate":"2.88888888","lastName":"Patel","pic":"2018/04/09/dfefd717a9de6d00eeaeb215935e4f36.jpg","country":"India","isMyFavourite":"0"},{"id":"19764","readytotalk":"0","roleId":"1","uid":"19764","firstName":"Harshad","hRate":5.65,"avgRate":"3.75","lastName":"Vrsi","pic":"2018/03/30/1487f82426559b76bf1913c86bd25edd.jpg","country":"Andorra","isMyFavourite":"0"},{"id":"17618","readytotalk":"0","roleId":"1","uid":"17618","firstName":"Carter","hRate":7.32,"avgRate":"3.33333333","lastName":"Tutor","pic":"2017/10/18/1e40d104b9318513f6b346ba127a599d.jpg","country":"USA","isMyFavourite":"0"},{"id":"362","readytotalk":"0","roleId":"1","uid":"362","firstName":"Marissa","hRate":15.96,"avgRate":"","lastName":"Pearl","pic":"2018/01/07/5735cbd178deff3ae4feb3d292d1c969.jpg","country":"Germany","isMyFavourite":"0"},{"id":"11671","readytotalk":"0","roleId":"1","uid":"11671","firstName":"Efi","hRate":13.3,"avgRate":"","lastName":"","pic":"2015/10/08/22cc2869522503617fb42399e7a8049a.jpg","country":"Germany","isMyFavourite":"0"},{"id":"19662","readytotalk":"0","roleId":"1","uid":"19662","firstName":"Diggy","hRate":5.65,"avgRate":"","lastName":"Dum","pic":"2018/03/12/a23b2107e688d51ce531347c7064991f.jpg","country":"Algeria","isMyFavourite":"0"},{"id":"19379","readytotalk":"0","roleId":"1","uid":"19379","firstName":"Haris","hRate":5,"avgRate":"","lastName":"","pic":"2018/02/22/56878326f14e87157555602654d37346.jpeg","country":"Germany","isMyFavourite":"0"},{"id":"19466","readytotalk":"0","roleId":"1","uid":"19466","firstName":"Ray","hRate":5.65,"avgRate":"","lastName":"Sabastein","pic":"2018/02/14/cc3e1c0433e90bab189bd74776dba648.jpg","country":"Australia","isMyFavourite":"0"},{"id":"19014","readytotalk":"0","roleId":"1","uid":"19014","firstName":"Hehdhd","hRate":5.65,"avgRate":"","lastName":"Nxnxhxhxh","pic":"2017/12/27/1bc301f0b9d7cb06b88f9d0297ea5f03.jpg","country":"Angola","isMyFavourite":"0"},{"id":"19204","readytotalk":"0","roleId":"1","uid":"19204","firstName":"Tina","hRate":5,"avgRate":"","lastName":"","pic":"2018/01/20/db8449914859a079eb8d5407eecfb1f7.jpg","country":"Germany","isMyFavourite":"0"}]
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
         * avgRate : 2.88888888
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
        private double hRate;
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

        public double getHRate() {
            return hRate;
        }

        public void setHRate(double hRate) {
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
