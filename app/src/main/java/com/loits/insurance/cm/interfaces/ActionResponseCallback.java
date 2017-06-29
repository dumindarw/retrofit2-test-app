package com.loits.insurance.cm.interfaces;

import com.loits.insurance.cm.models.ActionResponse;

/**
 * Created by DumindaW on 24/11/2016.
 */

public interface ActionResponseCallback {

    void onResponse(ActionResponse result);
    void onFailure(Throwable result);
}
