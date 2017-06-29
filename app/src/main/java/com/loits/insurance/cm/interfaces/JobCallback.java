package com.loits.insurance.cm.interfaces;

import com.loits.insurance.cm.models.JobList;

/**
 * Created by DumindaW on 24/11/2016.
 */

public interface JobCallback {

    void onResponse(JobList result);
    void onFailure(Throwable result);
}
