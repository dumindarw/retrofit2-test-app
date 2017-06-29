package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 31/01/2017.
 */

public class ClaimListItem {

    private String vehicleNo;
    private String date;
    private boolean isCompleted;
    /*private String progress;*/

    public ClaimListItem(String vehicleNo, String date) {
        this.vehicleNo = vehicleNo;
        this.date = date;
    }

    public ClaimListItem(String vehicleNo, String date, boolean isCompleted/*, String progress*/) {
        this.vehicleNo = vehicleNo;
        this.date = date;
        this.isCompleted = isCompleted;
        //this.progress = progress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    /*public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }*/

}
