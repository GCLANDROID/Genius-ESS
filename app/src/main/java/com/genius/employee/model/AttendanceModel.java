package com.genius.employee.model;

public class AttendanceModel {
    String workingdays,attendance,payable,absent,otherAbsent,leaveSanction,otherLaveSanction,lwp,otherLWP,monthName,MonthView;

    public AttendanceModel(String workingdays, String attendance, String payabledays,String absent, String otherAbsent, String leaveSanction, String otherLaveSanction, String lwp, String otherLWP,String month,String MonthView) {
        this.workingdays = workingdays;
        this.attendance = attendance;
        this.payable=payabledays;
        this.absent = absent;
        this.otherAbsent = otherAbsent;
        this.leaveSanction = leaveSanction;
        this.otherLaveSanction = otherLaveSanction;
        this.lwp = lwp;
        this.otherLWP = otherLWP;
        this.monthName=month;
        this.MonthView=MonthView;
    }

    public String getWorkingdays() {
        return workingdays;
    }

    public void setWorkingdays(String workingdays) {
        this.workingdays = workingdays;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getOtherAbsent() {
        return otherAbsent;
    }

    public void setOtherAbsent(String otherAbsent) {
        this.otherAbsent = otherAbsent;
    }

    public String getLeaveSanction() {
        return leaveSanction;
    }

    public void setLeaveSanction(String leaveSanction) {
        this.leaveSanction = leaveSanction;
    }

    public String getOtherLaveSanction() {
        return otherLaveSanction;
    }

    public void setOtherLaveSanction(String otherLaveSanction) {
        this.otherLaveSanction = otherLaveSanction;
    }

    public String getLwp() {
        return lwp;
    }

    public void setLwp(String lwp) {
        this.lwp = lwp;
    }

    public String getOtherLWP() {
        return otherLWP;
    }

    public void setOtherLWP(String otherLWP) {
        this.otherLWP = otherLWP;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthView() {
        return MonthView;
    }

    public void setMonthView(String monthView) {
        MonthView = monthView;
    }
}
