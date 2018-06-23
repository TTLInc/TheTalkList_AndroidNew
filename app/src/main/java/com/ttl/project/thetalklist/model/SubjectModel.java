package com.ttl.project.thetalklist.model;

import java.util.List;

public class SubjectModel {

    /**
     * status : 0
     * subjects : [{"id":"2","subject":"Arabic"},{"id":"11","subject":"Biology"},{"id":"12","subject":"Business"},{"id":"13","subject":"Chemistry"},{"id":"3","subject":"Chinese"},{"id":"14","subject":"Computer Science"},{"id":"15","subject":"Engineering"},{"id":"1","subject":"English"},{"id":"4","subject":"French"},{"id":"5","subject":"German"},{"id":"6","subject":"Japanese"},{"id":"7","subject":"Korean"},{"id":"16","subject":"Math"},{"id":"17","subject":"Physics"},{"id":"8","subject":"Portuguese"},{"id":"9","subject":"Russian"},{"id":"10","subject":"Spanish"}]
     */

    private int status;
    private List<SubjectsBean> subjects;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectsBean {
        /**
         * id : 2
         * subject : Arabic
         */

        private String id;
        private String subject;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
