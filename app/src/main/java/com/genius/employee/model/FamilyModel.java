package com.genius.employee.model;

public class FamilyModel {
    String name,realation,information;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealation() {
        return realation;
    }

    public void setRealation(String realation) {
        this.realation = realation;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public FamilyModel(String name, String realation, String information) {
        this.name = name;
        this.realation = realation;
        this.information = information;
    }
}
