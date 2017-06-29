package com.loits.insurance.cm.models;

/**
 * Created by DumindaW on 07/02/2017.
 */

public class InspectionType {

    String spinnerText;
    int value;

    public InspectionType( String spinnerText, int value ) {
        this.spinnerText = spinnerText;
        this.value = value;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return spinnerText;
    }

}