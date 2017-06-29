package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 02/02/2017.
 */

public class Job {

    private int intimationNo;
    private int inspectionType;
    private String assessorId;
    private String assessorMobile;
    private String callerName;
    private String callerMobile;
    private String incidentLoc;
    private String policyNo;
    private String vehicleNo;
    private String yearOfManuf;
    private double sumInsurance;
    private double excessAmount;
    private String hirePurchase;
    private String siteOffer;
    private String remarks;
    private long createDate;
    private int seqNo;

    public Job(int intimationNo, int inspectionType, String assessorId, String assessorMobile,
               String callerName, String callerMobile, String incidentLoc, String policyNo,
               String vehicleNo, String yearOfManuf, double sumInsurance, double excessAmount,
               String hirePurchase, String siteOffer, String remarks, long createDate, int seqNo) {
        this.intimationNo = intimationNo;
        this.inspectionType = inspectionType;
        this.assessorId = assessorId;
        this.assessorMobile = assessorMobile;
        this.callerName = callerName;
        this.callerMobile = callerMobile;
        this.incidentLoc = incidentLoc;
        this.policyNo = policyNo;
        this.vehicleNo = vehicleNo;
        this.yearOfManuf = yearOfManuf;
        this.sumInsurance = sumInsurance;
        this.excessAmount = excessAmount;
        this.hirePurchase = hirePurchase;
        this.siteOffer = siteOffer;
        this.remarks = remarks;
        this.createDate = createDate;
        this.seqNo = seqNo;
    }


    public Job(String assessorId, int inspectionType, int intimationNo){
        this.assessorId = assessorId;
        this.inspectionType = inspectionType;
        this.intimationNo = intimationNo;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }


    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public int getIntimationNo() {
        return intimationNo;
    }

    public void setIntimationNo(int intimationNo) {
        this.intimationNo = intimationNo;
    }

    public String getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(String assessorId) {
        this.assessorId = assessorId;
    }

    public String getAssessorMobile() {
        return assessorMobile;
    }

    public void setAssessorMobile(String assessorMobile) {
        this.assessorMobile = assessorMobile;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerMobile() {
        return callerMobile;
    }

    public void setCallerMobile(String callerMobile) {
        this.callerMobile = callerMobile;
    }

    public String getIncidentLoc() {
        return incidentLoc;
    }

    public void setIncidentLoc(String incidentLoc) {
        this.incidentLoc = incidentLoc;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getYearOfManuf() {
        return yearOfManuf;
    }

    public void setYearOfManuf(String yearOfManuf) {
        this.yearOfManuf = yearOfManuf;
    }

    public double getSumInsurance() {
        return sumInsurance;
    }

    public void setSumInsurance(double sumInsurance) {
        this.sumInsurance = sumInsurance;
    }

    public double getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(double excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getHirePurchase() {
        return hirePurchase;
    }

    public void setHirePurchase(String hirePurchase) {
        this.hirePurchase = hirePurchase;
    }

    public String getSiteOffer() {
        return siteOffer;
    }

    public void setSiteOffer(String siteOffer) {
        this.siteOffer = siteOffer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

}
