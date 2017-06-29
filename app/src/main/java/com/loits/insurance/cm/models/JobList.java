package com.loits.insurance.cm.models;

import java.util.List;

/**
 * Created by DumindaW on 02/02/2017.
 */

public class JobList {

    private List<Job> insuranceJob;

    public JobList(List<Job> insuranceJob) {
        super();
        this.insuranceJob = insuranceJob;
    }

    public List<Job> getInsuranceJob() {
        return insuranceJob;
    }

    public void setInsuranceJob(List<Job> insuranceJob) {
        this.insuranceJob = insuranceJob;
    }

}
