package com.genius.employee.model;

public class ComponentModel {
    String componentName,grossAmt,actualAmt;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
    }

    public String getActualAmt() {
        return actualAmt;
    }

    public void setActualAmt(String actualAmt) {
        this.actualAmt = actualAmt;
    }

    public ComponentModel(String componentName, String grossAmt, String actualAmt) {
        this.componentName = componentName;
        this.grossAmt = grossAmt;
        this.actualAmt = actualAmt;
    }
}
