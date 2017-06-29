package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 02/02/2017.
 */

public class Assessor {

    private String assessorId;

    public Assessor(String assessorId){
        this.assessorId = assessorId;
    }

    public String getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(String assessorId) {
        this.assessorId = assessorId;
    }
}
