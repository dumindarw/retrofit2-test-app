package com.loits.insurance.cm.interfaces;

import com.loits.insurance.cm.models.Login;

/**
 * Created by DumindaW on 24/11/2016.
 */

public interface LoginCallback {

    void onResponse(Login result);
    void onFailure(Throwable result);
}
