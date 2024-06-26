package com.genius.employee.model;

public class DailyReportModel {
    String visitingDate,visitingClientName,inTime,inLocation,outTime,outLocation,remarks,regTime,status,reglat,regLong,inLat,inLong,distance,contactperson,visitingPurpose,contactnumber,email,outLat,outLang;

    public DailyReportModel(String visitingDate, String visitingClientName, String inTime, String inLocation, String outTime, String outLocation, String remarks, String regTime, String status, String reglat, String regLong, String inLat, String inLong, String distance, String contactperson, String visitingPurpose, String contactnumber, String email,String outlat,String outLang) {
        this.visitingDate = visitingDate;
        this.visitingClientName = visitingClientName;
        this.inTime = inTime;
        this.inLocation = inLocation;
        this.outTime = outTime;
        this.outLocation = outLocation;
        this.remarks = remarks;
        this.regTime = regTime;
        this.status = status;
        this.reglat = reglat;
        this.regLong = regLong;
        this.inLat = inLat;
        this.inLong = inLong;
        this.distance = distance;
        this.contactperson = contactperson;
        this.visitingPurpose = visitingPurpose;
        this.contactnumber = contactnumber;
        this.email = email;
        this.outLat=outlat;
        this.outLang=outLang;
    }

    public String getVisitingDate() {
        return visitingDate;
    }

    public void setVisitingDate(String visitingDate) {
        this.visitingDate = visitingDate;
    }

    public String getVisitingClientName() {
        return visitingClientName;
    }

    public void setVisitingClientName(String visitingClientName) {
        this.visitingClientName = visitingClientName;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getInLocation() {
        return inLocation;
    }

    public void setInLocation(String inLocation) {
        this.inLocation = inLocation;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getOutLocation() {
        return outLocation;
    }

    public void setOutLocation(String outLocation) {
        this.outLocation = outLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReglat() {
        return reglat;
    }

    public void setReglat(String reglat) {
        this.reglat = reglat;
    }

    public String getRegLong() {
        return regLong;
    }

    public void setRegLong(String regLong) {
        this.regLong = regLong;
    }

    public String getInLat() {
        return inLat;
    }

    public void setInLat(String inLat) {
        this.inLat = inLat;
    }

    public String getInLong() {
        return inLong;
    }

    public void setInLong(String inLong) {
        this.inLong = inLong;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getVisitingPurpose() {
        return visitingPurpose;
    }

    public void setVisitingPurpose(String visitingPurpose) {
        this.visitingPurpose = visitingPurpose;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOutLat() {
        return outLat;
    }

    public void setOutLat(String outLat) {
        this.outLat = outLat;
    }

    public String getOutLang() {
        return outLang;
    }

    public void setOutLang(String outLang) {
        this.outLang = outLang;
    }
}
