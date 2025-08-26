package com.genius.employee.model;

public class AddExperienceModel {
    String JobRecordId="",organizationName,fromDate,toDate,designation,ctc,location;
    double experience;

    public AddExperienceModel(String organizationName, String fromDate, String toDate, String designation, double experience, String ctc, String location) {
        this.organizationName = organizationName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.designation = designation;
        this.experience = experience;
        this.ctc = ctc;
        this.location = location;
    }



    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJobRecordId() {
        return JobRecordId;
    }

    public void setJobRecordId(String jobRecordId) {
        JobRecordId = jobRecordId;
    }
}
