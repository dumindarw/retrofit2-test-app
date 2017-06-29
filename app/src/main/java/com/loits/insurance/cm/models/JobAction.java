package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 06/02/2017.
 */

public class JobAction {

    private int intimationNo;
    private int inspectionType;
    private String assessorId;
    private String reason;

    public JobAction(int intimationNo, int inspectionType, String assessorId, String reason) {
        this.intimationNo = intimationNo;
        this.inspectionType = inspectionType;
        this.assessorId = assessorId;
        this.reason = reason;
    }

    public int getIntimationNo() {
        return intimationNo;
    }

    public void setIntimationNo(int intimationNo) {
        this.intimationNo = intimationNo;
    }

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(String assessorId) {
        this.assessorId = assessorId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
