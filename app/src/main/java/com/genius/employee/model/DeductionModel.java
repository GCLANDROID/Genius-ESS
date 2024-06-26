package com.genius.employee.model;

public class DeductionModel {
    String compName,amt;

    public DeductionModel(String compName, String amt) {
        this.compName = compName;
        this.amt = amt;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
