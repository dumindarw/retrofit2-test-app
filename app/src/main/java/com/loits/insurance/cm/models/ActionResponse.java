package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 06/02/2017.
 */

public class ActionResponse {

    private String message;
    private int code;

    public ActionResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
