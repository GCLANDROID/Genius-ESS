package com.genius.employee.model;

public class FeedBackReportModule {
    String date,time,clintName,clintOffName,cpName,cpDes,cpEmail,cpMob,address,grpAnswer,remark;

    public FeedBackReportModule(String date, String time, String clintName, String clintOffName, String cpName, String cpDes, String cpEmail, String cpMob, String address, String grpAnswer, String remark) {
        this.date = date;
        this.time = time;
        this.clintName = clintName;
        this.clintOffName = clintOffName;
        this.cpName = cpName;
        this.cpDes = cpDes;
        this.cpEmail = cpEmail;
        this.cpMob = cpMob;
        this.address = address;
        this.grpAnswer=grpAnswer;
        this.remark=remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClintName() {
        return clintName;
    }

    public void setClintName(String clintName) {
        this.clintName = clintName;
    }

    public String getClintOffName() {
        return clintOffName;
    }

    public void setClintOffName(String clintOffName) {
        this.clintOffName = clintOffName;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCpDes() {
        return cpDes;
    }

    public void setCpDes(String cpDes) {
        this.cpDes = cpDes;
    }

    public String getCpEmail() {
        return cpEmail;
    }

    public void setCpEmail(String cpEmail) {
        this.cpEmail = cpEmail;
    }

    public String getCpMob() {
        return cpMob;
    }

    public void setCpMob(String cpMob) {
        this.cpMob = cpMob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrpAnswer() {
        return grpAnswer;
    }

    public void setGrpAnswer(String grpAnswer) {
        this.grpAnswer = grpAnswer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
